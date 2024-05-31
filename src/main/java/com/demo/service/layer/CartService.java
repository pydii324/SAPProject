package com.demo.service.layer;

import com.demo.exception.cart.CartNotExistsException;
import com.demo.exception.user.UserNotExistsException;
import com.demo.model.cart.CartEntity;
import com.demo.model.product.ProductEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public interface CartService {

    // Create
    boolean createCartByUsername(String username) throws UserNotExistsException;

    // Read
    List<CartEntity> loadAllCarts();
    CartEntity loadCartById(Long id)
            throws CartNotExistsException;
    CartEntity loadCartByUserId(Long userId)
            throws UserNotExistsException;
    CartEntity loadCartByUsername(String username)
            throws UserNotExistsException;

    // Update
    void addProductToCart(String username, Long productId);
    void removeProductFromCart(String username, Long productId);

    // Delete
    void deleteCartById(String username);

    HashMap<ProductEntity, Integer> getProductsInCart(String username) throws UserNotExistsException;
}
