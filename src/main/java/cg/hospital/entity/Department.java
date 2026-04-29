package cg.hospital.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "department")
public class Department {

    @Id
    @Column(name = "DepartmentID")
    private Integer departmentID;

    @Column(name = "Name", nullable = false, length = 30)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Head", referencedColumnName = "EmployeeID", nullable = false)
    private Physician head;

    public Department() {}

    public Department(Integer departmentID, String name, Physician head) {
        this.departmentID = departmentID;
        this.name = name;
        this.head = head;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Physician getHead() {
        return head;
    }

    public void setHead(Physician head) {
        this.head = head;
    }
}