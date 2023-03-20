package bg.softuni.mygymshop.model.entities;

import bg.softuni.mygymshop.model.enums.RoleType;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleType role;

    public RoleEntity() {
    }

    public Long getId() {
        return id;
    }

    public RoleEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public RoleType getRole() {
        return role;
    }

    public RoleEntity setRole(RoleType name) {
        this.role = name;
        return this;
    }
}
