package bg.softuni.mygymshop.model.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @ManyToOne
    private CategoryEntity category;

    public ProductEntity() {
    }

    public Long getId() {
        return id;
    }

    public ProductEntity setId(Long id) {
        this.id = id;
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
}
