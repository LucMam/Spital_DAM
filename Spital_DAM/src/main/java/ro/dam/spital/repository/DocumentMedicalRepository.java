package ro.dam.spital.repository;

import ro.dam.spital.domain.DocumentMedical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentMedicalRepository extends JpaRepository<DocumentMedical, Long> {
    List<DocumentMedical> findByPacientId(Long pacientId);
}
