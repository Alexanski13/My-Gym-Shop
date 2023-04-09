package bg.softuni.mygymshop.mapper;

import bg.softuni.mygymshop.model.dtos.OrderDTO;
import bg.softuni.mygymshop.model.dtos.ProductDetailDTO;
import bg.softuni.mygymshop.model.dtos.UserDTO;
import bg.softuni.mygymshop.model.entities.OrderEntity;
import bg.softuni.mygymshop.model.entities.ProductEntity;
import bg.softuni.mygymshop.model.entities.UserEntity;
import bg.softuni.mygymshop.repository.ProductRepository;
import bg.softuni.mygymshop.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class OrderMapper {

    private final ProductMapper productMapper;
    private final UserMapper userMapper;

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderMapper(ProductMapper productMapper, UserMapper userMapper,
                       ProductRepository productRepository, UserRepository userRepository) {
        this.productMapper = productMapper;
        this.userMapper = userMapper;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public OrderDTO toDto(OrderEntity order) {
        return new OrderDTO()
                .setId(order.getId())
                .setUserId(order.getBuyer().getId())
                .setProductId(order.getProduct().getProductId())
                .setOrderDate(order.getOrderDate())
                .setPrice(order.getTotalPrice())
                .setQuantity(order.getQuantity());
    }

    public OrderEntity toEntity(OrderDTO orderDto) {
        OrderEntity order = new OrderEntity();
        order.setId(orderDto.getId());
        ProductEntity product = productRepository.findById(orderDto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (product.getQuantity() < 1) {
            throw new NoSuchElementException("Product is out of stock");
        }

        product.setQuantity(product.getQuantity() - 1);
        productRepository.save(product);

        ProductDetailDTO productDetailDTO = productMapper.toDto(product);
        order.setProduct(productMapper.toEntity(productDetailDTO));

        UserEntity user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        UserDTO userDTO = userMapper.toDto(user);
        order.setBuyer(userMapper.toEntity(userDTO));
        order.setTotalPrice(productDetailDTO.getPrice());
        order.setOrderDate(orderDto.getOrderDate());
        order.setQuantity(orderDto.getQuantity());
        return order;
    }
}