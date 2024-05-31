package com.demo.service.logic;

import com.demo.dto.ProductDTO;
import com.demo.exception.product.ProductNotExistsException;
import com.demo.model.product.ProductEntity;
import com.demo.repository.ProductRepository;
import com.demo.service.layer.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // Create
    public void saveProduct(ProductDTO productDTO) {
        ProductEntity product = new ProductEntity();
        product.setTitle(productDTO.getTitle());
        product.setPrice(productDTO.getPrice());
        product.setAuthor(productDTO.getAuthor());
        product.setAvailableQuantity(productDTO.getAvailableQuantity());
        product.setPageCount(productDTO.getPageCount());
        product.setGenre(productDTO.getGenre());
        product.setLanguage(productDTO.getLanguage());
        product.setCountryOfOrigin(productDTO.getCountryOfOrigin());
        product.setTypeOfCover(productDTO.getTypeOfCover());
        product.setWeight(productDTO.getWeight());
        product.setDimensionX(productDTO.getDimensionX());
        product.setDimensionY(productDTO.getDimensionY());
        product.setDescription(productDTO.getDescription());
        product.setImage(productDTO.getImage());

        productRepository.save(product);
    }

    // Read
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }
    public ProductEntity getProductById(Long id) throws ProductNotExistsException{
        Optional<ProductEntity> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty())
            throw new ProductNotExistsException("Product with id: "+id+" does not exist!");
        return optionalProduct.get();
    }

    // Update
    public boolean modifyProductById(Long id, ProductDTO newProductData) throws ProductNotExistsException {
        Optional<ProductEntity> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isEmpty())
            //throw new ProductNotExistsException("Product with id: "+id+" does not exist!");
            return false;

        ProductEntity existingProduct = optionalProduct.get();

        System.out.println(newProductData.getTitle());
        if (newProductData.getTitle() != null)
            existingProduct.setTitle(newProductData.getTitle());

        if (newProductData.getPrice() != null)
            existingProduct.setPrice(newProductData.getPrice());

        if (newProductData.getMinDiscountPrice() != null)
            existingProduct.setMinDiscountPrice(newProductData.getMinDiscountPrice());

        if (newProductData.getAvailableQuantity() != null)
            existingProduct.setAvailableQuantity(newProductData.getAvailableQuantity());

        if (newProductData.getTypeOfCover() != null)
            existingProduct.setTypeOfCover(newProductData.getTypeOfCover());

        if (newProductData.getDescription() != null)
            existingProduct.setDescription(newProductData.getDescription());

        if (newProductData.getImage() != null)
            existingProduct.setImage(newProductData.getImage());

        productRepository.save(existingProduct);
        return true;
    }

    // Delete
    public void deleteProductById(Long id) throws ProductNotExistsException {
        Optional<ProductEntity> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty())
            throw new ProductNotExistsException("Product with id: "+id+" does not exist!");
        productRepository.deleteById(id);
    }
}
