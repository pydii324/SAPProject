package com.demo.service.logic;

import com.demo.exception.cart.CartNotExistsException;
import com.demo.exception.user.UserNotExistsException;
import com.demo.model.cart.CartEntity;
import com.demo.model.product.ProductEntity;
import com.demo.model.user.UserEntity;
import com.demo.repository.CartRepository;
import com.demo.repository.ProductRepository;
import com.demo.repository.UserRepository;
import com.demo.service.layer.CartService;
import com.demo.service.layer.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;
    private CartRepository cartRepository;
    private UserRepository userRepository;
    private UserService userService;

    // Create
    public boolean createCartByUsername(String username) throws UserNotExistsException {
        UserEntity user = userService.getUserByUsername(username);
        if (user.getCart() == null) {
            CartEntity cart = new CartEntity();
            cart.setUser(user);

            cartRepository.save(cart);
            return true;
        }
        return false;
    }

    // Read
    public List<CartEntity> loadAllCarts() {
        return cartRepository.findAll();
    }
    public CartEntity loadCartById(Long id) throws CartNotExistsException {
        Optional<CartEntity> optionalCart = cartRepository.findById(id);
        if (optionalCart.isEmpty())
            throw new CartNotExistsException("Cart with id "+id+" does not exist!");
        return optionalCart.get();
    }
    public CartEntity loadCartByUserId(Long userId) throws UserNotExistsException {
        UserEntity user = userService.getUserById(userId);
        return cartRepository.findCartEntityByUser(user);
    }
    public CartEntity loadCartByUsername(String username) throws UserNotExistsException {
        UserEntity user = userRepository.findByUsername(username);
        return cartRepository.findCartEntityByUser(user);
    }

    // Update
    public void addProductToCart(String username, Long productId) {
        UserEntity user = userRepository.findByUsername(username);
        CartEntity cart = cartRepository.findCartEntityByUser(user);
        ProductEntity productToAdd = productRepository.findById(productId).get();


        List<ProductEntity> productList = cart.getProducts();
        productList.add(productToAdd);
        cart.setProducts(productList);

        cartRepository.save(cart);
    }
    public void removeProductFromCart(String username, Long productId) {
        UserEntity user = userRepository.findByUsername(username);
        CartEntity cart = cartRepository.findCartEntityByUser(user);
        ProductEntity productToRemove = productRepository.findById(productId).get();

        List<ProductEntity> productList = cart.getProducts();
        productList.remove(productToRemove);
        cart.setProducts(productList);

        cartRepository.save(cart);
    }

    // Remove
    public void deleteCartById(String username) {
        UserEntity user = userRepository.findByUsername(username);
        CartEntity cart = cartRepository.findCartEntityByUser(user);
        cartRepository.delete(cart);
    }

    public HashMap<ProductEntity, Integer> getProductsInCart(String username) throws UserNotExistsException {
        CartEntity cart = loadCartByUsername(username);

        HashMap<ProductEntity, Integer> productsMap = new HashMap<>();

        for (ProductEntity productEntity : cart.getProducts()) {
            int quantity = productsMap.getOrDefault(productEntity, 0) + 1;
            productsMap.put(productEntity, quantity);
        }

        return productsMap;
    }
}
