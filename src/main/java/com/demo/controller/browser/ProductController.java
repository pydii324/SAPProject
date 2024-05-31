package com.demo.controller.browser;

import com.demo.dto.ProductDTO;
import com.demo.exception.product.ProductNotExistsException;
import com.demo.model.product.ProductEntity;
import com.demo.repository.ProductRepository;
import com.demo.service.layer.ProductService;
import com.demo.service.logic.ImageDecoder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@AllArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private ProductService productService;
    private final ImageDecoder imageDecoder;

    @GetMapping("/create_product")
    public String createProduct() {
        return "create_product";
    }

    @PostMapping("/create_product")
    public String saveProduct(@RequestParam String title,
                              @RequestParam String description,
                              @RequestParam Float price,
                              @RequestParam Integer availableQuantity,
                              @RequestParam MultipartFile image) throws IOException {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setTitle(title);
        productDTO.setDescription(description);
        productDTO.setPrice(price);
        productDTO.setAvailableQuantity(availableQuantity);
        productDTO.setImage(image.getBytes());

        productService.saveProduct(productDTO);
        return "login";
    }

    @GetMapping("/modifyProduct")
    public String modifyProduct() {
        return "modifyProduct";
    }

    @PostMapping("/modifyProduct")
    public String modifySaveProduct(
                                @RequestParam(name = "id") Long id,
                                @RequestParam(name = "title", required = false) String title,
                                @RequestParam(name = "description", required = false) String description,
                                @RequestParam(name = "price", required = false) Float price,
                                @RequestParam(name = "availableQuantity", required = false) Integer availableQuantity,
                                @RequestParam(name = "image", required = false) MultipartFile image) throws IOException, ProductNotExistsException {

        ProductEntity product = productService.getProductById(id);
        if (product == null)
            return "redirect:/modifyProduct?fail";

        if (!title.isEmpty())
            product.setTitle(title);
        if (!description.isEmpty())
            product.setDescription(description);
        if (price != null)
            product.setPrice(price);
        if (availableQuantity != null)
            product.setAvailableQuantity(availableQuantity);
        if (!image.isEmpty())
            product.setImage(image.getBytes());

        productRepository.save(product);
        return "redirect:/modifyProduct?success";
    }

    @GetMapping("/allProducts")
    public String allProducts(Model model) {
        List<ProductEntity> productsList = productService.getAllProducts();
        model.addAttribute("productsList", productsList);
        return "adminProducts";
    }

    @GetMapping("/product/{productId}")
    public String viewMore(@PathVariable Long productId, Model model) throws ProductNotExistsException {
        ProductEntity product = productService.getProductById(productId);
        model.addAttribute("product", product);
        model.addAttribute("imageDecoder", imageDecoder);
        return "product";
    }

    @GetMapping("/products")
    public String productsPage(Model model) {
        List<ProductEntity> productList = productService.getAllProducts();
        model.addAttribute("productList", productList);
        model.addAttribute("imageDecoder", imageDecoder);
        return "products";
    }
}
