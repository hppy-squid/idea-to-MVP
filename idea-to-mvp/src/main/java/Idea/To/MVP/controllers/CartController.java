package Idea.To.MVP.controllers;

import Idea.To.MVP.models.Cart;
import Idea.To.MVP.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartService cartService;


    //Detta är bara för att testa att det fungerar. Cart kommer att skapas egentligen när vi skapar ett cart item
    //I CartItemController
    @PostMapping("/new")
    public Cart newCartTest() {
        return cartService.newCart();
    }
}
