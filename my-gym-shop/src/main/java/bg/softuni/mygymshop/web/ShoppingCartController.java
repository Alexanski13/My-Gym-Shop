package bg.softuni.mygymshop.web;

import bg.softuni.mygymshop.model.dtos.cart.ShoppingCartDTO;
import bg.softuni.mygymshop.model.entities.ProductEntity;
import bg.softuni.mygymshop.model.entities.UserEntity;
import bg.softuni.mygymshop.service.ProductService;
import bg.softuni.mygymshop.service.ShoppingCartService;
import bg.softuni.mygymshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    private final ProductService productService;

    private final UserService userService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, ProductService productService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("")
    public String viewShoppingCart(Model model, Principal principal) {
        UserEntity user = getUserFromPrincipal(principal);
        ShoppingCartDTO shoppingCartDTO = shoppingCartService.getShoppingCart(user);
        model.addAttribute("shoppingCart", shoppingCartDTO);
        return "shopping-cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam Integer quantity, Principal principal) {
        UserEntity user = getUserFromPrincipal(principal);
        ProductEntity product = productService.getProductById(productId);
        shoppingCartService.addItemToCart(user, product, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long productId, Principal principal) {
        UserEntity user = getUserFromPrincipal(principal);
        ProductEntity product = productService.getProductById(productId);
        shoppingCartService.removeItemFromCart(user, product);
        return "redirect:/cart";
    }

    private UserEntity getUserFromPrincipal(Principal principal) {
        String username = principal.getName();
        return userService.getUserByUsername(username);
    }
}
