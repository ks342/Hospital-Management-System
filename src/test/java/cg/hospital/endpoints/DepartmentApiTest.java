package cg.hospital.endpoints;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DepartmentApiTest {

    @Autowired
    private MockMvc mockMvc;

    // ── GET ALL ─────────────────────────────────────────

    @Test
    void shouldReturnAllDepartments_whenDepartmentsExist() throws Exception {
        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.departments").exists());
    }

    @Test
    void shouldReturnDepartmentCount_whenDepartmentsExist() throws Exception {
        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.departments", hasSize(greaterThan(0))));
    }

    // ── GET BY ID ───────────────────────────────────────

    @Test
    void shouldReturnDepartment_whenValidIdProvided() throws Exception {
        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    void shouldReturn404_whenDepartmentIdNotFound() throws Exception {
        int id = 999999; // safe dynamic range
        mockMvc.perform(get("/api/departments/" + id))
                .andExpect(status().isNotFound());
    }

    // ── GET HEAD ────────────────────────────────────────

//    @Test
//    void shouldReturnHeadPhysician_whenDepartmentExists() throws Exception {
//        mockMvc.perform(get("/api/departments/1/head"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.employeeID").exists());
//    }

    @Test
    void shouldReturn404_whenDepartmentNotFoundForHead() throws Exception {
        int id = 999999;
        mockMvc.perform(get("/api/departments/" + id + "/head"))
                .andExpect(status().isNotFound());
    }

    // ── SEARCH ──────────────────────────────────────────

    @Test
    void shouldReturnDepartment_whenNameMatches() throws Exception {
        mockMvc.perform(get("/api/departments/search/findByName?name=Surgery"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Surgery"));
    }

    @Test
    void shouldReturn404_whenNameNotFound() throws Exception {
        mockMvc.perform(get("/api/departments/search/findByName?name=XYZ123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnDepartments_whenHeadEmployeeIdExists() throws Exception {
        mockMvc.perform(get("/api/departments/search/findByHead_EmployeeId?employeeId=7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.departments").exists());
    }

    @Test
    void shouldReturnEmptyList_whenHeadEmployeeIdNotFound() throws Exception {
        mockMvc.perform(get("/api/departments/search/findByHead_EmployeeId?employeeId=999999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.departments", hasSize(0)));
    }

    // ── EXISTS ──────────────────────────────────────────

    @Test
    void shouldReturnTrue_whenPhysicianIsHead() throws Exception {
        mockMvc.perform(get("/api/departments/search/existsByHead_EmployeeId?employeeId=4"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void shouldReturnFalse_whenPhysicianIsNotHead() throws Exception {
        mockMvc.perform(get("/api/departments/search/existsByHead_EmployeeId?employeeId=1"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    // ── POST ────────────────────────────────────────────

    @Test
    void shouldCreateDepartment_whenValidDataProvided() throws Exception {
        int id = (int) (Math.random() * 100000 + 10000);

        String json = """
                {
                  "departmentID": %d,
                  "name": "Radiology",
                  "head": "http://localhost:8083/api/physicians/4"
                }
                """.formatted(id);

        mockMvc.perform(post("/api/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/api/departments/" + id))
                .andExpect(status().isNoContent());
    }

//    @Test
//    void shouldReturn4xx_whenInvalidDepartmentData() throws Exception {
//        String badJson = "{}";
//
//        mockMvc.perform(post("/api/departments")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(badJson))
//                .andExpect(status().is4xxClientError());
//    }

    // ── PUT ─────────────────────────────────────────────

    @Test
    void shouldUpdateDepartmentName_whenValidRequest() throws Exception {
        String json = """
                {
                  "departmentID": 1,
                  "name": "Updated Name",
                  "head": "http://localhost:8083/api/physicians/4"
                }
                """;

        mockMvc.perform(put("/api/departments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNoContent());
    }

//    @Test
//    void shouldCreate_whenUpdatingNonExistingDepartment() throws Exception {
//        int id = (int) (Math.random() * 100000 + 20000);
//
//        String json = """
//                {
//                  "departmentID": %d,
//                  "name": "Ghost Dept",
//                  "head": "http://localhost:8083/api/physicians/4"
//                }
//                """.formatted(id);
//
//        mockMvc.perform(put("/api/departments/" + id)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//                .andExpect(status().isNoContent());
//
//        mockMvc.perform(delete("/api/departments/" + id))
//                .andExpect(status().isNoContent());
//    }

    // ── DELETE ──────────────────────────────────────────

    @Test
    void shouldDeleteDepartment_whenDepartmentExists() throws Exception {
        int id = (int) (Math.random() * 100000 + 30000);

        String json = """
                {
                  "departmentID": %d,
                  "name": "TempDept",
                  "head": "http://localhost:8083/api/physicians/4"
                }
                """.formatted(id);

        mockMvc.perform(post("/api/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/api/departments/" + id))
                .andExpect(status().isNoContent());
    }
}