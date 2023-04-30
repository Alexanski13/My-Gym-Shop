package bg.softuni.mygymshop.repository;

import bg.softuni.mygymshop.model.entities.UserEntity;
import bg.softuni.mygymshop.service.ApplicationUserDetailsService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findUserEntityByUsername(String username);
}
