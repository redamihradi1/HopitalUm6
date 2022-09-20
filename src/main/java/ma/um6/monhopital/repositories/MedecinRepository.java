package ma.um6.monhopital.repositories;


import ma.um6.monhopital.entities.Medecin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedecinRepository extends JpaRepository<Medecin,Long> {
    public Medecin findByNom(String n);
    Page<Medecin> findByNomContains(String kw, Pageable pageable);
}
