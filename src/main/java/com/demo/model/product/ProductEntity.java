package com.demo.model.product;

import com.demo.model.order.ProductsInOrder;
import com.demo.model.sale.Sale;
import com.demo.model.user.UserEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "products")
@Data
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(name = "price", columnDefinition = "DECIMAL(8,2)")
    private Float price;
    private Float minDiscountPrice;
    private String author;
    private Integer availableQuantity;
    private Integer pageCount;
    private String genre;
    private String language;
    private String countryOfOrigin;
    private String typeOfCover;
    private Integer weight;
    private Integer dimensionX;
    private Integer dimensionY;
    private String description;

    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    @ManyToMany
    private List<Sale> sales;

    /*@OneToMany(mappedBy = "product")
    private List<ProductsInOrder> orderItems;*/
}
