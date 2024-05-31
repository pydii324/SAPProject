package com.demo.controller.browser;

import com.demo.config.UserDetailsService;
import com.demo.exception.order.CartIsEmptyException;
import com.demo.exception.order.OrderNotExistsException;
import com.demo.exception.product.ProductQuantityUnavailableException;
import com.demo.exception.user.UserNotExistsException;
import com.demo.model.cart.CartEntity;
import com.demo.model.order.OrderEntity;
import com.demo.model.order.ProductsInOrder;
import com.demo.model.order.Status;
import com.demo.model.product.ProductEntity;
import com.demo.repository.UserRepository;
import com.demo.service.layer.OrderService;
import com.demo.service.logic.BasicSum;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@AllArgsConstructor
public class OrderController {

    private final UserDetailsService userDetailsService;
    private final OrderService orderService;
    private final UserRepository userRepository;
    private BasicSum basicSum;

    @GetMapping("/orders")
    public String orders(Model model) throws UserNotExistsException {
        String username = userDetailsService.getUsername();
        Long userId = userRepository.findByUsername(username).getId();
        List<OrderEntity> ordersList = orderService.loadOrdersByUserId(userId);
        model.addAttribute("ordersList", ordersList);
        return "orders";
    }

    @GetMapping("/order/{orderId}")
    public String order(Model model, @PathVariable Long orderId) throws UserNotExistsException, OrderNotExistsException {
        String username = userDetailsService.getUsername();

        OrderEntity order = orderService.loadOrderById(orderId);
        if (!order.getUser().getUsername().equals(username))
            return "not_your_order";

        List<ProductsInOrder> productsInOrderList = order.getProductsInOrderList();
        model.addAttribute("productsInOrderList", productsInOrderList);

        model.addAttribute("orderId", orderId);
        model.addAttribute("BasicSum", basicSum);

        return "order";
    }

    @PostMapping("/createOrder")
    public String createOrder() throws CartIsEmptyException, ProductQuantityUnavailableException, UserNotExistsException, OrderNotExistsException {
        String username  = userDetailsService.getUsername();
        Long orderId = orderService.createOrder(username);

        if (orderId == null)
            return "redirect:/cart?wrong";
        OrderEntity order = orderService.loadOrderById(orderId);
        return "redirect:/order/" + order.getId();
    }
}
