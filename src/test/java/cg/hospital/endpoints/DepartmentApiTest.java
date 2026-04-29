package cg.hospital.endpoints;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DepartmentApiTest {

    @Autowired
    private MockMvc mockMvc;

    // ✅ GET ALL
    @Test
    void shouldReturnAllDepartments_whenDepartmentsExist() throws Exception {
        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.departments").exists());
    }

    // ✅ GET BY ID
//    @Test
//    void shouldReturnDepartment_whenValidIdProvided() throws Exception {
//        mockMvc.perform(get("/api/departments/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("General Medicine"));
//    }

    // ✅ SEARCH BY NAME (VALID)
    @Test
    void shouldReturnDepartment_whenNameMatches() throws Exception {
        mockMvc.perform(get("/api/departments/search/findByName?name=Surgery"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Surgery"));
    }

    // ✅ SEARCH BY HEAD (VALID)
    @Test
    void shouldReturnDepartments_whenHeadEmployeeIdExists() throws Exception {
        mockMvc.perform(get("/api/departments/search/findByHead_EmployeeId?employeeId=7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.departments").exists());
    }

    // ✅ EXISTS TRUE
    @Test
    void shouldReturnTrue_whenPhysicianIsHead() throws Exception {
        mockMvc.perform(get("/api/departments/search/existsByHead_EmployeeId?employeeId=4"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    // ✅ EXISTS FALSE
    @Test
    void shouldReturnFalse_whenPhysicianIsNotHead() throws Exception {
        mockMvc.perform(get("/api/departments/search/existsByHead_EmployeeId?employeeId=1"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    // ✅ COUNT CHECK
    @Test
    void shouldReturnDepartmentCount_whenDepartmentsExist() throws Exception {
        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.departments", hasSize(greaterThanOrEqualTo(3))));
    }
}