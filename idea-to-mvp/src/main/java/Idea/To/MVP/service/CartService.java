package Idea.To.MVP.service;


import Idea.To.MVP.Exceptions.CartNotFoundException;
import Idea.To.MVP.Repository.CartItemRepository;
import Idea.To.MVP.Repository.CartRepository;
import Idea.To.MVP.models.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
   private final CartRepository cartRepository;
   private final CartItemRepository cartItemRepository;

   public Cart newCart() {
      Cart cart = new Cart();
      return cartRepository.save(cart);
   }
}
