package bg.softuni.mygymshop.service;

import bg.softuni.mygymshop.model.dtos.CreateProductDTO;
import bg.softuni.mygymshop.model.dtos.ProductDetailDTO;
import bg.softuni.mygymshop.model.entities.CategoryEntity;
import bg.softuni.mygymshop.model.entities.ProductEntity;
import bg.softuni.mygymshop.model.enums.ProductCategoryType;
import bg.softuni.mygymshop.model.views.ProductDetailsViewDTO;
import bg.softuni.mygymshop.repository.ProductCategoryRepository;
import bg.softuni.mygymshop.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static bg.softuni.mygymshop.model.constants.ProductCategoryDescription.*;

@Service
public class ProductService {

    private final ProductCategoryRepository productCategoryRepository;

    private final ProductRepository productRepository;


    @Autowired
    public ProductService(ProductCategoryRepository productCategoryRepository,
                          ProductRepository productRepository) {
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
    }

    @PostConstruct
    private void initCategories() {
        if (productCategoryRepository.count() == 0) {
            var creatineCategory = new CategoryEntity().setType(ProductCategoryType.CREATINE)
                    .setDescription(CREATINE_DESCRIPTION);
            var proteinCategory = new CategoryEntity().setType(ProductCategoryType.PROTEIN)
                    .setDescription(PROTEIN_DESCRIPTION);
            var vitaminCategory = new CategoryEntity().setType(ProductCategoryType.VITAMIN)
                    .setDescription(VITAMIN_DESCRIPTION);

            productCategoryRepository.save(creatineCategory);
            productCategoryRepository.save(proteinCategory);
            productCategoryRepository.save(vitaminCategory);
        }
    }

    public Page<ProductDetailsViewDTO> getAllProducts(Pageable pageable) {
        return
                productRepository.
                        findAll(pageable).
                        map(this::map);
    }

    private ProductDetailsViewDTO map(ProductEntity productEntity) {
        return new ProductDetailsViewDTO().
                setName(productEntity.getName()).
                setId(productEntity.getProductId()).
                setImageUrl(productEntity.getImageUrl()).
                setDescription(productEntity.getDescription()).
                setType(productEntity.getType());
    }

    public CreateProductDTO addProduct(CreateProductDTO createProductDTO) {
        ProductEntity product = mapToProduct(createProductDTO);
        ProductEntity savedProduct = productRepository.save(product);
        return mapToProductDTO(savedProduct);
    }

    private ProductEntity mapToProduct(CreateProductDTO productDTO) {
        return new ProductEntity()
                .setName(productDTO.getName())
                .setPrice(productDTO.getPrice())
                .setImageUrl(productDTO.getImageUrl())
                .setDescription(productDTO.getDescription())
                .setType(productDTO.getType());
    }

    private CreateProductDTO mapToProductDTO(ProductEntity product) {
        return new CreateProductDTO()
                .setName(product.getName())
                .setPrice(product.getPrice())
                .setImageUrl(product.getImageUrl())
                .setDescription(product.getDescription())
                .setType(product.getType());
    }

    private ProductDetailDTO mapToProductDetailDTO(ProductEntity product) {
        return new ProductDetailDTO()
                .setId(product.getProductId())
                .setName(product.getName())
                .setPrice(product.getPrice())
                .setImageUrl(product.getImageUrl())
                .setDescription(product.getDescription())
                .setType(product.getType());
    }

    public ProductEntity getProductById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    public Optional<ProductDetailDTO> findProductById(Long id) {
        return productRepository.findById(id)
                .map(this::mapToProductDetailDTO);
    }

    public void updateProduct(ProductDetailDTO productDto) {
        ProductEntity product = productRepository.findById(productDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        product.setProductId(product.getProductId());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setImageUrl(productDto.getImageUrl());
        product.setType(productDto.getType());

        productRepository.save(product);
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
}
