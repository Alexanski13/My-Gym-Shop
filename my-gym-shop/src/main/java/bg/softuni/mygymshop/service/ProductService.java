package bg.softuni.mygymshop.service;

import bg.softuni.mygymshop.model.dtos.CreateProductDTO;
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
                setId(productEntity.getId()).
                setImageUrl(productEntity.getImageUrl()).
                setDescription(productEntity.getDescription()).
                setType(productEntity.getType());
    }

    public boolean addProduct(CreateProductDTO createProductDTO) {
        Optional<ProductEntity> byName =
                this.productRepository.findByName(createProductDTO.getName());

        if (byName.isPresent()) {
            return false;
        }

        ProductEntity product = new ProductEntity();
        product.setName(createProductDTO.getName());
        product.setDescription(createProductDTO.getDescription());
        product.setType(createProductDTO.getType());
        product.setPrice(createProductDTO.getPrice());
        product.setImageUrl(createProductDTO.getImageUrl());


        productRepository.save(product);

        return true;
    }
}
