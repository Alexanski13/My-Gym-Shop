package bg.softuni.mygymshop.config;

import bg.softuni.mygymshop.mapper.OrderMapper;
import bg.softuni.mygymshop.mapper.ProductMapper;
import bg.softuni.mygymshop.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;


    @Autowired
    public MapperConfig(ProductMapper productMapper, OrderMapper orderMapper, UserMapper userMapper) {
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
        this.userMapper = userMapper;
    }
}
