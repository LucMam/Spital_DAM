package ro.dam.spital.repository;

import ro.dam.spital.domain.Medic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicRepository extends JpaRepository<Medic, Long> {
    List<Medic> findBySectieNume(String sectieNume);
}
