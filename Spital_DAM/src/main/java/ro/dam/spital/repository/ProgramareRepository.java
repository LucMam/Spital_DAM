package ro.dam.spital.repository;

import ro.dam.spital.domain.Programare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProgramareRepository extends JpaRepository<Programare, Long> {
    List<Programare> findByDataOraBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);
    List<Programare> findByMedicId(Long medicId);
}
