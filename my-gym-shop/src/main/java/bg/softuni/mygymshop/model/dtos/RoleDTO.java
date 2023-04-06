package bg.softuni.mygymshop.model.dtos;

import bg.softuni.mygymshop.model.enums.RoleType;

public class RoleDTO {

    private Long id;
    private RoleType role;

    public RoleDTO() {
    }

    public RoleDTO(Long id, RoleType role) {
        this.id = id;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public RoleDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public RoleType getRole() {
        return role;
    }

    public RoleDTO setRole(RoleType role) {
        this.role = role;
        return this;
    }
}
