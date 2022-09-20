package ma.um6.monhopital.repositories;

import ma.um6.monhopital.entities.Medecin;
import ma.um6.monhopital.entities.Personnel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonnelRepository extends JpaRepository<Personnel,Long> {
    Page<Personnel> findByNomContains(String kw, Pageable pageable);
}
