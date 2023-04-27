package bg.softuni.mygymshop.model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_item")
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column
    private Integer quantity;

    public CartItemEntity() {
    }

    public CartItemEntity(UserEntity user, ProductEntity product, Integer quantity) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public CartItemEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public CartItemEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public CartItemEntity setProduct(ProductEntity product) {
        this.product = product;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public CartItemEntity setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }
}