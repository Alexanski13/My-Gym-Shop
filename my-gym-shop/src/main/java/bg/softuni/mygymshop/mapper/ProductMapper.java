package bg.softuni.mygymshop.mapper;

import bg.softuni.mygymshop.model.dtos.ProductDetailDTO;
import bg.softuni.mygymshop.model.entities.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductDetailDTO toDto(ProductEntity product) {
        return new ProductDetailDTO()
                .setId(product.getProductId())
                .setType(product.getType())
                .setName(product.getName())
                .setDescription(product.getDescription())
                .setImageUrl(product.getImageUrl())
                .setPrice(product.getPrice())
                .setQuantity(product.getQuantity());
    }

    public ProductEntity toEntity(ProductDetailDTO productDto) {
        return new ProductEntity()
                .setProductId(productDto.getId())
                .setType(productDto.getType())
                .setName(productDto.getName())
                .setDescription(productDto.getDescription())
                .setImageUrl(productDto.getImageUrl())
                .setPrice(productDto.getPrice())
                .setQuantity(productDto.getQuantity());
    }
}
