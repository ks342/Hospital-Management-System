package cg.hospital.repository;

import cg.hospital.entity.Undergoes;
import cg.hospital.entity.UndergoesId;
import cg.hospital.repository.UndergoesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * PAGE 3 — TAB 2: JPA Repository Tests for Undergoes (FK: Undergoes.Procedures
 * → Procedures.Code).
 *
 * Uses your REAL MySQL database (Replace.NONE) so actual seeded data is used.
 *
 * From MySQL Workbench screenshot (select * from undergoes), seed data is:
 * Patient=100000001, Procedures=2, Stay=3215, Date=2008-05-03, Physician=7,
 * AssistingNurse=101 Patient=100000001, Procedures=6, Stay=3215,
 * Date=2008-05-02, Physician=3, AssistingNurse=101 Patient=100000001,
 * Procedures=7, Stay=3217, Date=2008-05-10, Physician=7, AssistingNurse=101
 * Patient=100000004, Procedures=1, Stay=3217, Date=2008-05-07, Physician=3,
 * AssistingNurse=102 Patient=100000004, Procedures=4, Stay=3217,
 * Date=2008-05-13, Physician=3, AssistingNurse=103 Patient=100000004,
 * Procedures=5, Stay=3217, Date=2008-05-09, Physician=6, AssistingNurse=NULL
 *
 * So: findByIdProcedures(1) → 1 record (Stay=3217, Physician=3)
 * findByIdProcedures(999) → 0 records
 *
 * Total: 6 test cases (JPA).
 *
 * NOTE: The repository method is: List<Undergoes> findByIdProcedures(Integer
 * procedures); Spring Data REST exposes it as: GET
 * /api/undergoes/search/findByIdProcedures?procedures={value} (method name
 * becomes the URL segment — this is what you saw working in Postman)
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UndergoesRepositoryTest {

	@Autowired
	private UndergoesRepository undergoesRepository;

	// -----------------------------------------------------------------------
	// Test 1 (Page 3 Tab 2 #1): findByIdProcedures(1) returns non-empty list.
	//
	// Procedure code=1 (Reverse Rhinopodoplasty) has 1 record in undergoes
	// per seed data (Stay=3217, Physician=3).
	// -----------------------------------------------------------------------
	@Test
	public void testFindUndergoesForProcedure() {
		List<Undergoes> result = undergoesRepository.findByIdProcedures(1);

		assertThat(result).isNotEmpty();
		// All returned records must have procedures = 1
		result.forEach(u -> assertThat(u.getProcedures()).isEqualTo(1));
	}

	// -----------------------------------------------------------------------
	// Test 2 (Page 3 Tab 2 #2): Each record returned for code=1 has a non-null
	// stayEntity (the FK relationship to Stay table loads correctly).
	//
	// WHY: Undergoes has @ManyToOne to Stay via @JoinColumn(name="Stay",
	// insertable=false, updatable=false). When JPA fetches the record,
	// it should populate stayEntity from the Stay table using the FK value.
	// -----------------------------------------------------------------------
	@Test
	public void testUndergoesStayEntityField() {
		List<Undergoes> result = undergoesRepository.findByIdProcedures(1);

		assertThat(result).isNotEmpty();
		result.forEach(u -> {
			// The plain FK integer should match a known Stay ID
			assertThat(u.getStay()).isNotNull();
			// The @ManyToOne stayEntity should be loaded (Stay table has StayID=3217)
			assertThat(u.getStayEntity()).isNotNull();
			assertThat(u.getStayEntity().getStayId()).isEqualTo(u.getStay());
		});
	}

	// -----------------------------------------------------------------------
	// Test 3 (Page 3 Tab 2 #3): findByIdProcedures(999) returns empty list.
	//
	// Procedure code=999 does not exist in undergoes or procedures tables.
	// -----------------------------------------------------------------------
	@Test
	public void testNoUndergoesForProcedure() {
		List<Undergoes> result = undergoesRepository.findByIdProcedures(999);

		assertThat(result).isEmpty();
	}

	// -----------------------------------------------------------------------
	// Test 4: Verify physician FK relationship loads for procedures=1 record.
	//
	// From seed data: Procedures=1, Physician=3.
	// Undergoes has @ManyToOne Physician (insertable=false, updatable=false).
	// physicianId (plain column) should be 3, and physician entity should load.
	// -----------------------------------------------------------------------
	@Test
	public void testUndergoesPhysicianField() {
		List<Undergoes> result = undergoesRepository.findByIdProcedures(1);

		assertThat(result).isNotEmpty();
		Undergoes record = result.get(0);

		// Plain FK integer
		assertThat(record.getPhysicianId()).isNotNull();
		// @ManyToOne physician entity should be populated
		assertThat(record.getPhysician()).isNotNull();
		assertThat(record.getPhysician().getEmployeeId()).isEqualTo(record.getPhysicianId());
	}

	// -----------------------------------------------------------------------
	// Test 5: findByIdProcedures(2) returns records — procedure=2 has 1 record.
	//
	// From seed: Patient=100000001, Procedures=2, Stay=3215.
	// -----------------------------------------------------------------------
	@Test
	public void testFindUndergoesForProcedure_code2() {
		List<Undergoes> result = undergoesRepository.findByIdProcedures(2);

		assertThat(result).isNotEmpty();
		result.forEach(u -> assertThat(u.getProcedures()).isEqualTo(2));
	}

	// -----------------------------------------------------------------------
	// Test 6: Verify composite PK (UndergoesId) can be used to find a record.
	//
	// From seed: Procedures=1, Stay=3217 is a valid composite PK.
	// -----------------------------------------------------------------------
	@Test
	public void testFindByCompositeId() {
		UndergoesId id = new UndergoesId(1, 3217);
		java.util.Optional<Undergoes> result = undergoesRepository.findById(id);

		assertThat(result).isPresent();
		assertThat(result.get().getProcedures()).isEqualTo(1);
		assertThat(result.get().getStay()).isEqualTo(3217);
	}
}