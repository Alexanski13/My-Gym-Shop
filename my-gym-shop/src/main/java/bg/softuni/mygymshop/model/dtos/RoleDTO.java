package bg.softuni.mygymshop.model.dtos;

import bg.softuni.mygymshop.model.enums.RoleType;

public class RoleDTO {

    private Long id;
    private RoleType name;

    public RoleDTO() {
    }

    public RoleDTO(Long id, RoleType name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public RoleDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public RoleType getName() {
        return name;
    }

    public RoleDTO setName(RoleType name) {
        this.name = name;
        return this;
    }
}
