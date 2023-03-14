package bg.softuni.mygymshop.model.entities;

import bg.softuni.mygymshop.model.enums.CategoryType;
import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private CategoryType type;

    @Column(columnDefinition = "TEXT")
    private String description;

    public CategoryEntity() {
    }

    public Long getId() {
        return id;
    }

    public CategoryEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public CategoryType getType() {
        return type;
    }

    public CategoryEntity setType(CategoryType type) {
        this.type = type;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CategoryEntity setDescription(String description) {
        this.description = description;
        return this;
    }
}
