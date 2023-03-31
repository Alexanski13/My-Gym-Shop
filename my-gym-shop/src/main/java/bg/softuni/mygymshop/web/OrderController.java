package bg.softuni.mygymshop.web;

import bg.softuni.mygymshop.model.dtos.OrderDTO;
import bg.softuni.mygymshop.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;

    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public String showAllOrders(Model model, @PageableDefault(
            sort = "id",
            size = 3
    ) Pageable pageable) {
        Page<OrderDTO> orderDTOs = orderService.getAllOrders(pageable);
        model.addAttribute("orders", orderDTOs);
        return "orders";
    }
}
