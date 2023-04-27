package bg.softuni.mygymshop.model.dtos.product;

public class ProductInventoryDTO {

    private Long id;
    private String name;
    private Integer quantity;

    public Long getId() {
        return id;
    }

    public ProductInventoryDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductInventoryDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductInventoryDTO setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }
}
