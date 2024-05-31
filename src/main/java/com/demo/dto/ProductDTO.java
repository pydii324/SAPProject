package com.demo.dto;

import jakarta.persistence.Table;
import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String title;
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
    private byte[] image;
}
