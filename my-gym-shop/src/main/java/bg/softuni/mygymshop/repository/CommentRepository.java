package bg.softuni.mygymshop.repository;

import bg.softuni.mygymshop.model.entities.CommentEntity;
import bg.softuni.mygymshop.model.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByProduct(ProductEntity product);
}
