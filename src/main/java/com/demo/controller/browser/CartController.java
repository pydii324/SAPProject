package com.demo.controller.browser;

import com.demo.config.UserDetailsService;
import com.demo.exception.user.UserNotExistsException;
import com.demo.model.product.ProductEntity;
import com.demo.model.sale.Sale;
import com.demo.model.sale.SaleStatus;
import com.demo.repository.SaleRepository;
import com.demo.service.layer.CartService;
import com.demo.service.logic.BasicSum;
import com.demo.service.logic.SaleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class CartController {

    private final UserDetailsService userDetailsService;
    private final CartService cartService;
    private final SaleRepository saleRepository;
    private SaleService saleService;
    private final BasicSum basicSum;

    @GetMapping("/cart")
    public String cart(Model model) throws UserNotExistsException {

        String username = userDetailsService.getUsername();
        cartService.createCartByUsername(username);

        List<Sale> salesList = saleService.loadAllSales();
        HashMap<ProductEntity, Integer> productsList = cartService.getProductsInCart(username);

        // Float is the current price of the products
        HashMap<ProductEntity, Float> productsPrice = new HashMap<>();

        for (Map.Entry<ProductEntity, Integer> entry : productsList.entrySet()) {
            ProductEntity product = entry.getKey();
            Integer quantity = entry.getValue();
            List<Sale> applicableSales = saleService.getSalesForProduct(product);

            float basePrice = product.getPrice() * quantity;
            float discount = 0.0f;
            for (Sale sale : applicableSales) {
                if (sale.getStatus().equals(SaleStatus.ACTIVE)) {
                    discount += sale.getDiscountPercentage();
                }
            }
            float totalPrice = basePrice * (1f - discount);
            if (totalPrice < 0) {
                productsPrice.put(product, 0f);
            } else {
                productsPrice.put(product, totalPrice);
            }
        }
        model.addAttribute("productsPrice", productsPrice);
        model.addAttribute("productsList", productsList);
        model.addAttribute("BasicSum", basicSum);

        return "cart";
    }

        @PostMapping("/addToCart/{productId}")
        public String addToCart (@PathVariable Long productId) throws UserNotExistsException {
            String username = userDetailsService.getUsername();
            cartService.createCartByUsername(username);
            cartService.addProductToCart(username, productId);

            return "redirect:/product/{productId}?productAdded";
        }
        @PostMapping("/removeFromCart/{productId}")
        public String removeFromCart (@PathVariable Long productId) throws UserNotExistsException {
            String username = userDetailsService.getUsername();
            cartService.removeProductFromCart(username, productId);

            return "redirect:/cart";
        }
    }
