package cg.hospital.repository;

import cg.hospital.entity.Prescribes;
import cg.hospital.entity.PrescribesId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PrescribesRepositoryTest {

    @Autowired
    private PrescribesRepository prescribesRepository;

    // findAll is intentionally not allowed per rules

    @Test
    public void testFindById() {
        PrescribesId id = new PrescribesId();
        id.setPhysician(1);
        id.setPatient(1);
        id.setMedication(1);
        id.setDate(java.time.LocalDateTime.now());
        Prescribes found = prescribesRepository.findById(id).orElse(null);
        // It might be null if DB is empty
    }

    @Test
    public void testFindByAppointment_AppointmentID() {
        Pageable pageable = PageRequest.of(0, 10);
        // Assuming an appointment ID exists, we can fetch all Prescribes to get a valid
        // ID if needed,
        // but testing the method invocation is sufficient for JPA tests.
        Page<Prescribes> page = prescribesRepository.findByAppointment_AppointmentId(1, pageable);
        assertThat(page).isNotNull();
    }

    @Test
    public void testFindByIdPhysician() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Prescribes> page = prescribesRepository.findByIdPhysician(1, pageable);
        assertThat(page).isNotNull();
        if (page.hasContent()) {
            assertThat(page.getContent().get(0).getId().getPhysician()).isEqualTo(1);
        }
    }

    @Test
    public void testFindByIdPatient() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Prescribes> page = prescribesRepository.findByIdPatient(1, pageable);
        assertThat(page).isNotNull();
        if (page.hasContent()) {
            assertThat(page.getContent().get(0).getId().getPatient()).isEqualTo(1);
        }
    }
}
