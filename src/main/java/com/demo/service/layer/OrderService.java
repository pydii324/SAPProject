package com.demo.service.layer;

import com.demo.exception.order.CartIsEmptyException;
import com.demo.exception.order.OrderNotExistsException;
import com.demo.exception.product.ProductQuantityUnavailableException;
import com.demo.exception.user.UserNotExistsException;
import com.demo.model.cart.CartEntity;
import com.demo.model.order.OrderEntity;
import com.demo.model.product.ProductEntity;
import com.demo.model.user.UserEntity;
import org.hibernate.query.Order;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public interface OrderService {

    // Create
    Long createOrder(String username)
            throws CartIsEmptyException, ProductQuantityUnavailableException, UserNotExistsException;

    // Read
    List<OrderEntity> loadAllOrders();
    OrderEntity loadOrderById(Long cartId)
            throws OrderNotExistsException;
    List<OrderEntity> loadOrdersByUserId(Long userId)
            throws UserNotExistsException;

    /*boolean modifyOrderById(Long id);*/

    // Delete
    void deleteOrderById(Long id)
            throws OrderNotExistsException;

}
