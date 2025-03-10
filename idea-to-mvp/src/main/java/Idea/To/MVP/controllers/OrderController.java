package Idea.To.MVP.controllers;

import Idea.To.MVP.DTO.OrdersDto;
import Idea.To.MVP.Exceptions.OrderNotFoundException;
import Idea.To.MVP.Response.ApiResponse;
import Idea.To.MVP.models.Orders;
import Idea.To.MVP.service.OrderService;
import Idea.To.MVP.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @PostMapping("/newOrder")
    public ResponseEntity<ApiResponse> createNewOrder(@RequestParam UUID userId) {
        try {
            Orders order = orderService.placeNewOrder(userId);
            OrdersDto ordersDto = orderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("Order has been created successfully", true, ordersDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), true, null));
        }
    }

    @GetMapping("/get/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById (@PathVariable UUID orderId) {
        try {
            OrdersDto orderDto = orderService.getOrderById(orderId);
            return ResponseEntity.ok(new ApiResponse("Order has been found by Id successfully", true, orderDto));
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), true, null));
        }
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<ApiResponse> getOrderByUser (@PathVariable UUID userId) {
        try {
            List<OrdersDto> ordersDto = orderService.getAUsersOrders(userId);
            return ResponseEntity.ok(new ApiResponse("All orders for the user "
                    + userService.getUserById(userId).getUserName()
                    + " has been retrieved",
                    true, ordersDto));
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), false, null));
        }
    }


}
