package cg.hospital.repository;

import cg.hospital.entity.Physician;
import cg.hospital.entity.TrainedIn;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PhysicianRepositoryTest {

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private TrainedInRepository trainedInRepository;

    // ── PAGE 2 TESTS ──

    @Test
    public void testFindAll() {
        List<Physician> list = physicianRepository.findAll();
        assertThat(list).isNotEmpty();
        assertThat(list.size())
            .isGreaterThanOrEqualTo(9);
    }

    @Test
    public void testFindByName() {
        List<Physician> list =
            physicianRepository.findByName("John Dorian Updated");
        assertThat(list).isNotEmpty();
    }

    @Test
    public void testFindByPosition() {
        List<Physician> list =
            physicianRepository
            .findByPosition("Staff Internist");
        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getPosition())
            .isEqualTo("Staff Internist");
    }

    @Test
    public void testFindByNameNotFound() {
        List<Physician> list =
            physicianRepository.findByName("Nobody");
        assertThat(list).isEmpty();
    }

    @Test
    public void testFindBySurgicalAttending() {
        List<Physician> list =
            physicianRepository
            .findByPosition("Surgical Attending Physician");
        assertThat(list.size())
            .isGreaterThanOrEqualTo(3);
    }

    @Test
    @Transactional
    public void testCreate() {
        Physician p = new Physician();
        p.setEmployeeId(101);
        p.setName("Test Doctor");
        p.setPosition("Test Position");
        p.setSsn(999999998);
        Physician saved = physicianRepository.save(p);
        assertThat(saved.getEmployeeId()).isEqualTo(101);
        physicianRepository.deleteById(101);
    }

    @Test
    @Transactional
    public void testUpdate() {
        Physician p = physicianRepository
            .findById(1).orElse(null);
        assertThat(p).isNotNull();
        String original = p.getName();
        p.setName("Test Doctor");
        physicianRepository.save(p);
        Physician updated = physicianRepository
            .findById(1).orElse(null);
        assertThat(updated.getName())
            .isEqualTo("Test Doctor");
        updated.setName(original);
        physicianRepository.save(updated);
    }

    @Test
    public void testFindAllNotEmpty() {
        assertThat(physicianRepository.findAll())
            .isNotEmpty();
    }

    @Test
    public void testFindAllFirstRecord() {
        Physician p = physicianRepository
            .findAll().get(0);
        assertThat(p.getEmployeeId()).isEqualTo(1);
    }

    // ── PAGE 3 TESTS ──

    @Test
    public void testFindById() {
        Physician p = physicianRepository
            .findById(1).orElse(null);
        assertThat(p).isNotNull();
        assertThat(p.getPosition())
            .isEqualTo("Staff Internist");
        assertThat(p.getSsn()).isEqualTo(111111111);
    }

    @Test
    public void testFindByIdNotFound() {
        Physician p = physicianRepository
            .findById(999).orElse(null);
        assertThat(p).isNull();
    }

    @Test
    public void testTrainedInByPhysician() {
        List<TrainedIn> list =
            trainedInRepository.findByIdPhysician(3);
        assertThat(list).isNotEmpty();
    }

    @Test
    public void testTrainedInEmpty() {
        List<TrainedIn> list =
            trainedInRepository.findByIdPhysician(1);
        assertThat(list).isEmpty();
    }

    @Test
    public void testTrainedInCertDate() {
        List<TrainedIn> list =
            trainedInRepository.findByIdPhysician(3);
        assertThat(list).isNotEmpty();
        assertThat(list.get(0)
            .getCertificationDate()).isNotNull();
    }
}