package bg.softuni.mygymshop.model.dtos;

import bg.softuni.mygymshop.model.enums.ProductCategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ProductDetailDTO {

    private Long id;

    private String name;

    private ProductCategoryType type;

    private BigDecimal price;

    private String description;

    private String imageUrl;

    public Long getId() {
        return id;
    }

    public ProductDetailDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductDetailDTO setName(String name) {
        this.name = name;
        return this;
    }

    public ProductCategoryType getType() {
        return type;
    }

    public ProductDetailDTO setType(ProductCategoryType type) {
        this.type = type;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductDetailDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductDetailDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductDetailDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
