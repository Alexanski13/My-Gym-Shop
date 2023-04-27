package bg.softuni.mygymshop.repository;

import bg.softuni.mygymshop.model.entities.CartItemEntity;
import bg.softuni.mygymshop.model.entities.ProductEntity;
import bg.softuni.mygymshop.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    List<CartItemEntity> findByUser(UserEntity user);

    CartItemEntity findByUserAndProduct(UserEntity user, ProductEntity product);
}
