package com.demo.demo;

import com.demo.model.order.OrderEntity;
import com.demo.repository.OrderRepository;
import com.demo.repository.UserRepository;
import com.demo.service.layer.OrderService;
import com.demo.service.layer.UserService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    // Mocking
    @Mock
    private OrderRepository orderRepository;

    public List<OrderEntity> loadAllOrders() {
        return orderRepository.findAll();
    }

    // given when then
    @Test
    public void testLoadAllOrders() {
        List<OrderEntity> orderList = new ArrayList<>();
        when(orderRepository.findAll()).thenReturn(orderList);
        // 1. ochakvano, 2. test unit
        Assertions.assertEquals(null, orderRepository.findAll());
    }
}
