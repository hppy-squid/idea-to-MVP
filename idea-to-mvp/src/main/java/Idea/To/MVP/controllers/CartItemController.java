package Idea.To.MVP.controllers;

import Idea.To.MVP.Exceptions.CartNotFoundException;
import Idea.To.MVP.Response.ApiResponse;
import Idea.To.MVP.models.Cart;
import Idea.To.MVP.models.User;
import Idea.To.MVP.service.CartItemService;
import Idea.To.MVP.service.CartService;
import Idea.To.MVP.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cartItem")
public class CartItemController {
    private final CartItemService cartItemService;
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(@RequestParam UUID productId, @RequestParam(required = false) UUID cartId, @RequestParam Long amount) {
        try {
            //Detta måste ändras sen då vi kommer att ha autentiserade användare och gästanvändare
            if (cartId == null) {
                Cart cart = cartService.newCart();
                cartId = cart.getId();
            }
            cartItemService.addProductToCart(cartId,productId,amount);
            return ResponseEntity.ok(new ApiResponse("Add item is successful", true, null));
        } catch (CartNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), false, null));
        }

    }
}
