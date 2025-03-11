package Idea.To.MVP.Config;

import Idea.To.MVP.DTO.CartDto;
import Idea.To.MVP.DTO.CartItemDto;
import Idea.To.MVP.models.Cart;
import Idea.To.MVP.models.CartItem;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(Cart.class, CartDto.class)
                .addMapping(Cart::getId, CartDto::setCartId);

        modelMapper.createTypeMap(CartItem.class, CartItemDto.class)
                .addMapping(CartItem::getId, CartItemDto::setCartItemId)
                .addMapping(CartItem::getProduct, CartItemDto::setProduct);

        return modelMapper;
    }
}
