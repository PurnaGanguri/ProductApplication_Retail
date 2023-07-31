package org.product.services;

import lombok.extern.slf4j.Slf4j;
import org.product.dto.ProductDTO;
import org.product.model.ApprovalQueue;
import org.product.model.Product;
import org.product.repository.ApprovalQueueRepository;
import org.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService
{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ApprovalQueueRepository approvalQueueRepository;

    @Override
    public List<ProductDTO> getActiveProducts() {
        log.info("Get all list of active products......");
        List<Product> products =  productRepository.findByStatusTrueOrderByPostedDateDesc();
        List<ProductDTO> productDTOS = products.stream().map(this::convertToDto).collect(Collectors.toList());
        return productDTOS;

    }

    @Override
    public List<ProductDTO> searchProducts(Specification<Product> spec) {
        log.info("Search for the required product......");
        List<Product> entityList = productRepository.findAll(spec);
        List<ProductDTO> outPutDTO =entityList.stream().map(x ->convertToDto(x)).collect(Collectors.toList());
        return outPutDTO;
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        log.info("Create a new product........");
        Product product = convertToEntity(productDTO, null);

        if (product.getPrice() >10000) {
           product.setStatus(false);
        } else if (product.getPrice() > 5000) {
            product.setStatus(true);
        } else {
            product.setStatus(true);
        }
        product = productRepository.save(product);
        return convertToDto(product);
    }

    @Override
    public ProductDTO updateProduct( Long productId, ProductDTO productDTO) {

        log.info("Update the product details....");

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("product not found"));
        Product updatedProduct = convertToEntity(productDTO, existingProduct);
        if (updatedProduct.getPrice() > existingProduct.getPrice() * 1.5) {
            updatedProduct.setStatus(false); // Push to approval queue
        } else {
            updatedProduct.setStatus(true);
        }
        updatedProduct.setId(productId);
        Product updatedClient = productRepository.save(updatedProduct);
        return convertToDto(updatedClient);
    }

    @Override
    public void deleteProduct(Long productId) {
        log.info("Delete the product........");
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        existingProduct.setStatus(false);
        productRepository.save(existingProduct);
    }

    @Override
    public List<ProductDTO> getProductsInApprovalQueue() {
        log.info("Get the list of product details that are in approval queue..... ");
        List<Product> products = approvalQueueRepository.findAll().stream().map(ApprovalQueue::getProduct).collect(Collectors.toList());
        List<ProductDTO> productDTOS = products.stream().map(this::convertToDto).collect(Collectors.toList());
        return productDTOS;
    }

    @Override
    public void approveProduct(Long approvalId) {
        log.info("Approve the product.......");
        ApprovalQueue approval = approvalQueueRepository.findById(approvalId)
                .orElseThrow(() -> new IllegalArgumentException("Approval not found"));
        Product product = approval.getProduct();
        product.setStatus(true);
        productRepository.save(product);
        approvalQueueRepository.delete(approval);
    }

    @Override
    public void rejectProduct(Long approvalId) {
        log.info("Reject the product ................");
        ApprovalQueue approval = approvalQueueRepository.findById(approvalId)
                .orElseThrow(() -> new IllegalArgumentException("Approval not found"));
        approvalQueueRepository.delete(approval);
    }

    private ProductDTO convertToDto(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setStatus(product.isStatus());
        productDTO.setPrice(product.getPrice());
        productDTO.setPostedDate(product.getPostedDate());
        return productDTO;
    }

    private Product convertToEntity(ProductDTO productDTO, Product product) {

        if(product == null){
            product = new Product();
        }
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setStatus(productDTO.isStatus());
        product.setPrice(productDTO.getPrice());
        product.setPostedDate(productDTO.getPostedDate());
        return product;
    }

}