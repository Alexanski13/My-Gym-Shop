package bg.softuni.mygymshop.service;

import bg.softuni.mygymshop.model.dtos.CreateProductDTO;
import bg.softuni.mygymshop.model.dtos.ProductDetailDTO;
import bg.softuni.mygymshop.model.dtos.ProductInventoryDTO;
import bg.softuni.mygymshop.model.entities.CategoryEntity;
import bg.softuni.mygymshop.model.entities.ProductEntity;
import bg.softuni.mygymshop.model.enums.ProductCategoryType;
import bg.softuni.mygymshop.model.views.ProductDetailsViewDTO;
import bg.softuni.mygymshop.repository.CategoryRepository;
import bg.softuni.mygymshop.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static bg.softuni.mygymshop.model.constants.ProductCategoryDescription.*;

@Service
public class ProductService {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;


    @Autowired
    public ProductService(CategoryRepository categoryRepository,
                          ProductRepository productRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    private void initCategories() {
        if (categoryRepository.count() == 0) {
            var creatineCategory = new CategoryEntity().setType(ProductCategoryType.CREATINE)
                    .setDescription(CREATINE_DESCRIPTION);
            var proteinCategory = new CategoryEntity().setType(ProductCategoryType.PROTEIN)
                    .setDescription(PROTEIN_DESCRIPTION);
            var vitaminCategory = new CategoryEntity().setType(ProductCategoryType.VITAMIN)
                    .setDescription(VITAMIN_DESCRIPTION);

            categoryRepository.save(creatineCategory);
            categoryRepository.save(proteinCategory);
            categoryRepository.save(vitaminCategory);
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
                setType(productEntity.getType()).
                setPrice(productEntity.getPrice()).
                setQuantity(productEntity.getQuantity());
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
                .setType(productDTO.getType())
                .setQuantity(productDTO.getQuantity());
    }

    private CreateProductDTO mapToProductDTO(ProductEntity product) {
        return new CreateProductDTO()
                .setName(product.getName())
                .setPrice(product.getPrice())
                .setImageUrl(product.getImageUrl())
                .setDescription(product.getDescription())
                .setType(product.getType())
                .setQuantity(product.getQuantity());
    }

    private ProductDetailDTO mapToProductDetailDTO(ProductEntity product) {
        return new ProductDetailDTO()
                .setId(product.getProductId())
                .setName(product.getName())
                .setPrice(product.getPrice())
                .setImageUrl(product.getImageUrl())
                .setDescription(product.getDescription())
                .setType(product.getType())
                .setQuantity(product.getQuantity());
    }

    public Optional<ProductDetailDTO> findProductById(Long id) {
        return productRepository.findById(id)
                .map(this::mapToProductDetailDTO);
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    public List<ProductDetailDTO> getAllProductsForOrder() {
        return productRepository.findAll().stream().map(this::mapToProductDetailDTO).collect(Collectors.toList());

    }

    public ProductEntity getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Unable to find product!"));
    }

    // IMPLEMENTING SCHEDULE JOB

    @Transactional
    public void updateProductInventories(List<ProductInventoryDTO> productInventories) {
        for (ProductInventoryDTO productInventory : productInventories) {
            ProductEntity product = productRepository.findById(productInventory.getId()).orElse(null);
            if (product != null) {
                product.setQuantity(productInventory.getQuantity());
                productRepository.save(product);
            }
        }
    }

    public List<ProductInventoryDTO> getAllProductInventories() {
        List<ProductEntity> products = productRepository.findAll();
        List<ProductInventoryDTO> productInventories = new ArrayList<>();
        for (ProductEntity product : products) {
            ProductInventoryDTO productInventory = new ProductInventoryDTO();
            productInventory.setId(product.getProductId());
            productInventory.setName(product.getName());
            productInventory.setQuantity(product.getQuantity());
            productInventories.add(productInventory);
        }
        return productInventories;
    }

    // UPDATE PRODUCT

    @Transactional
    public void updateProduct(CreateProductDTO productDTO) {
        ProductEntity product = productRepository.findById(productDTO.getId()).orElse(null);
        if (product != null) {
            product
                    .setName(productDTO.getName())
                    .setDescription(productDTO.getDescription())
                    .setType(productDTO.getType())
                    .setName(productDTO.getName())
                    .setPrice(productDTO.getPrice())
                    .setQuantity(productDTO.getQuantity())
                    .setImageUrl(productDTO.getImageUrl());
            productRepository.saveAndFlush(product);
        }
    }
}
