package com.demo.repository;

import com.demo.model.cart.CartEntity;
import com.demo.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    CartEntity findCartEntityByUser(UserEntity user);
}
