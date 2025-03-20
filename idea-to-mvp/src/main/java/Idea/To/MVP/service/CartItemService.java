package Idea.To.MVP.service;

import Idea.To.MVP.Exceptions.CartItemNotFoundException;
import Idea.To.MVP.Repository.CartItemRepository;
import Idea.To.MVP.Repository.CartRepository;
import Idea.To.MVP.models.Cart;
import Idea.To.MVP.models.CartItem;
import Idea.To.MVP.models.Product;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartService cartService;
    private final ProductService productService;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    //hämtar cart från databas, lägger till produkt efter produktId. Kontrollerar om id finns innan den lägger till.
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

    private CartItem getCartItem(UUID cartId, UUID productId) {
        Cart cart = cartService.getCartById(cartId);
        return cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException("This item has not been found"));
    }

    public void removeItemFromCart (UUID cartId, UUID productId) {
        Cart cart = cartService.getCartById(cartId);
        CartItem item = getCartItem(cartId, productId);
        cart.deleteItem(item);
        cartRepository.save(cart);
    }

    //uppdaterar cartItem, om amount är noll så tas det bort.
    public void updateAmount (UUID cartId, UUID productId, long amount) {
        Cart cart = cartService.getCartById(cartId);
        cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setAmount(amount);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                    if (item.getAmount() == 0) {
                        cart.getCartItems().remove(item);
                    }
                });
        BigDecimal totalPriceOfCart = cart.getCartItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(totalPriceOfCart);
        cartRepository.save(cart);
    }

}
