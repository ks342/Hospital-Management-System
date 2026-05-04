package cg.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import cg.hospital.entity.OnCall;
import cg.hospital.entity.OnCallId;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "onCalls", path = "on_call")
public interface OnCallRepository extends JpaRepository<OnCall, OnCallId> {

    // GET /api/on_call/search/findByNurse?nurse=101
    // This is what Page 2 uses to fetch all on-call records for a specific nurse
    List<OnCall> findByNurse_EmployeeId(Integer employeeId);
    
}