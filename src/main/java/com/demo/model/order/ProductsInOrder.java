package com.demo.model.order;

import com.demo.model.product.ProductEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_product")
@Data
public class ProductsInOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private int quantity;
    private float productPrice;
}
