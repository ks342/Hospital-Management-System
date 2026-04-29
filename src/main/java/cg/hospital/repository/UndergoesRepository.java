package cg.hospital.repository;

import cg.hospital.entity.Undergoes;
import cg.hospital.entity.UndergoesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "undergoes", collectionResourceRel = "undergoes")
public interface UndergoesRepository extends JpaRepository<Undergoes, UndergoesId> {

    // Derived query: SELECT * FROM Undergoes WHERE Procedures = ?
    // No JPQL — Spring derives from method name
    // Used in Page 3 Tab 2: fetch all undergoes records for a given procedure code
    List<Undergoes> findByProcedures(Integer procedures);
}