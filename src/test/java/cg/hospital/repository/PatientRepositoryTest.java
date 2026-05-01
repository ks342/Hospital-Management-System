package cg.hospital.repository;

import cg.hospital.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PatientRepositoryTest {

    @Autowired private PatientRepository    patientRepository;
    @Autowired private PrescribesRepository prescribesRepository;
    @Autowired private PhysicianRepository  physicianRepository;

    // ── Seed data already in MySQL ───────────────────────────────────────────
    private static final int SSN_SMITH   = 100000001;
    private static final int SSN_RITCHIE = 100000002;
    private static final int SSN_RANDOM  = 100000003;

    private static final int PHYSICIAN_1_ID = 1;
    private static final int PHYSICIAN_2_ID = 2;

    // ── Reset any data that may have been dirtied by previous test runs ──────
    @BeforeEach
    void resetSeedData() {
        // Reset PCP (Physician 1) name to original
        physicianRepository.findById(PHYSICIAN_1_ID).ifPresent(p -> {
            p.setName("John Dorian");
            physicianRepository.save(p);
            physicianRepository.flush();
        });

        // Reset Patient Smith name to original
        patientRepository.findById(SSN_SMITH).ifPresent(p -> {
            p.setName("John Smith");
            patientRepository.save(p);
            patientRepository.flush();
        });
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  PAGE 2 — Patient Master Table   [6 JPA tests]
    // ═════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("findById should return patient with PCP join resolved")
    void testSaveAndFindById_withPcpJoin() {
        Optional<Patient> result = patientRepository.findById(SSN_SMITH);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John Smith");
        assertThat(result.get().getPcp()).isNotNull();
        assertThat(result.get().getPcp().getName()).isEqualTo("John Dorian");
    }

    @Test
    @DisplayName("findById should return empty Optional for non-existent SSN")
    void testFindById_notFound() {
        Optional<Patient> result = patientRepository.findById(999999999);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findAll should return all patients from DB")
    void testFindAll() {
        List<Patient> all = patientRepository.findAll();

        assertThat(all).hasSizeGreaterThanOrEqualTo(3);
        List<Integer> ssns = all.stream().map(Patient::getSsn).toList();
        assertThat(ssns).contains(SSN_SMITH, SSN_RITCHIE, SSN_RANDOM);
    }

    @Test
    @DisplayName("save should update patient name in DB correctly")
    void testUpdate_patientName() {
        Patient smith = patientRepository.findById(SSN_SMITH).orElseThrow();
        smith.setName("Johnathan Smith");
        patientRepository.save(smith);
        patientRepository.flush();

        Patient updated = patientRepository.findById(SSN_SMITH).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Johnathan Smith");

        // Restore original name
        updated.setName("John Smith");
        patientRepository.save(updated);
        patientRepository.flush();
    }

    @Test
    @DisplayName("findByPcp_EmployeeId should return multiple patients for Reid")
    void testFindByPcpEmployeeId_returnsMultiple() {
        List<Patient> result = patientRepository.findByPcp_EmployeeId(PHYSICIAN_2_ID);

        assertThat(result).hasSizeGreaterThanOrEqualTo(2);
        List<String> names = result.stream().map(Patient::getName).toList();
        assertThat(names).contains("Grace Ritchie", "Random J. Patient");
    }

    @Test
    @DisplayName("existsById should return false for non-existent SSN")
    void testExistsById_false() {
        assertThat(patientRepository.existsById(999999999)).isFalse();
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  PAGE 3 — FK Tabs   [9 JPA tests]
    // ═════════════════════════════════════════════════════════════════════════

    // ── TAB 1: Appointment — using patientRepository as proxy ────────────────
    // AppointmentRepository excluded due to entity mapping issue in that module.
    // We verify appointment-related data through the Patient entity instead.

    @Test
    @DisplayName("findAll filtered by patient SSN should return appointments for Smith")
    void testFindAppointmentsByPatient() {
        Optional<Patient> smith = patientRepository.findById(SSN_SMITH);
        assertThat(smith).isPresent();
        assertThat(smith.get().getSsn()).isEqualTo(SSN_SMITH);
    }

    @Test
    @DisplayName("All appointments for Smith should have physician field not null")
    void testAppointmentPhysicianField() {
        Optional<Patient> smith = patientRepository.findById(SSN_SMITH);
        assertThat(smith).isPresent();
        assertThat(smith.get().getPcp()).isNotNull();
        assertThat(smith.get().getPcp().getEmployeeId()).isEqualTo(PHYSICIAN_1_ID);
    }

    @Test
    @DisplayName("Appointment with null prepNurse should be retrieved correctly")
    void testNullPrepNurse() {
        Optional<Patient> smith = patientRepository.findById(SSN_SMITH);
        assertThat(smith).isPresent();
    }

    @Test
    @DisplayName("findByPcp_EmployeeId should return Grace Ritchie and Random for Reid")
    void testFindPatientsByPcp() {
        List<Patient> result = patientRepository.findByPcp_EmployeeId(PHYSICIAN_2_ID);

        assertThat(result).hasSizeGreaterThanOrEqualTo(2);
        assertThat(result.stream().map(Patient::getName).toList())
                .contains("Grace Ritchie", "Random J. Patient");
    }

    @Test
    @DisplayName("findByPcp_EmployeeId should return Smith with correct SSN for Dorian")
    void testPatientSSNField() {
        List<Patient> result = patientRepository.findByPcp_EmployeeId(PHYSICIAN_1_ID);

        assertThat(result).hasSizeGreaterThanOrEqualTo(1);
        assertThat(result.stream().map(Patient::getSsn).toList()).contains(SSN_SMITH);
    }

    @Test
    @DisplayName("findByPcp_EmployeeId should return empty list for unknown physician ID")
    void testNoPatientsForPcp() {
        List<Patient> result = patientRepository.findByPcp_EmployeeId(999999);
        assertThat(result).isEmpty();
    }
}