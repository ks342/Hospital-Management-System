package cg.hospital.repository;

import cg.hospital.entity.Department;
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
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    @DisplayName("findAll should return all departments from DB")
    void testFindAll() {
        List<Department> all = departmentRepository.findAll();

        // Based on your DB:
        // 1 - General Medicine
        // 2 - Surgery
        // 3 - Psychiatry
        assertThat(all).hasSizeGreaterThanOrEqualTo(3);
    }

//    @Test
//    @DisplayName("findById should return department when ID exists")
//    void testFindById_found() {
//        Optional<Department> result = departmentRepository.findById(1);
//
//        assertThat(result).isPresent();
//        assertThat(result.get().getName()).isEqualTo("General Medicine");
//    }

//    @Test
//    @DisplayName("findById should return empty when ID does not exist")
//    void testFindById_notFound() {
//        Optional<Department> result = departmentRepository.findById(999);
//
//        assertThat(result).isEmpty();
//    }

    @Test
    @DisplayName("findByName should return department when name matches")
    void testFindByName_found() {
        Optional<Department> result = departmentRepository.findByName("Surgery");

        assertThat(result).isPresent();
        assertThat(result.get().getDepartmentID()).isEqualTo(2);
    }

    @Test
    @DisplayName("findByName should return empty when name does not match")
    void testFindByName_notFound() {
        Optional<Department> result = departmentRepository.findByName("Dermatology");

        assertThat(result).isEmpty();
    }

//    @Test
//    @DisplayName("findByHead_EmployeeId should return correct departments")
//    void testFindByHeadEmployeeID() {
//        List<Department> result = departmentRepository.findByHead_EmployeeId(4);
//
//        assertThat(result).isNotEmpty();
//        assertThat(result.get(0).getName()).isEqualTo("General Medicine");
//    }

    @Test
    @DisplayName("findByHead_EmployeeId should return empty list")
    void testFindByHeadEmployeeID_noResults() {
        List<Department> result = departmentRepository.findByHead_EmployeeId(999);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("existsByHead_EmployeeId should return true")
    void testExistsByHeadEmployeeID_exists() {
        boolean exists = departmentRepository.existsByHead_EmployeeId(4);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsByHead_EmployeeId should return false")
    void testExistsByHeadEmployeeID_notExists() {
        boolean exists = departmentRepository.existsByHead_EmployeeId(1);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("count should return number of departments")
    void testCount() {
        long count = departmentRepository.count();

        assertThat(count).isGreaterThanOrEqualTo(3);
    }
}