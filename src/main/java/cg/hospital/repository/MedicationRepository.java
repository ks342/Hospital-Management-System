package cg.hospital.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import cg.hospital.entity.Medication;

@RepositoryRestResource(collectionResourceRel = "medications", path = "medications")
public interface MedicationRepository extends JpaRepository<Medication, Integer> {
}