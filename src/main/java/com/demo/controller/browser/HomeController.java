package com.demo.controller.browser;

import com.demo.model.product.ProductEntity;
import com.demo.service.layer.ProductService;
import com.demo.service.logic.ImageDecoder;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class HomeController {

    private ProductService productService;
    private ImageDecoder imageDecoder;


    @GetMapping("/home")
    public String home(Model model) {
        List<ProductEntity> productList = productService.getAllProducts();
        model.addAttribute("productList", productList);
        model.addAttribute("imageDecoder", imageDecoder);

        return "home";
    }
}
