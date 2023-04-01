package bg.softuni.mygymshop.web;

import bg.softuni.mygymshop.model.dtos.OrderDTO;
import bg.softuni.mygymshop.model.dtos.ProductDetailDTO;
import bg.softuni.mygymshop.model.dtos.UserDTO;
import bg.softuni.mygymshop.service.OrderService;
import bg.softuni.mygymshop.service.ProductService;
import bg.softuni.mygymshop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    private final UserService userService;

    private final ProductService productService;

    public OrderController(OrderService orderService, UserService userService, ProductService productService) {
        this.orderService = orderService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/all")
    public String showAllOrders(Model model, @PageableDefault(
            sort = "id",
            size = 3
    ) Pageable pageable, @ModelAttribute("orderDate") OrderDTO orderDate) {
        Page<OrderDTO> orderDTOs = orderService.getAllOrders(pageable);
        model.addAttribute("orders", orderDTOs);
        model.addAttribute("orderDate", orderDate.getFormattedDate());
        return "orders";
    }

    @GetMapping("/show")
    public String showOrderForm(Model model) {
        List<UserDTO> users = userService.getAllUsers();
        List<ProductDetailDTO> products = productService.getAllProductsForOrder();
        model.addAttribute("order", new OrderDTO());
        model.addAttribute("users", users);
        model.addAttribute("products", products);
        return "order-create";
    }

    @PostMapping("/create")
    public String createOrder(@Valid OrderDTO orderDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("order", orderDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.order",
                    bindingResult);
            return "redirect:/orders/all";
        }

        orderService.createOrder(orderDTO);

        return "redirect:/orders/show";
    }
}
