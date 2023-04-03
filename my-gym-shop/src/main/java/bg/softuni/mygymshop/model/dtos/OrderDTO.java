package bg.softuni.mygymshop.model.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderDTO {

    private Long productId;
    private Long userId;
    private LocalDateTime orderDate = LocalDateTime.now();
    private ProductDetailDTO product;
    private UserDTO buyer;
    private BigDecimal price;

    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public OrderDTO setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public Long getProductId() {
        return productId;
    }

    public OrderDTO setProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public OrderDTO setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public OrderDTO setOrderDate(LocalDateTime orderDate) {
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



    public String getFormattedDate() {
        LocalDate convertDate = getOrderDate().toLocalDate();
        return convertDate.toString();
    }
}
