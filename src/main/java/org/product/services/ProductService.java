package org.product.services;

import org.product.dto.ProductDTO;
import org.product.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getActiveProducts();

    List<ProductDTO> searchProducts(Specification<Product> spec);

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(Long productId,ProductDTO productDTO);

     void deleteProduct(Long productId);

    List<ProductDTO> getProductsInApprovalQueue();

    void approveProduct(Long approvalId);

     void rejectProduct (Long approvalId);
}
