package com.demo.repository;

import com.demo.model.order.OrderEntity;
import com.demo.model.user.UserEntity;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findOrderEntityByUser(UserEntity user);
}
