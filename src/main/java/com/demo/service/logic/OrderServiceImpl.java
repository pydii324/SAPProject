package com.demo.service.logic;

import com.demo.exception.order.CartIsEmptyException;
import com.demo.exception.order.OrderNotExistsException;
import com.demo.exception.product.ProductQuantityUnavailableException;
import com.demo.exception.user.UserNotExistsException;
import com.demo.model.cart.CartEntity;
import com.demo.model.order.OrderEntity;
import com.demo.model.order.ProductsInOrder;
import com.demo.model.order.Status;
import com.demo.model.product.ProductEntity;
import com.demo.model.sale.Sale;
import com.demo.model.sale.SaleStatus;
import com.demo.model.user.UserEntity;
import com.demo.repository.OrderRepository;
import com.demo.repository.UserRepository;
import com.demo.service.layer.CartService;
import com.demo.service.layer.OrderService;
import com.demo.service.layer.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartService cartService;
    private final SaleService saleService;
    private OrderRepository orderRepository;
    private UserService userService;
    private UserRepository userRepository;
    private PriceCalculator priceCalculator;

    // Create
    public Long createOrder(String username) throws CartIsEmptyException, ProductQuantityUnavailableException, UserNotExistsException {
        UserEntity user = userRepository.findByUsername(username);

        // proverka za prazna kolichka
        HashMap<ProductEntity, Integer> cartProducts = cartService.getProductsInCart(username);
        if (cartProducts.isEmpty()) {
            return null;
        }

        // proverka za nalichnost na produkta v sklada
        for (Map.Entry<ProductEntity, Integer> entry : cartProducts.entrySet()) {
            if (entry.getKey().getAvailableQuantity() < entry.getValue()) {
                return null;
            }
        }

        // mahane na produkti ot kolichkata
        cartService.deleteCartById(username);

        OrderEntity order = new OrderEntity();
        order.setOrderDate(LocalDateTime.now());
        order.setUser(user);
        order.setStatus(Status.PENDING);

        List<ProductsInOrder> productsInOrderList = new ArrayList<>();
        for (Map.Entry<ProductEntity, Integer> entry : cartProducts.entrySet()) {
            ProductEntity product = entry.getKey();
            Integer quantity = entry.getValue();
            List<Sale> aplicableSales = saleService.getSalesForProduct(product);

            float basePrice = product.getPrice() * quantity;
            float discount = 0.0f;
            for (Sale sale : aplicableSales) {
                if (sale.getStatus().equals(SaleStatus.ACTIVE)) {
                    discount += sale.getDiscountPercentage();
                }
            }
            float totalPrice = basePrice * (1f - discount);
            if (totalPrice < 0) {
                totalPrice = 0;
            } else {
                totalPrice = basePrice * (1f - discount);
            }

            ProductsInOrder productsInOrder = new ProductsInOrder();

            productsInOrder.setProduct(product);
            productsInOrder.setQuantity(quantity);
            productsInOrder.setProductPrice(totalPrice);
            productsInOrder.setOrder(order);
            productsInOrderList.add(productsInOrder);
        }

        order.setProductsInOrderList(productsInOrderList);

        orderRepository.save(order);
        return order.getId();
    }

    // Read
    public List<OrderEntity> loadAllOrders() {
        return orderRepository.findAll();
    }
    public OrderEntity loadOrderById(Long id) throws OrderNotExistsException {
        Optional<OrderEntity> orderEntity = orderRepository.findById(id);
        if (orderEntity.isEmpty())
            throw new OrderNotExistsException("Order with id: "+id+" does not exist!");
        return orderEntity.get();
    }
    public List<OrderEntity> loadOrdersByUserId(Long userId) throws UserNotExistsException {
        UserEntity user = userService.getUserById(userId);
        return orderRepository.findOrderEntityByUser(user);
    }

    // Update
    /*public boolean modifyOrderById(Long id) {

        Optional<OrderEntity> orderEntity = orderRepository.findById(id);
        if (orderEntity.isEmpty())
            return false;


    }*/

    // Delete
    public void deleteOrderById(Long id) throws OrderNotExistsException {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isEmpty())
            throw new OrderNotExistsException("Order with id: "+id+" does not exist");
        orderRepository.delete(optionalOrder.get());
    }
}
