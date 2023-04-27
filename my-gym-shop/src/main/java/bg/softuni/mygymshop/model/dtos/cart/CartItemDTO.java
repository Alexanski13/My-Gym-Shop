package bg.softuni.mygymshop.model.dtos.cart;

import java.math.BigDecimal;

public class CartItemDTO {

    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;

    public CartItemDTO() {}

    public CartItemDTO(Long productId, String productName, BigDecimal productPrice, Integer quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public CartItemDTO setProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public CartItemDTO setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public CartItemDTO setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public CartItemDTO setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }
}
