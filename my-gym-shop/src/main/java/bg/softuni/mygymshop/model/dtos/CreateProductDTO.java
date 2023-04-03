package bg.softuni.mygymshop.model.dtos;


import bg.softuni.mygymshop.model.enums.ProductCategoryType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class CreateProductDTO {

  @NotBlank
  private String name;

  @NotNull
  private ProductCategoryType type;

  @Positive
  @NotNull
  private BigDecimal price;

  @NotEmpty
  private String description;

  @NotEmpty
  private String imageUrl;

  @NotNull
  private Integer quantity;

  public Integer getQuantity() {
    return quantity;
  }

  public CreateProductDTO setQuantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  public ProductCategoryType getType() {
    return type;
  }

  public CreateProductDTO setType(ProductCategoryType type) {
    this.type = type;
    return this;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public CreateProductDTO setPrice(BigDecimal price) {
    this.price = price;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public CreateProductDTO setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public CreateProductDTO setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
    return this;
  }

  public String getName() {
    return name;
  }

  public CreateProductDTO setName(String name) {
    this.name = name;
    return this;
  }
}
