package bg.softuni.mygymshop.model.entities;

import bg.softuni.mygymshop.model.enums.RoleName;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleName name;

    public RoleEntity() {
    }

    public Long getId() {
        return id;
    }

    public RoleEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public RoleName getName() {
        return name;
    }

    public RoleEntity setName(RoleName name) {
        this.name = name;
        return this;
    }
}
