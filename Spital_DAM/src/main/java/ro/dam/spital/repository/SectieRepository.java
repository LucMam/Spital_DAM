package ro.dam.spital.repository;

import ro.dam.spital.domain.Sectie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectieRepository extends JpaRepository<Sectie, Long> {
}
