package cg.hospital.repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import cg.hospital.entity.Nurse;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = Replace.NONE)  // uses your MySQL, not H2
public class NurseRepositoryTest {

    @Autowired
    private NurseRepository nurseRepository;

    @BeforeEach
    void setUp() {
        // Data from insert_data.sql — saved fresh before each test
        nurseRepository.save(new Nurse(101, "Carla Espinosa",  "Head Nurse", true,  111111110));
        nurseRepository.save(new Nurse(102, "Laverne Roberts", "Nurse",      true,  222222220));
        nurseRepository.save(new Nurse(103, "Paul Flowers",    "Nurse",      false, 333333330));
    }

    // TC-1: testFindAll
    @Test
    void testFindAll() {
        List<Nurse> nurses = nurseRepository.findAll();
        assertFalse(nurses.isEmpty());
        assertTrue(nurses.size() >= 3);
    }

    // TC-2: testFindById
    @Test
    void testFindById() {
        Optional<Nurse> nurse = nurseRepository.findById(101);
        assertTrue(nurse.isPresent());
        assertEquals(101, nurse.get().getEmployeeId());
        assertEquals("Carla Espinosa", nurse.get().getName());
    }

    // TC-3: testSaveNurse
    @Test
    void testSaveNurse() {
        Nurse newNurse = new Nurse(104, "Test Nurse", "Nurse", true, 444444440);
        Nurse saved = nurseRepository.save(newNurse);
        assertNotNull(saved);
        assertEquals(104, saved.getEmployeeId());
        assertEquals("Test Nurse", saved.getName());
    }

    // TC-4: testUpdateRegistered
    @Test
    void testUpdateRegistered() {
        Nurse nurse = nurseRepository.findById(101).get();
        nurse.setRegistered(false);
        Nurse updated = nurseRepository.save(nurse);
        assertEquals(false, updated.getRegistered());
    }

    // TC-5: testUpdateSSN
    @Test
    void testUpdateSSN() {
        Nurse nurse = nurseRepository.findById(101).get();
        nurse.setSsn(999999);
        Nurse updated = nurseRepository.save(nurse);
        assertEquals(999999, updated.getSsn());
    }
}