package com.demo.service.layer;

import com.demo.dto.ProductDTO;
import com.demo.exception.product.ProductNotExistsException;
import com.demo.model.product.ProductEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {

    // Create
    void saveProduct(ProductDTO product);

    // Read
    List<ProductEntity> getAllProducts();
    ProductEntity getProductById(Long id)
            throws ProductNotExistsException;

    // Update
    boolean modifyProductById(Long id, ProductDTO newUserData)
        throws ProductNotExistsException;

    // Delete
    void deleteProductById(Long id)
        throws ProductNotExistsException;

}
