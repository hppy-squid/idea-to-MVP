package Idea.To.MVP.Exceptions;

public class CartItemNotFoundException extends RuntimeException{
    public CartItemNotFoundException(String message) {
        super(message);
    }

}
