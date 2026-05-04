package cg.hospital.repository;

import cg.hospital.entity.Appointment;
import cg.hospital.projection.AppointmentProjection;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "appointments", collectionResourceRel = "appointments", excerptProjection = AppointmentProjection.class)
public interface AppointmentRepository
        extends JpaRepository<Appointment, Integer> {

    // GET /api/appointments/search/findByOrderByStartoDesc?size=10
    Page<Appointment> findByOrderByStartoDesc(
            Pageable pageable);

    Page<Appointment> findByPatientEntitySsn(
            @Param("patient") Integer patient,
            Pageable pageable);

    Page<Appointment> findByPhysicianEntityEmployeeId(
            @Param("physician") Integer physician,
            Pageable pageable);

    Page<Appointment> findByPrepNurseEntityEmployeeId(
            @Param("nurse") Integer nurse,
            Pageable pageable);
}