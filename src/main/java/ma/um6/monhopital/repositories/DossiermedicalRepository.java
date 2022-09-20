package ma.um6.monhopital.repositories;

import ma.um6.monhopital.entities.DossierMedical;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DossiermedicalRepository extends JpaRepository<DossierMedical,Long> {
    Page<DossierMedical> findByRapportContains(String kw, Pageable pageable);
    DossierMedical findByPatientNom(String x);
}
