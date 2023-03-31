package bg.softuni.mygymshop.model.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OrderDTO {

    private Long id;
    private LocalDate orderDate;
    private ProductDetailDTO product;
    private UserDTO buyer;
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public OrderDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public OrderDTO setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public ProductDetailDTO getProduct() {
        return product;
    }

    public OrderDTO setProduct(ProductDetailDTO product) {
        this.product = product;
        return this;
    }

    public UserDTO getBuyer() {
        return buyer;
    }

    public OrderDTO setBuyer(UserDTO buyer) {
        this.buyer = buyer;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
