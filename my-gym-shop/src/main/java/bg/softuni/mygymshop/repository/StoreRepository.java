package bg.softuni.mygymshop.repository;

import bg.softuni.mygymshop.model.entities.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
}
