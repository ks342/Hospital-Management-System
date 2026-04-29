package cg.hospital.entity;

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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Appointment", referencedColumnName = "AppointmentID")
    private Appointment appointment;

    // Provides easy reading of the joined Medication details
    @ManyToOne(fetch = FetchType.LAZY)
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

    
}