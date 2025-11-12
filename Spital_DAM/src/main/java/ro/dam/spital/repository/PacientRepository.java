package ro.dam.spital.repository;

import ro.dam.spital.domain.Pacient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacientRepository extends JpaRepository<Pacient, Long> {
    List<Pacient> findByNumeContainingIgnoreCase(String nume);
}
