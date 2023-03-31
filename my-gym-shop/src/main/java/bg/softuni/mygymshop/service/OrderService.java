package bg.softuni.mygymshop.service;

import bg.softuni.mygymshop.model.dtos.OrderDTO;
import bg.softuni.mygymshop.model.entities.OrderEntity;
import bg.softuni.mygymshop.model.entities.ProductEntity;
import bg.softuni.mygymshop.model.entities.UserEntity;
import bg.softuni.mygymshop.repository.OrderRepository;
import bg.softuni.mygymshop.repository.ProductRepository;
import bg.softuni.mygymshop.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository,
                        ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public void placeOrder(OrderDTO orderDTO) {
        OrderEntity orderEntity = modelMapper.map(orderDTO, OrderEntity.class);
        ProductEntity productEntity = productRepository.findById(orderDTO.getProduct().getId()).orElseThrow();
        UserEntity userEntity = userRepository.findById(orderDTO.getBuyer().getId()).orElseThrow();
        orderEntity.setProduct(productEntity);
        orderEntity.setBuyer(userEntity);
        orderRepository.save(orderEntity);
    }

    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        Page<OrderEntity> orderEntities = orderRepository.findAll(pageable);
        return orderEntities.map(orderEntity -> modelMapper.map(orderEntity, OrderDTO.class));
    }
}
