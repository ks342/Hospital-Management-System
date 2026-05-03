package cg.hospital.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PrescribesId implements Serializable {
    @Column(name = "Physician", nullable = false)
    private Integer physician;

    @Column(name = "Patient", nullable = false)
    private Integer patient;

    @Column(name = "Medication", nullable = false)
    private Integer medication;

    @Column(name = "Date", nullable = false)
    private LocalDateTime date;

    public Integer getPhysician() {
        return physician;
    }

    public void setPhysician(Integer physician) {
        this.physician = physician;
    }

    public Integer getPatient() {
        return patient;
    }

    public void setPatient(Integer patient) {
        this.patient = patient;
    }

    public Integer getMedication() {
        return medication;
    }

    public void setMedication(Integer medication) {
        this.medication = medication;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        return Objects.hash(physician, patient, medication, date);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PrescribesId other = (PrescribesId) obj;
        if (physician == null) {
            if (other.physician != null)
                return false;
        } else if (!physician.equals(other.physician))
            return false;
        if (patient == null) {
            if (other.patient != null)
                return false;
        } else if (!patient.equals(other.patient))
            return false;
        if (medication == null) {
            if (other.medication != null)
                return false;
        } else if (!medication.equals(other.medication))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        return true;
    }

}
