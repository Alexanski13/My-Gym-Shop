package bg.softuni.mygymshop.model.dtos.cart;

import java.util.List;

public class ShoppingCartDTO {
    private List<CartItemDTO> cartItems;

    public ShoppingCartDTO() {
    }

    public ShoppingCartDTO(List<CartItemDTO> cartItems) {
        this.cartItems = cartItems;
    }

    public List<CartItemDTO> getCartItems() {
        return cartItems;
    }

    public ShoppingCartDTO setCartItems(List<CartItemDTO> cartItems) {
        this.cartItems = cartItems;
        return this;
    }
}
