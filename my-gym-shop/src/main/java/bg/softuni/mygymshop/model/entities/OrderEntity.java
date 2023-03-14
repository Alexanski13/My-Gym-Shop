package bg.softuni.mygymshop.model.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime orderDate;

    @ManyToOne
    private UserEntity buyer;

    @ManyToMany
    private List<ProductEntity> products;

    @Column
    private BigDecimal totalPrice;

    public OrderEntity() {
        this.products = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public OrderEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public OrderEntity setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public UserEntity getBuyer() {
        return buyer;
    }

    public OrderEntity setBuyer(UserEntity buyer) {
        this.buyer = buyer;
        return this;
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public OrderEntity setProducts(List<ProductEntity> products) {
        this.products = products;
        return this;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public OrderEntity setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }
}
