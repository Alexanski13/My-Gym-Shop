package bg.softuni.mygymshop.model.entities;

import bg.softuni.mygymshop.model.enums.ProductCategoryType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategoryType type;

    private String imageUrl;

    @Column(nullable = false)
    private Integer quantity;
    

    @OneToMany(targetEntity = CommentEntity.class, mappedBy = "product", cascade = CascadeType.ALL)
    private Set<CommentEntity> comments;


    public ProductEntity() {
        this.comments = new HashSet<>();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductEntity setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public Set<CommentEntity> getComments() {
        return comments;
    }

    public ProductEntity setComments(Set<CommentEntity> comments) {
        this.comments = comments;
        return this;
    }

    public Long getProductId() {
        return productId;
    }

    public ProductEntity setProductId(Long id) {
        this.productId = id;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductEntity setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public ProductCategoryType getType() {
        return type;
    }

    public ProductEntity setType(ProductCategoryType type) {
        this.type = type;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductEntity setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductEntity setName(String name) {
        this.name = name;
        return this;
    }
}
