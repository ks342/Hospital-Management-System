package cg.hospital.config;

import cg.hospital.entity.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.spi.BackendIdConverter;

import java.io.Serializable;

/**
 * Registers BackendIdConverter beans for every entity that uses a composite
 * (embedded) primary key.  Without these, Spring Data REST cannot parse the
 * "{part1}_{part2}" string from the URL path into the correct @EmbeddedId /
 * @IdClass type, producing a 500 "No converter found" error.
 *
 * Covered entities:
 *   AffiliatedWith  → AffiliatedWithId  (physician_department)
 *   Block           → BlockId           (blockFloor_blockCode)
 *   TrainedIn       → TrainedInId       (physician_treatment)
 *   Undergoes       → UndergoesId       (procedures_stay)
 *   Prescribes      → PrescribesId      (physician_patient_medication_date)
 */
@Configuration
public class CompositeIdConverterConfig {

    // ── AffiliatedWith: "1_1" → AffiliatedWithId(physician=1, department=1) ──

    @Bean
    public BackendIdConverter affiliatedWithIdConverter() {
        return new BackendIdConverter() {

            @Override
            public boolean supports(Class<?> delimiter) {
                return AffiliatedWith.class.equals(delimiter);
            }

            @Override
            public Serializable fromRequestId(String id, Class<?> entityType) {
                if (id == null || !id.contains("_"))
                    throw new IllegalArgumentException(
                            "AffiliatedWith id must be in format {physicianId}_{departmentId}, got: " + id);
                String[] parts = id.split("_", 2);
                return new AffiliatedWithId(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            }

            @Override
            public String toRequestId(Serializable id, Class<?> entityType) {
                AffiliatedWithId awId = (AffiliatedWithId) id;
                return awId.getPhysician() + "_" + awId.getDepartment();
            }
        };
    }

    // ── Block: "1_2" → BlockId(blockFloor=1, blockCode=2) ────────────────────

    @Bean
    public BackendIdConverter blockIdConverter() {
        return new BackendIdConverter() {

            @Override
            public boolean supports(Class<?> delimiter) {
                return Block.class.equals(delimiter);
            }

            @Override
            public Serializable fromRequestId(String id, Class<?> entityType) {
                if (id == null || !id.contains("_"))
                    throw new IllegalArgumentException(
                            "Block id must be in format {blockFloor}_{blockCode}, got: " + id);
                String[] parts = id.split("_", 2);
                return new BlockId(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            }

            @Override
            public String toRequestId(Serializable id, Class<?> entityType) {
                BlockId bId = (BlockId) id;
                return bId.getBlockFloor() + "_" + bId.getBlockCode();
            }
        };
    }

    // ── TrainedIn: "3_1" → TrainedInId(physician=3, treatment=1) ─────────────

    @Bean
    public BackendIdConverter trainedInIdConverter() {
        return new BackendIdConverter() {

            @Override
            public boolean supports(Class<?> delimiter) {
                return TrainedIn.class.equals(delimiter);
            }

            @Override
            public Serializable fromRequestId(String id, Class<?> entityType) {
                if (id == null || !id.contains("_"))
                    throw new IllegalArgumentException(
                            "TrainedIn id must be in format {physicianId}_{treatmentCode}, got: " + id);
                String[] parts = id.split("_", 2);
                TrainedInId tid = new TrainedInId();
                tid.setPhysician(Integer.parseInt(parts[0]));
                tid.setTreatment(Integer.parseInt(parts[1]));
                return tid;
            }

            @Override
            public String toRequestId(Serializable id, Class<?> entityType) {
                TrainedInId tid = (TrainedInId) id;
                return tid.getPhysician() + "_" + tid.getTreatment();
            }
        };
    }

    // ── Undergoes: "2_3215" → UndergoesId(procedures=2, stay=3215) ───────────

    @Bean
    public BackendIdConverter undergoesIdConverter() {
        return new BackendIdConverter() {

            @Override
            public boolean supports(Class<?> delimiter) {
                return Undergoes.class.equals(delimiter);
            }

            @Override
            public Serializable fromRequestId(String id, Class<?> entityType) {
                if (id == null || !id.contains("_"))
                    throw new IllegalArgumentException(
                            "Undergoes id must be in format {procedureCode}_{stayId}, got: " + id);
                String[] parts = id.split("_", 2);
                return new UndergoesId(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            }

            @Override
            public String toRequestId(Serializable id, Class<?> entityType) {
                UndergoesId uid = (UndergoesId) id;
                return uid.getProcedures() + "_" + uid.getStay();
            }
        };
    }

    // ── Prescribes: "1_100000001_1_2008-04-24T10:47:00" → PrescribesId ───────
    // PrescribesId has 4 parts: physician, patient, medication, date (ISO datetime)

    @Bean
    public BackendIdConverter prescribesIdConverter() {
        return new BackendIdConverter() {

            @Override
            public boolean supports(Class<?> delimiter) {
                return Prescribes.class.equals(delimiter);
            }

            @Override
            public Serializable fromRequestId(String id, Class<?> entityType) {
                if (id == null)
                    throw new IllegalArgumentException("Prescribes id must not be null");
                // Format: {physician}_{patient}_{medication}_{date}
                // Date may contain colons and dashes — split on first 3 underscores only
                String[] parts = id.split("_", 4);
                if (parts.length != 4)
                    throw new IllegalArgumentException(
                            "Prescribes id must be in format {physician}_{patient}_{medication}_{date}, got: " + id);
                PrescribesId pid = new PrescribesId();
                pid.setPhysician(Integer.parseInt(parts[0]));
                pid.setPatient(Integer.parseInt(parts[1]));
                pid.setMedication(Integer.parseInt(parts[2]));
                pid.setDate(java.time.LocalDateTime.parse(parts[3]));
                return pid;
            }

            @Override
            public String toRequestId(Serializable id, Class<?> entityType) {
                PrescribesId pid = (PrescribesId) id;
                return pid.getPhysician() + "_"
                        + pid.getPatient() + "_"
                        + pid.getMedication() + "_"
                        + pid.getDate().toString();
            }
        };
    }
}
