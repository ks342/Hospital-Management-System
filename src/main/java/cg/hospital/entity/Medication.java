package cg.hospital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MEDICATION")
public class Medication {

    @Id
    @Column(name = "Code", nullable = false)
    private Integer code;

    @Column(name = "Name", nullable = false, length = 30)
    private String name;

    @Column(name = "Brand", nullable = false, length = 30)
    private String brand;

    @Column(name = "Description", nullable = false, length = 30)
    private String description;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}