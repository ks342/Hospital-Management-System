package cg.hospital.projection;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import cg.hospital.entity.Appointment;

@Projection(name = "appointmentView", types = {Appointment.class})
public interface AppointmentProjection {

    Integer getAppointmentId();

    LocalDateTime getStarto();

    LocalDateTime getEndo();

    String getExaminationRoom();

    // Flatten Patient → name only
    @Value("#{target.patientEntity.name}")
    String getPatientName();

    // Flatten Patient SSN (useful as identifier on the frontend)
    @Value("#{target.patientEntity.ssn}")
    Integer getPatientSsn();

    // Flatten Physician → name only
    @Value("#{target.physicianEntity.name}")
    String getPhysicianName();

    @Value("#{target.physicianEntity.employeeId}")
    Integer getPhysicianId();

    // PrepNurse is nullable – guard against NullPointerException with ternary SpEL
    @Value("#{target.prepNurseEntity != null ? target.prepNurseEntity.name : null}")
    String getPrepNurseName();

    @Value("#{target.prepNurseEntity != null ? target.prepNurseEntity.employeeId : null}")
    Integer getPrepNurseId();
}
