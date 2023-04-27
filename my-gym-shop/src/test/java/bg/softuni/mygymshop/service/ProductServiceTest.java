package bg.softuni.mygymshop.service;

import bg.softuni.mygymshop.model.dtos.product.CreateProductDTO;
import bg.softuni.mygymshop.model.entities.ProductEntity;
import bg.softuni.mygymshop.model.enums.ProductCategoryType;
import bg.softuni.mygymshop.model.views.ProductDetailsViewDTO;
import bg.softuni.mygymshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testGetAllProducts_success() {
        // arrange
        List<ProductEntity> products = Arrays.asList(
                new ProductEntity()
                        .setName("Product 1")
                        .setProductId(1L)
                        .setImageUrl("http://image1.jpg")
                        .setDescription("Description 1")
                        .setType(ProductCategoryType.CREATINE)
                        .setPrice(BigDecimal.valueOf(10.0))
                        .setQuantity(5),
                new ProductEntity()
                        .setName("Product 2")
                        .setProductId(2L)
                        .setImageUrl("http://image2.jpg")
                        .setDescription("Description 2")
                        .setType(ProductCategoryType.PROTEIN)
                        .setPrice(BigDecimal.valueOf(20.0))
                        .setQuantity(10),
                new ProductEntity()
                        .setName("Product 3")
                        .setProductId(3L)
                        .setImageUrl("http://image3.jpg")
                        .setDescription("Description 3")
                        .setType(ProductCategoryType.VITAMIN)
                        .setPrice(BigDecimal.valueOf(30.0))
                        .setQuantity(20)
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductEntity> page = new PageImpl<>(products, pageable, products.size());
        when(productRepository.findAll(pageable)).thenReturn(page);

        // act
        Page<ProductDetailsViewDTO> result = productService.getAllProducts(pageable);

        // assert
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(1, result.getTotalPages());
        assertEquals(3, result.getTotalElements());
        assertEquals("Product 1", result.getContent().get(0).getName());
        assertEquals(1, result.getContent().get(0).getId());
        assertEquals("http://image1.jpg", result.getContent().get(0).getImageUrl());
        assertEquals("Description 1", result.getContent().get(0).getDescription());
        assertEquals(ProductCategoryType.CREATINE, result.getContent().get(0).getType());
        assertEquals(BigDecimal.valueOf(10.0), result.getContent().get(0).getPrice(), String.valueOf(0.001));
        assertEquals(5, result.getContent().get(0).getQuantity());
    }

    @Test
    void testGetAllProducts_ThrowsException() {
        // arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findAll(pageable)).thenThrow(new RuntimeException("Unable to retrieve products"));

        // act and assert
        assertThrows(RuntimeException.class, () -> productService.getAllProducts(pageable));
    }

    @Test
    public void testAddProduct_Success() {
        // Arrange
        CreateProductDTO createProductDTO = new CreateProductDTO();
        createProductDTO.setName("Product 1");
        createProductDTO.setPrice(BigDecimal.valueOf(10.0));
        createProductDTO.setImageUrl("https://example.com/image.jpg");
        createProductDTO.setDescription("This is a product.");
        createProductDTO.setType(ProductCategoryType.CREATINE);
        createProductDTO.setQuantity(5);

        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(1L);
        productEntity.setName("Product 1");
        productEntity.setPrice(BigDecimal.valueOf(10.0));
        productEntity.setImageUrl("https://example.com/image.jpg");
        productEntity.setDescription("This is a product.");
        productEntity.setType(ProductCategoryType.CREATINE);
        productEntity.setQuantity(5);

        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        // Act
        CreateProductDTO result = productService.addProduct(createProductDTO);

        // Assert
        assertEquals("Product 1", result.getName());
        assertEquals(BigDecimal.valueOf(10.0), result.getPrice());
        assertEquals("https://example.com/image.jpg", result.getImageUrl());
        assertEquals("This is a product.", result.getDescription());
        assertEquals(ProductCategoryType.CREATINE, result.getType());
        assertEquals(5, result.getQuantity());
    }

    @Test
    public void addProduct_throws() {
        CreateProductDTO createProductDTO = new CreateProductDTO();

        when(productRepository.save(any(ProductEntity.class))).thenThrow(new RuntimeException("Failed to save product"));

        assertThrows(RuntimeException.class, () -> productService.addProduct(createProductDTO));
    }
}