package cg.hospital.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "APPOINTMENT")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AppointmentID", nullable = false)
    private Integer appointmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Patient", referencedColumnName = "SSN", nullable = false)
    private Patient patientEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PrepNurse", referencedColumnName = "EmployeeID")
    private Nurse prepNurseEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Physician", referencedColumnName = "EmployeeID", nullable = false)
    private Physician physicianEntity;

    @Column(name = "Starto", nullable = false)
    private LocalDateTime starto;

    @Column(name = "Endo", nullable = false)
    private LocalDateTime endo;

    @Column(name = "ExaminationRoom", nullable = false, columnDefinition = "TEXT")
    private String examinationRoom;

    // The HATEOAS relationship builder
    @JsonIgnore
    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Prescribes prescriptions;

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Patient getPatientEntity() {
        return patientEntity;
    }

    public void setPatientEntity(Patient patientEntity) {
        this.patientEntity = patientEntity;
    }

    public Nurse getPrepNurseEntity() {
        return prepNurseEntity;
    }

    public void setPrepNurseEntity(Nurse prepNurseEntity) {
        this.prepNurseEntity = prepNurseEntity;
    }

    public Physician getPhysicianEntity() {
        return physicianEntity;
    }

    public void setPhysicianEntity(Physician physicianEntity) {
        this.physicianEntity = physicianEntity;
    }

    public LocalDateTime getStarto() {
        return starto;
    }

    public void setStarto(LocalDateTime starto) {
        this.starto = starto;
    }

    public LocalDateTime getEndo() {
        return endo;
    }

    public void setEndo(LocalDateTime endo) {
        this.endo = endo;
    }

    public String getExaminationRoom() {
        return examinationRoom;
    }

    public void setExaminationRoom(String examinationRoom) {
        this.examinationRoom = examinationRoom;
    }

    public Prescribes getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(Prescribes prescriptions) {
        this.prescriptions = prescriptions;
    }

}