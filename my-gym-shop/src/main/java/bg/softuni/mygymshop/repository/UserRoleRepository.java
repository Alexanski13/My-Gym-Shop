package bg.softuni.mygymshop.repository;

import bg.softuni.mygymshop.model.entities.RoleEntity;
import bg.softuni.mygymshop.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findUserRoleEntityByRole(RoleType role);

    Optional<RoleEntity> findByRole(RoleType role);
}
