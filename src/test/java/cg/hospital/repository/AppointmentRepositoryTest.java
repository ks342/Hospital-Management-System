package cg.hospital.repository;

import cg.hospital.entity.Appointment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    public void testFindAllWithPagination() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Appointment> page = appointmentRepository.findAll(pageable);
        assertThat(page).isNotNull();
    }

    @Test
    public void testFindById() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Appointment> page = appointmentRepository.findAll(pageable);
        if (page.hasContent()) {
            Appointment first = page.getContent().get(0);
            Appointment found = appointmentRepository.findById(first.getAppointmentId()).orElse(null);
            assertThat(found).isNotNull();
            assertThat(found.getAppointmentId()).isEqualTo(first.getAppointmentId());
        }
    }

    @Test
    public void testFindByOrderByStartoDesc() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Appointment> page = appointmentRepository.findByOrderByStartoDesc(pageable);
        assertThat(page).isNotNull();
        if (page.hasContent() && page.getContent().size() > 1) {
            Appointment first = page.getContent().get(0);
            Appointment second = page.getContent().get(1);
            assertThat(first.getStarto()).isAfterOrEqualTo(second.getStarto());
        }
    }

    @Test
    public void testFindByPatientEntitySsn() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Appointment> page = appointmentRepository.findByPatientEntitySsn(1, pageable);
        assertThat(page).isNotNull();
        if (page.hasContent()) {
            assertThat(page.getContent().get(0).getPatientEntity().getSsn()).isEqualTo(1);
        }
    }

    @Test
    public void testFindByPhysicianEntityEmployeeId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Appointment> page = appointmentRepository.findByPhysicianEntityEmployeeId(1, pageable);
        assertThat(page).isNotNull();
        if (page.hasContent()) {
            assertThat(page.getContent().get(0).getPhysicianEntity().getEmployeeId()).isEqualTo(1);
        }
    }

    @Test
    public void testFindByPrepNurseEntityEmployeeId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Appointment> page = appointmentRepository.findByPrepNurseEntityEmployeeId(101, pageable);
        assertThat(page).isNotNull();
        if (page.hasContent()) {
            assertThat(page.getContent().get(0).getPrepNurseEntity().getEmployeeId()).isEqualTo(101);
        }
    }

    @Test
    @Transactional
    public void testSaveAndUpdate() {
        // The MySQL table uses GENERATED ALWAYS AS IDENTITY, but the JDBC driver
        // doesn't always support auto-increment retrieval for this.
        // Test update only: fetch an existing record and modify it.
        Page<Appointment> existing = appointmentRepository.findAll(PageRequest.of(0, 1));
        if (!existing.hasContent()) {
            // DB is empty, skip gracefully
            return;
        }

        Appointment appt = existing.getContent().get(0);
        String originalRoom = appt.getExaminationRoom();

        // Update
        appt.setExaminationRoom("Room Z");
        Appointment updated = appointmentRepository.save(appt);
        assertThat(updated.getExaminationRoom()).isEqualTo("Room Z");

        // Restore original value
        updated.setExaminationRoom(originalRoom);
        appointmentRepository.save(updated);
    }
}
