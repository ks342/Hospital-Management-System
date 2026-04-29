package cg.hospital.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Affiliated_With")
public class AffiliatedWith {

    @EmbeddedId
    private AffiliatedWithId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("physician")
    @JoinColumn(name = "Physician", referencedColumnName = "EmployeeID", nullable = false)
    private Physician physician;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("department")
    @JoinColumn(name = "Department", referencedColumnName = "DepartmentID", nullable = false)
    private Department department;

    @Column(name = "PrimaryAffiliation", nullable = false)
    private boolean primaryAffiliation;

    public AffiliatedWith() {}

    public AffiliatedWith(AffiliatedWithId id, Physician physician,
                          Department department, boolean primaryAffiliation) {
        this.id = id;
        this.physician = physician;
        this.department = department;
        this.primaryAffiliation = primaryAffiliation;
    }

    public AffiliatedWithId getId() { return id; }
    public void setId(AffiliatedWithId id) { this.id = id; }

    public Physician getPhysician() { return physician; }
    public void setPhysician(Physician physician) { this.physician = physician; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public boolean isPrimaryAffiliation() { return primaryAffiliation; }
    public void setPrimaryAffiliation(boolean primaryAffiliation) {
        this.primaryAffiliation = primaryAffiliation;
    }
}