package cg.hospital.repository;
 
import cg.hospital.entity.Appointment;
import cg.hospital.entity.Nurse;
import cg.hospital.entity.OnCall;
import cg.hospital.entity.Undergoes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
 
import java.util.List;
import java.util.Optional;
 
import static org.assertj.core.api.Assertions.assertThat;
 
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class NurseRepositoryTest {
 
    @Autowired private NurseRepository       nurseRepository;
    @Autowired private OnCallRepository      onCallRepository;
    @Autowired private UndergoesRepository   undergoesRepository;
    @Autowired private  AppointmentRepository appointmentRepository; 
    // NOTE: AppointmentRepository NOT autowired here
    // because teammate's repo has no findByPrepNurse method yet
    // — tested via endpoint test instead
 
    private static final int NURSE_CARLA   = 101;
    private static final int NURSE_PAUL    = 103;
 
    // ═══════════════════════════════════════════════════════════════
    //  PAGE 2 — Nurse Master   [5 JPA tests]
    // ═══════════════════════════════════════════════════════════════
 
    @Test
    @DisplayName("findAll should return all nurses from DB")
    void testFindAll() {
        List<Nurse> list = nurseRepository.findAll();
        assertThat(list).isNotEmpty();
        assertThat(list).isNotNull();
        assertThat(list.size()).isGreaterThanOrEqualTo(3);
    }
    
 
    @Test
    @DisplayName("findById should return Carla Espinosa for ID 101")
    void testFindById() {
        Optional<Nurse> nurse = nurseRepository.findById(NURSE_CARLA);
        assertThat(nurse).isPresent();
        assertThat(nurse.get().getEmployeeId()).isEqualTo(NURSE_CARLA);
        assertThat(nurse).isPresent();
        assertThat(nurse.get().getEmployeeId()).isEqualTo(101);
    }
 

    
    @Test
    @DisplayName("save should persist a new nurse to DB")
    void testSaveNurse() {
        Nurse newNurse = new Nurse(199, "Test Nurse", "Nurse", true, 444444440);
        Nurse saved = nurseRepository.save(newNurse);
        assertThat(saved).isNotNull();
        assertThat(saved.getEmployeeId()).isEqualTo(199);
        nurseRepository.deleteById(199);
    }
 
    @Test
    @DisplayName("save should update registered field for Paul Flowers")
    void testUpdateRegistered() {
        Nurse nurse = nurseRepository.findById(NURSE_PAUL).orElseThrow();
        boolean original = nurse.getRegistered();
        nurse.setRegistered(true);
        nurseRepository.save(nurse);
        Nurse updated = nurseRepository.findById(NURSE_PAUL).orElseThrow();
        assertThat(updated.getRegistered()).isTrue();
        // restore
        updated.setRegistered(original);
        nurseRepository.save(updated);
    }
 
    @Test
    @DisplayName("save should update SSN for Carla Espinosa")
    void testUpdateSSN() {
        Nurse nurse = nurseRepository.findById(NURSE_CARLA).orElseThrow();
        int original = nurse.getSsn();
        nurse.setSsn(999999);
        nurseRepository.save(nurse);
        Nurse updated = nurseRepository.findById(NURSE_CARLA).orElseThrow();
        assertThat(updated.getSsn()).isEqualTo(999999);
        // restore
        updated.setSsn(original);
        nurseRepository.save(updated);
    }

    //  PAGE 3 — Associated Tables   [6 JPA tests]


    // ── Appointments (PrepNurse) 

//    TC-6
//    @Test
//    @DisplayName("findByPrepNurse_EmployeeID should return appointments for Carla")
//    void testFindAppointmentsByNurse() {
//        List<Appointment> result = appointmentRepository
//                .findByPrepNurseEntityEmployeeId(NURSE_CARLA);
//        assertThat(result).isNotEmpty();
//    }
    @Test
    @DisplayName("findByPrepNurse should return appointments for Carla")
    void testFindAppointmentsByNurse() {

        Page<Appointment> result =
                appointmentRepository.findByPrepNurseEntityEmployeeId(
                        NURSE_CARLA,
                        PageRequest.of(0, 10)
                );

        assertThat(result).isNotEmpty();
    }
    @Test
    @DisplayName("Appointments for Carla should have valid patient and start date")
    void testAppointmentFields() {

        Page<Appointment> result = appointmentRepository
                .findByPrepNurseEntityEmployeeId(NURSE_CARLA, PageRequest.of(0, 10));

        assertThat(result).isNotEmpty();

        Appointment a = result.getContent().get(0);

        assertThat(a.getPatientEntity()).isNotNull();
        assertThat(a.getStarto()).isNotNull();
    }

    // ── On-Call 

//    TC-8
    @Test
    @DisplayName("findByNurse_EmployeeID should return on-call records for Carla")
    void testFindOnCallByNurse() {
        List<OnCall> result = onCallRepository
                .findByNurse_EmployeeId(NURSE_CARLA);
        assertThat(result).isNotEmpty();
    }

    // TC-9
    @Test
    @DisplayName("On-call records for Carla should have valid start and end times")
    void testOnCallTimeRange() {
        List<OnCall> result = onCallRepository
                .findByNurse_EmployeeId(NURSE_CARLA);
        assertThat(result).isNotEmpty();
        result.forEach(oc -> {
            assertThat(oc.getOnCallStart()).isNotNull();
            assertThat(oc.getOnCallEnd()).isNotNull();
            assertThat(oc.getOnCallEnd()).isAfter(oc.getOnCallStart());
        });
    }

    // ── Undergoes (AssistingNurse) ───────────────────────────────

    // TC-10
    @Test
    @DisplayName("findByAssistingNurseId should return procedures Carla assisted in")
    void testFindProceduresByNurse() {
        List<Undergoes> result = undergoesRepository.findByAssistingNurse_EmployeeId(NURSE_CARLA);
        assertThat(result).isNotEmpty();
    }
 
//    Tc-11
    
    @Test
    @DisplayName("Undergoes records for Carla should have valid dateUndergoes")
    void testProcedureDateExists() {
        List<Undergoes> result = undergoesRepository.findByAssistingNurse_EmployeeId(NURSE_CARLA);
        assertThat(result).isNotEmpty();
        result.forEach(u -> assertThat(u.getDateUndergoes()).isNotNull());
    }
}