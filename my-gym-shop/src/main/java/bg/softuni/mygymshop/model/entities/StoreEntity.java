package bg.softuni.mygymshop.model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "stores")
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String location;

    @Column(nullable = false, unique = true)
    private String address;

    @Column
    private String status;

    public StoreEntity() {
    }

    public Long getId() {
        return id;
    }

    public StoreEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public StoreEntity setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public StoreEntity setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public StoreEntity setStatus(String status) {
        this.status = status;
        return this;
    }
}
