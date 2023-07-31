package org.product.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;

    private  double minPrice;
    private  double maxPrice;

    private boolean status;

    private LocalDateTime postedDate;
    private LocalDateTime minPostedDate;
    private LocalDateTime maxPostedDate;


}
