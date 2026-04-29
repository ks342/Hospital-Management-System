package cg.hospital.repository;

import cg.hospital.entity.AffiliatedWith;
import cg.hospital.entity.AffiliatedWithId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "affiliatedWith", path = "affiliated-with")
public interface AffiliatedWithRepository extends JpaRepository<AffiliatedWith, AffiliatedWithId> {

    List<AffiliatedWith> findByDepartment_DepartmentID(
            @Param("departmentID") Integer departmentID);

    List<AffiliatedWith> findByPhysician_EmployeeId(
            @Param("employeeId") Integer employeeId);

    long countByDepartment_DepartmentID(
            @Param("departmentID") Integer departmentID);

    List<AffiliatedWith> findByDepartment_DepartmentIDAndPrimaryAffiliationTrue(
            @Param("departmentID") Integer departmentID);

    boolean existsByPhysician_EmployeeIdAndDepartment_DepartmentID(
            @Param("employeeId") Integer employeeId,
            @Param("departmentID") Integer departmentID);
}