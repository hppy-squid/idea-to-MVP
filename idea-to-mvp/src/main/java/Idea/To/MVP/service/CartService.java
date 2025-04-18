package Idea.To.MVP.service;


import Idea.To.MVP.DTO.CartDto;
import Idea.To.MVP.Exceptions.CartNotFoundException;
import Idea.To.MVP.Repository.CartItemRepository;
import Idea.To.MVP.Repository.CartRepository;
import Idea.To.MVP.models.Cart;
import Idea.To.MVP.models.User;
import lombok.RequiredArgsConstructor;

import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
   private final CartRepository cartRepository;
   private final CartItemRepository cartItemRepository;
   private final ModelMapper modelMapper;
    private final UserService userService;

    // Skapar en new cart
    public Cart newCart(UUID userId) {
        User user = userService.getUserById(userId);
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCartItems(new ArrayList<>());
        return cartRepository.save(cart);
   }

   // Hittar en kundvagn via ett id
    @Transactional
    public Cart getCartById(UUID id) {
    return cartRepository.findCartWithItemsById(id)
        .orElseThrow(() -> new CartNotFoundException("Cart with id: " + id + " not found"));
}

    public List<Cart> getAllCarts() {
       List<Cart> carts = cartRepository.findAll();

       if(carts.isEmpty()) {
          throw new CartNotFoundException("No carts found");
       }
       return carts;
    }

    // Tömmer en kundvagn
    @Transactional
    public void clearCart(UUID id) {
        Cart cart = getCartById(id);
        cartItemRepository.deleteAllByCartId(id);

        User user = cart.getUser();
        if (user != null) {
            user.setCart(null);
        }
        cart.clearCart();
        cartRepository.delete(cart);
    }

    // Hämtar totalpriset av en kundvagn
    public BigDecimal getTotalPrice(UUID id) {
        Cart cart = getCartById(id);
        return cart.getTotalPrice();
    }

    // Konverterar en kundvagn till en CartDTO
    @Transactional
    public CartDto convertToDto(Cart cart) {
        Hibernate.initialize(cart.getCartItems());
        return modelMapper.map(cart, CartDto.class);
    }

}
