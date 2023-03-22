package bg.softuni.mygymshop.service;

import bg.softuni.mygymshop.model.entities.CategoryEntity;
import bg.softuni.mygymshop.model.enums.ProductCategoryType;
import bg.softuni.mygymshop.repository.ProductCategoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static bg.softuni.mygymshop.model.constants.ProductCategoryDescription.*;

@Service
public class ProductService {

    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
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
}
