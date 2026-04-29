package cg.hospital.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import cg.hospital.entity.Nurse;

@RepositoryRestResource(collectionResourceRel = "nurses", path = "nurse")
public interface NurseRepository extends JpaRepository<Nurse, Integer> {
    // Spring Data REST auto-generates:
    // GET    /api/nurse          → findAll
    // GET    /api/nurse/{id}     → findById
    // POST   /api/nurse          → save (create)
    // PUT    /api/nurse/{id}     → save (full update)
    // DELETE /api/nurse/{id}     → delete
}