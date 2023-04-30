package bg.softuni.mygymshop.service;

import bg.softuni.mygymshop.model.dtos.cart.ShoppingCartDTO;
import bg.softuni.mygymshop.model.entities.CartItemEntity;
import bg.softuni.mygymshop.model.entities.ProductEntity;
import bg.softuni.mygymshop.model.entities.UserEntity;
import bg.softuni.mygymshop.repository.CartItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ShoppingCartService cartService;

    @Test
    public void testAddItemToCartSuccess() {
        UserEntity user = new UserEntity();
        ProductEntity product = new ProductEntity();
        product.setQuantity(10);
        Integer quantity = 2;

        when(cartItemRepository.findByUserAndProduct(user, product)).thenReturn(null);
        when(cartItemRepository.save(any(CartItemEntity.class))).thenReturn(new CartItemEntity());
        Mockito.doNothing().when(productService).saveProduct(any(ProductEntity.class));

        cartService.addItemToCart(user, product, quantity);

        verify(cartItemRepository, times(1)).findByUserAndProduct(user, product);
        verify(cartItemRepository, times(1)).save(any(CartItemEntity.class));
        verify(productService, times(1)).saveProduct(any(ProductEntity.class));
    }

    @Test
    public void testAddItemToCartInsufficientQuantity() {
        UserEntity user = new UserEntity();
        ProductEntity product = new ProductEntity();
        product.setQuantity(1);
        Integer quantity = 2;

        when(cartItemRepository.findByUserAndProduct(user, product)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> cartService.addItemToCart(user, product, quantity));
    }

    @Test
    public void testAddItemToCartProductAlreadyInCart() {
        UserEntity user = new UserEntity();
        ProductEntity product = new ProductEntity();
        product.setQuantity(10);
        Integer quantity = 2;

        when(cartItemRepository.findByUserAndProduct(user, product)).thenReturn(new CartItemEntity());

        assertThrows(NullPointerException.class, () -> cartService.addItemToCart(user, product, quantity));
    }

    @Test
    public void testRemoveItemFromCartSuccess() {
        UserEntity user = new UserEntity();
        ProductEntity product = new ProductEntity();
        product.setQuantity(10);
        CartItemEntity cartItem = new CartItemEntity(user, product, 1);

        when(cartItemRepository.findByUserAndProduct(user, product)).thenReturn(cartItem);

        cartService.removeItemFromCart(user, product);

        verify(cartItemRepository, times(1)).findByUserAndProduct(user, product);
        verify(cartItemRepository, times(1)).delete(cartItem);
        verify(productService, times(1)).saveProduct(product);
    }

    @Test
    public void testRemoveItemFromCartNotFound() {
        UserEntity user = new UserEntity();
        ProductEntity product = new ProductEntity();

        when(cartItemRepository.findByUserAndProduct(user, product)).thenReturn(null);

        cartService.removeItemFromCart(user, product);

        verify(cartItemRepository, times(1)).findByUserAndProduct(user, product);
        verify(cartItemRepository, times(0)).delete(any(CartItemEntity.class));
        verify(productService, times(0)).saveProduct(any(ProductEntity.class));
    }

    @Test
    public void testGetShoppingCart() {
        // create a test user
        UserEntity user = new UserEntity();
        user.setId(1L);

        // create some test cart items
        List<CartItemEntity> cartItems = new ArrayList<>();
        ProductEntity product1 = new ProductEntity()
                .setProductId(1L)
                .setName("Product 1")
                .setPrice(BigDecimal.valueOf(10.0));
        cartItems.add(new CartItemEntity(user, product1, 2));
        ProductEntity product2 = new ProductEntity()
                .setProductId(2L)
                .setName("Product 2")
                .setPrice(BigDecimal.valueOf(10.0));
        cartItems.add(new CartItemEntity(user, product2, 3));

        // set up the mock to return the test cart items when findByUser is called
        when(cartItemRepository.findByUser(user)).thenReturn(cartItems);

        // call the method being tested
        ShoppingCartDTO shoppingCart = cartService.getShoppingCart(user);

        // verify that the method returned the expected ShoppingCartDTO object
        assertEquals(shoppingCart.getCartItems().size(), cartItems.size());
        assertEquals(shoppingCart.getCartItems().get(0).getProductId(), product1.getProductId());
        assertEquals(shoppingCart.getCartItems().get(0).getProductName(), product1.getName());
        assertEquals(shoppingCart.getCartItems().get(0).getProductPrice(), product1.getPrice());
        assertEquals(shoppingCart.getCartItems().get(0).getQuantity(), 2);
        assertEquals(shoppingCart.getCartItems().get(1).getProductId(), product2.getProductId());
        assertEquals(shoppingCart.getCartItems().get(1).getProductName(), product2.getName());
        assertEquals(shoppingCart.getCartItems().get(1).getProductPrice(), product2.getPrice());
        assertEquals(shoppingCart.getCartItems().get(1).getQuantity(), 3);
    }



    @Test
    public void testGetShoppingCartError() {
        UserEntity user = new UserEntity();

        when(cartItemRepository.findByUser(user)).thenReturn(Collections.emptyList());

        ShoppingCartDTO shoppingCartDTO = cartService.getShoppingCart(user);

        assertNotNull(shoppingCartDTO);
        assertEquals(0, shoppingCartDTO.getCartItems().size());
    }

}
