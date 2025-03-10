package Idea.To.MVP.controllers;

import Idea.To.MVP.DTO.CartDto;
import Idea.To.MVP.DTO.UserDto;
import Idea.To.MVP.Exceptions.CartNotFoundException;
import Idea.To.MVP.Response.ApiResponse;
import Idea.To.MVP.models.Cart;
import Idea.To.MVP.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

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

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getCartById(@PathVariable("id") String uuid) {
        try {
            UUID id = UUID.fromString(uuid);
            CartDto cartDto = cartService.convertToDto(cartService.getCartById(id));
            return ResponseEntity.ok(new ApiResponse("Cart found successfully", true, cartDto));
        } catch (CartNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), false, null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse("Id is to large or too small", false, null));
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllCarts() {
        try {
            List<Cart> carts = cartService.getAllCarts();
            List <CartDto> cartDto = carts.stream()
                    .map(cartService :: convertToDto)
                    .toList();
            return ResponseEntity.ok(new ApiResponse("Carts found successfully", true, cartDto));
        } catch (CartNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), false, null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable("id") String uuid) {
        try {
            UUID id = UUID.fromString(uuid);
            cartService.clearCart(id);
            return ResponseEntity.ok(new ApiResponse("Cart cleared successfully", true, null));
        } catch (CartNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), false, null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse("Id is to large or too small", false, null));
        }
    }

    @GetMapping("/get/total/{id}")
    public ResponseEntity<ApiResponse> getTotalPrice(@PathVariable("id") String uuid) {
        try {
            UUID id = UUID.fromString(uuid);
            BigDecimal totalPrice = cartService.getTotalPrice(id);
            return ResponseEntity.ok(new ApiResponse("Total price found successfully", true, totalPrice));
        } catch (CartNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), false, null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse("Id is to large or too small", false, null));
        }
    }
}
