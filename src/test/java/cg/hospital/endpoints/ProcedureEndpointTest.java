package cg.hospital.endpoints;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProcedureEndpointTest {

	@Autowired
	private MockMvc mockMvc;

	// =========================================================================
	// PAGE 2 — PROCEDURE MASTER TABLE (CRUD) — 5 Tests (unchanged)
	// =========================================================================

	@Test
	void testGetAllProcedures() throws Exception {
		mockMvc.perform(get("/api/procedures")).andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.procedures").isArray());
	}

	@Test
	void testGetProcedureById_found() throws Exception {
		mockMvc.perform(get("/api/procedures/1")).andExpect(status().isOk()).andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.cost").exists());
	}

	@Test
	void testGetProcedureById_notFound() throws Exception {
		mockMvc.perform(get("/api/procedures/99999")).andExpect(status().isNotFound());
	}

	@Test
	void testCreateProcedure() throws Exception {
		String json = "{\"code\": 8001, \"name\": \"MockMVC Test Proc\", \"cost\": 750.0}";

		mockMvc.perform(post("/api/procedures").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated());

		mockMvc.perform(get("/api/procedures/8001")).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("MockMVC Test Proc")).andExpect(jsonPath("$.cost").value(750.0));
	}

	@Test
	void testUpdateProcedureCost() throws Exception {
		String createJson = "{\"code\": 8002, \"name\": \"Update Cost Test\", \"cost\": 100.0}";
		mockMvc.perform(post("/api/procedures").contentType(MediaType.APPLICATION_JSON).content(createJson))
				.andExpect(status().isCreated());

		String updateJson = "{\"code\": 8002, \"name\": \"Update Cost Test\", \"cost\": 9999.0}";
		mockMvc.perform(put("/api/procedures/8002").contentType(MediaType.APPLICATION_JSON).content(updateJson))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/procedures/8002")).andExpect(status().isOk())
				.andExpect(jsonPath("$.cost").value(9999.0));
	}

	// =========================================================================
	// PAGE 3 — TAB 1: Trained_In
	//
	// WHY THIS URL:
	// @RepositoryRestResource(path = "trained-in")
	// Method: findByIdTreatment(@Param("treatment") Integer treatment)
	// → URL: GET /api/trained-in/search/findByIdTreatment?treatment={value}
	//
	// The method name is findByIdTreatment (navigating the @EmbeddedId field
	// "id" → field "treatment" inside TrainedInId). Spring Data REST uses the
	// method name as-is for the URL segment, and @Param("treatment") as the
	// query parameter name.
	//
	// collectionResourceRel = "trainedIn"
	// → _embedded key is "trainedIn" (NOT "trainedIns" or "trained-ins")
	// =========================================================================

	// TEST 6 — findByIdTreatment?treatment=1 → 200 OK, records present.
	// Seed data has physicians trained for treatment code=1.
	@Test
	void testTrainedInEndpoint_found() throws Exception {
		mockMvc.perform(get("/api/trained-in/search/findByIdTreatment").param("treatment", "1"))
				.andExpect(status().isOk())
				// collectionResourceRel="trainedIn" → key inside _embedded is "trainedIn"
				.andExpect(jsonPath("$._embedded.trainedIn").isArray())
				.andExpect(jsonPath("$._embedded.trainedIn.length()").value(org.hamcrest.Matchers.greaterThan(0)));
	}

	// TEST 7 — findByIdTreatment?treatment=8 → 200 OK, empty result.
	// Spring Data REST returns _embedded: {trainedIn: []} for empty collections
	// (does NOT omit _embedded). So we assert the array inside is empty.
	@Test
	void testTrainedInEndpoint_notFound() throws Exception {
		mockMvc.perform(get("/api/trained-in/search/findByIdTreatment").param("treatment", "8"))
				.andExpect(status().isOk()).andExpect(jsonPath("$._embedded.trainedIn").isArray())
				.andExpect(jsonPath("$._embedded.trainedIn").isEmpty());
	}

	// TEST 9 — findByProcedures?procedures=8 → 200 OK, empty result.
	// Same behaviour: _embedded is present but contains an empty array.
	// The key inside _embedded matches the repository's collectionResourceRel
	// (or defaults to the entity name in camelCase — check your UndergoesRepository
	// @RepositoryRestResource annotation for the exact key name).
	@Test
	void testUndergoesEndpoint_notFound() throws Exception {
		mockMvc.perform(get("/api/undergoes/search/findByProcedures").param("procedures", "8"))
				.andExpect(status().isOk())
				// _embedded exists but all arrays inside it are empty
				// value() checks the raw map — if all collections empty, the map values are []
				.andExpect(jsonPath("$._embedded").isMap()).andExpect(jsonPath("$._embedded.*[0]").doesNotExist());
	}

	// =========================================================================
	// PAGE 3 — TAB 2: Undergoes
	//
	// Repository method : findByProcedures(@Param("procedures") Integer procedures)
	// URL: GET /api/undergoes/search/findByProcedures?procedures={value}
	// =========================================================================

	// TEST 8 — findByProcedures?procedures=1 → 200 OK, records present.
	// Seed data: procedure=1 has 1 record (Patient=100000004, Stay=3217).
	@Test
	void testUndergoesEndpoint_found() throws Exception {
		mockMvc.perform(get("/api/undergoes/search/findByProcedures").param("procedures", "1"))
				.andExpect(status().isOk()).andExpect(jsonPath("$._embedded").exists());
	}

	// TEST 10 — Physician HAL link present for procedures=1
	@Test
	void testUndergoesEndpoint_physicianLinkPresent() throws Exception {
		mockMvc.perform(get("/api/undergoes/search/findByProcedures").param("procedures", "1"))
				.andExpect(status().isOk()).andExpect(jsonPath("$._embedded").exists())
				.andExpect(jsonPath("$._embedded..['_links']['physician']").exists());
	}

	// TEST 11 — AssistingNurse HAL link present for procedures=1
	@Test
	void testUndergoesEndpoint_assistingNurseLinkPresent() throws Exception {
		mockMvc.perform(get("/api/undergoes/search/findByProcedures").param("procedures", "1"))
				.andExpect(status().isOk()).andExpect(jsonPath("$._embedded").exists())
				.andExpect(jsonPath("$._embedded..['_links']['assistingNurse']").exists());
	}

	// TEST 12 — Exact seed name for procedures/1
	@Test
	void testGetProcedureById_correctName() throws Exception {
		mockMvc.perform(get("/api/procedures/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Reverse Rhinopodoplasty"));
	}
}