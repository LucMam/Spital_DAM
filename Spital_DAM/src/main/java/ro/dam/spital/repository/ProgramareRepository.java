package ro.dam.spital.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.dam.spital.domain.Programare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProgramareRepository extends JpaRepository<Programare, Long> {

    List<Programare> findByDataOraBetween(LocalDateTime start, LocalDateTime end);



    @Query("""
        SELECT p FROM Programare p
        WHERE (:pacientId IS NULL OR p.pacient.id = :pacientId)
        AND (:medicId IS NULL OR p.medic.id = :medicId)
        AND (:sectieId IS NULL OR p.medic.sectie.id = :sectieId)
        """)
    List<Programare> cauta(
            @Param("pacientId") Long pacientId,
            @Param("medicId") Long medicId,
            @Param("sectieId") Long sectieId
    );
}




