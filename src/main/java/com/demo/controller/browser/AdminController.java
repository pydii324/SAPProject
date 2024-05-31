package com.demo.controller.browser;

import com.demo.dto.ProductDTO;
import com.demo.dto.SaleDTO;
import com.demo.exception.order.OrderNotExistsException;
import com.demo.exception.product.ProductNotExistsException;
import com.demo.exception.user.UserNotExistsException;
import com.demo.model.order.OrderEntity;
import com.demo.model.order.ProductsInOrder;
import com.demo.model.order.Status;
import com.demo.model.product.ProductEntity;
import com.demo.model.sale.SaleStatus;
import com.demo.model.user.UserEntity;
import com.demo.repository.OrderRepository;
import com.demo.repository.ProductRepository;
import com.demo.service.layer.OrderService;
import com.demo.service.layer.ProductService;
import com.demo.service.layer.UserService;
import com.demo.service.logic.BasicSum;
import com.demo.service.logic.SaleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private ProductService productService;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private OrderService orderService;
    private BasicSum basicSum;
    private SaleService saleService;

    @GetMapping("")
    public String admin() {
        return "admin";
    }

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
            return "redirect:/admin/modifyProduct?fail";

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
        return "redirect:/admin/modifyProduct?success";
    }

    @GetMapping("/deleteProduct")
    public String deleteProduct() {
        return "deleteProduct";
    }
    @PostMapping("/deleteProduct")
    public String deleteProduct(@RequestParam Long productId) {

        Optional<ProductEntity> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isPresent()) {
            productRepository.delete(optionalProduct.get());
            return "redirect:/admin/deleteProduct?success";
        } else {
            return "redirect:/admin/deleteProduct?fail";
        }
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<UserEntity> usersList = userService.getAllUsers();
        model.addAttribute("usersList", usersList);
        return "users";
    }

    @GetMapping("/create_admin")
    public String createAdmin() {
        return "create_admin";
    }
    @PostMapping("/create_admin")
    public String createAdmin(@RequestParam String username) throws UserNotExistsException {
        if (userService.grantAdminPrivileges(username))
            return "redirect:/admin/create_admin?created";
        return "redirect:/admin/create_admin?notCreated";
    }

    @GetMapping("/remove_admin")
    public String removeAdmin() {
        return "remove_admin";
    }
    @PostMapping("/remove_admin")
    public String removeAdmin(@RequestParam String username) {
        if (userService.takeAdminPrivileges(username))
            return "redirect:/admin/remove_admin?success";
        return "redirect:/admin/remove_admin?fail";
    }

    @GetMapping("/all_orders")
    public String allOrders(Model model) {
        List<OrderEntity> ordersList = orderRepository.findAll();
        model.addAttribute("ordersList", ordersList);
        return "all_orders";
    }
    @GetMapping("/order/{orderId}")
    public String order(@PathVariable Long orderId, Model model) throws OrderNotExistsException {

        OrderEntity order = orderService.loadOrderById(orderId);

        List<ProductsInOrder> productsInOrderList = order.getProductsInOrderList();

        model.addAttribute("orderId", orderId);
        model.addAttribute("productsInOrderList", productsInOrderList);
        model.addAttribute("BasicSum", basicSum);

        return "order";
    }

    @GetMapping("/modifyOrder")
    public String modifyOrder() {
        return "modifyOrder";
    }
    @PostMapping("/modifyOrder")
    public String modifyOrder(@RequestParam Long id,
                              @RequestParam String status) {

        if (id == null) {
            return "redirect:/admin/modifyOrder?fail";
        }

        Optional<OrderEntity> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isEmpty()) {
            return "redirect:/admin/modifyOrder?fail";
        }
        OrderEntity order = optionalOrder.get();

        switch (status) {
            case "pending" -> order.setStatus(Status.PENDING);
            case "processing" -> order.setStatus(Status.PROCESSING);
            case "finished" -> order.setStatus(Status.SHIPPED);
        }

        orderRepository.save(order);
        return "redirect:/admin/modifyOrder?success";
    }

    @GetMapping("/start_sale")
    public String startSale() {
        return "start_sale";
    }
    @PostMapping("/start_sale")
    public String startSale(@RequestParam int endDay,
                            @RequestParam int endMonth,
                            @RequestParam int endYear,
                            @RequestParam float discountPercentage) {

        LocalDateTime endDateTime = LocalDateTime.of(endYear, endMonth, endDay, 0, 0, 0);

        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setEndDate(endDateTime);
        saleDTO.setDiscountPercentage(discountPercentage);

        saleService.startSale(saleDTO);

        return "redirect:/admin/start_sale?success";
    }
    @GetMapping("/end_sale")
    public String endSale() {
        return "end_sale";
    }
    @PostMapping("/end_sale")
    public String endSale(@RequestParam Long saleId) {
        saleService.endSale(saleId);
        if (saleService.endSale(saleId))
            return "redirect:/admin/end_sale?success";
        return "redirect:/admin/end_sale?fail";
    }

    @GetMapping("/add_product_in_sale")
    public String addProductsInSale() {
        return "add_product_sale";
    }
    @PostMapping("add_product_in_sale")
    public String addProductsInSale(@RequestParam Long saleId,
                                    @RequestParam Long productId) {

        if(saleService.addProductToSale(saleId, productId))
            return "redirect:/admin/add_product_in_sale?success";
        return "redirect:/admin/add_product_in_sale?fail";
    }

    @GetMapping("/remove_product_from_sale")
    public String removeProductsFromSale() {
        return "remove_product_sale";
    }
    @PostMapping("remove_product_from_sale")
    public String removeProductsFromSale(@RequestParam Long saleId,
                                         @RequestParam Long productId) {

        if(saleService.removeProductFromSale(saleId, productId))
            return "redirect:/admin/remove_product_from_sale?success";
        return "redirect:/admin/remove_product_from_sale?fail";
    }
}
