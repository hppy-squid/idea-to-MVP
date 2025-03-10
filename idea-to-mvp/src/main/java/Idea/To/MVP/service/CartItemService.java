package Idea.To.MVP.service;

import Idea.To.MVP.Repository.CartItemRepository;
import Idea.To.MVP.Repository.CartRepository;
import Idea.To.MVP.models.Cart;
import Idea.To.MVP.models.CartItem;
import Idea.To.MVP.models.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartService cartService;
    private final ProductService productService;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    public void addProductToCart (UUID cartId, UUID productId, Long amount) {
        Cart cart = cartService.getCartById(cartId);
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        CartItem cartItem = cart.getCartItems().stream().
                filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());
        try {
            if (cartItem.getId() != null) {
                cartItem.setAmount(cartItem.getAmount() + amount);
            } else {
                cartItem.setCart(cart);
                cartItem.setProduct(product);
                cartItem.setAmount(amount);
                cartItem.setUnitPrice(product.getPrice());
            }

            cartItem.setTotalPrice();
            cart.addProductToCart(cartItem);
            cartItemRepository.save(cartItem);
            cartRepository.save(cart);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add product to cart: " + e.getMessage());
        }

    }

}
