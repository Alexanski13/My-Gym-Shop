package bg.softuni.mygymshop.service;

import bg.softuni.mygymshop.model.dtos.cart.CartItemDTO;
import bg.softuni.mygymshop.model.dtos.cart.ShoppingCartDTO;
import bg.softuni.mygymshop.model.entities.CartItemEntity;
import bg.softuni.mygymshop.model.entities.ProductEntity;
import bg.softuni.mygymshop.model.entities.UserEntity;
import bg.softuni.mygymshop.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartService {
    private final CartItemRepository cartItemRepository;

    private final ProductService productService;

    @Autowired
    public ShoppingCartService(CartItemRepository cartItemRepository, ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
    }

    public void addItemToCart(UserEntity user, ProductEntity product, Integer quantity) {
        CartItemEntity cartItem = cartItemRepository.findByUserAndProduct(user, product);

        if (cartItem == null) {
            cartItem = new CartItemEntity(user, product, quantity);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        cartItemRepository.save(cartItem);

        int remainingQuantity = product.getQuantity() - quantity;
        if (remainingQuantity >= 0) {
            product.setQuantity(remainingQuantity);
            productService.saveProduct(product);
        } else {
            throw new IllegalArgumentException("Insufficient product quantity");
        }
    }

    public void removeItemFromCart(UserEntity user, ProductEntity product) {
        CartItemEntity cartItem = cartItemRepository.findByUserAndProduct(user, product);

        if (cartItem != null) {
            int quantity = cartItem.getQuantity();
            cartItemRepository.delete(cartItem);
            product.setQuantity(product.getQuantity() + quantity);
            productService.saveProduct(product);
        }
    }

    public ShoppingCartDTO getShoppingCart(UserEntity user) {
        List<CartItemEntity> cartItems = cartItemRepository.findByUser(user);
        List<CartItemDTO> cartItemDTOS = new ArrayList<>();

        for (CartItemEntity cartItem : cartItems) {
            ProductEntity product = cartItem.getProduct();
            CartItemDTO cartItemDto = new CartItemDTO(product.getProductId(), product.getName(), product.getPrice(), cartItem.getQuantity());
            cartItemDTOS.add(cartItemDto);
        }

        return new ShoppingCartDTO(cartItemDTOS);
    }
}