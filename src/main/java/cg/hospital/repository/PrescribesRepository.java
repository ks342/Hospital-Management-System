package cg.hospital.repository;

import cg.hospital.entity.Prescribes;
import cg.hospital.entity.PrescribesId;
import cg.hospital.projection.PrescribesProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "prescriptions", collectionResourceRel = "prescriptions", excerptProjection = PrescribesProjection.class)
public interface PrescribesRepository
        extends JpaRepository<Prescribes, PrescribesId> {

    // GET /api/prescriptions/search/findByAppointment_AppointmentId?id={id}
    Page<Prescribes> findByAppointment_AppointmentId(
            @Param("id") Integer appointmentId,
            Pageable pageable);

    Page<Prescribes> findByIdPhysician(
            @Param("physician") Integer physician,
            Pageable pageable);

    Page<Prescribes> findByIdPatient(
            @Param("patient") Integer patient,
            Pageable pageable);
}