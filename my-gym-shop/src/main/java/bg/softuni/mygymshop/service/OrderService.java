package bg.softuni.mygymshop.service;

import bg.softuni.mygymshop.mapper.OrderMapper;
import bg.softuni.mygymshop.mapper.ProductMapper;
import bg.softuni.mygymshop.model.dtos.OrderDTO;
import bg.softuni.mygymshop.model.entities.OrderEntity;
import bg.softuni.mygymshop.model.entities.ProductEntity;
import bg.softuni.mygymshop.model.entities.UserEntity;
import bg.softuni.mygymshop.repository.OrderRepository;
import bg.softuni.mygymshop.repository.ProductRepository;
import bg.softuni.mygymshop.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    private final OrderMapper orderMapper;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        ModelMapper modelMapper, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.orderMapper = orderMapper;
    }

    public OrderDTO createOrder(OrderDTO orderDto) {
        OrderEntity order = orderMapper.toEntity(orderDto);
        OrderEntity savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        Page<OrderEntity> orderEntities = orderRepository.findAll(pageable);
        return orderEntities.map(orderEntity -> modelMapper.map(orderEntity, OrderDTO.class));
    }
}
