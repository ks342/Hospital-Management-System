package cg.hospital.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "PRESCRIBES")
public class Prescribes {

    @EmbeddedId
    private PrescribesId id;

    @Column(name = "Dose", nullable = false, length = 30)
    private String dose;

    // Maps to the "Appointment INTEGER" foreign key in your DDL
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Appointment", referencedColumnName = "AppointmentID")
    private Appointment appointment;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Physician", referencedColumnName = "EmployeeID", insertable = false, updatable = false)
    private Physician physicianEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Patient", referencedColumnName = "SSN", insertable = false, updatable = false)
    private Patient patientEntity;

    // Provides easy reading of the joined Medication details
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Medication", referencedColumnName = "Code", insertable = false, updatable = false)
    private Medication medicationEntity;

    public PrescribesId getId() {
        return id;
    }

    public void setId(PrescribesId id) {
        this.id = id;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Medication getMedicationEntity() {
        return medicationEntity;
    }

    public void setMedicationEntity(Medication medicationEntity) {
        this.medicationEntity = medicationEntity;
    }

    public Physician getPhysicianEntity() {
        return physicianEntity;
    }

    public void setPhysicianEntity(Physician physicianEntity) {
        this.physicianEntity = physicianEntity;
    }

    public Patient getPatientEntity() {
        return patientEntity;
    }

    public void setPatientEntity(Patient patientEntity) {
        this.patientEntity = patientEntity;
    }

}