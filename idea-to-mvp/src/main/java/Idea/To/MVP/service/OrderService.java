package Idea.To.MVP.service;

import Idea.To.MVP.DTO.OrdersDto;
import Idea.To.MVP.Enum.OrderStatus;
import Idea.To.MVP.Exceptions.OrderNotFoundException;
import Idea.To.MVP.Repository.OrdersRepository;
import Idea.To.MVP.Repository.ProductRepository;
import Idea.To.MVP.models.Cart;
import Idea.To.MVP.models.OrderItem;
import Idea.To.MVP.models.Orders;
import Idea.To.MVP.models.Product;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static Idea.To.MVP.Enum.OrderStatus.PAYMENT_PENDING;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final CartService cartService;
    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;


    public Orders placeNewOrder (UUID userId) {
        Cart cart = cartService.getCartById(userId);
        Orders order = createNewOrder(cart);
        List<OrderItem> orderItemsList = createOrderItems(order, cart);
        order.setOrderItems(new ArrayList<>(orderItemsList));
        order.setOrderStatus(PAYMENT_PENDING); // Set initial status as payment pending

        //Lägg till detta som en input
        //order.setStripeSessionId(stripeSessionId);

        order.setTotalAmount(totalPrice(orderItemsList));
        Orders savedOrder = ordersRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    public Orders confirmStripePayment(String stripeSessionId) {
        Orders order  = ordersRepository.findByStripeSessionId(stripeSessionId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found for sessions " + stripeSessionId));
        order.setOrderStatus(OrderStatus.PAYMENT_COMPLETED);
        Orders savedOrder = ordersRepository.save(order);
        cartService.clearCart(order.getUser().getCart().getId());
        return savedOrder;
    }

    private BigDecimal totalPrice(List<OrderItem> orderItemsList) {
        return orderItemsList.stream()
                .map(orderItem -> orderItem.getPrice()
                        .multiply(new BigDecimal(orderItem.getAmount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<OrderItem> createOrderItems(Orders order, Cart cart) {
        return cart.getCartItems().stream()
                .map(item -> {
                    Product product = item.getProduct();
                    product.setInStock(true);
                    productRepository.save(product);
                    return new OrderItem(order, product, item.getAmount(), item.getUnitPrice());
                }).toList();
    }

    private Orders createNewOrder(Cart cart) {
        Orders order = new Orders();
        order.setUser(cart.getUser());
        order.setOrderStatus(PAYMENT_PENDING);
        order.setOrderDate(new Date());
        return order;
    }

    public OrdersDto getOrderById(UUID orderId) {
        return ordersRepository.findById(orderId)
                .map(this :: convertToDto)
                .orElseThrow(()  -> new OrderNotFoundException("No order with this ID has been found"));
    }

    public List<OrdersDto> getAUsersOrders (UUID userId) {
        List<Orders> orders = ordersRepository.findByUserId(userId);
        return orders.stream().map(this :: convertToDto).toList();
    }

    public OrdersDto convertToDto(Orders order) {
        return modelMapper.map(order, OrdersDto.class);
    }
}
