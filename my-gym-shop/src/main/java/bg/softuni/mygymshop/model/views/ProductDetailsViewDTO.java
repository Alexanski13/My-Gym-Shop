package bg.softuni.mygymshop.model.views;

import bg.softuni.mygymshop.model.enums.ProductCategoryType;

import java.math.BigDecimal;

public class ProductDetailsViewDTO {

    private Long id;

    private String name;
    private String imageUrl;
    private ProductCategoryType type;
    private BigDecimal price;

    private String description;

    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public ProductDetailsViewDTO setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public String offerSummary() {
        return type + " " + price;
    }

    public String getName() {
        return name;
    }

    public ProductDetailsViewDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Long getId() {
        return id;
    }

    public ProductDetailsViewDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductDetailsViewDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public ProductCategoryType getType() {
        return type;
    }

    public ProductDetailsViewDTO setType(ProductCategoryType type) {
        this.type = type;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductDetailsViewDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductDetailsViewDTO setDescription(String description) {
        this.description = description;
        return this;
    }
}
