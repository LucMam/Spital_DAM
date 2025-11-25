package ro.dam.spital.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.dam.spital.domain.Medic;
import ro.dam.spital.domain.Pacient;
import ro.dam.spital.domain.Programare;
import ro.dam.spital.repository.ProgramareRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class ProgramareService {

    private final ProgramareRepository programareRepository;

    public ProgramareService(ProgramareRepository programareRepository) {
        this.programareRepository = programareRepository;
    }

    public Programare creeazaProgramare(Pacient pacient, Medic medic, LocalDateTime dataOra, String scop) {
        // nu permite programari in trecut
        if (dataOra.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Nu se poate crea programare in trecut");
        }

        // regula simpla: un medic nu poate avea mai mult de 10 programari intr-o zi
        LocalDate zi = dataOra.toLocalDate();
        LocalDateTime start = LocalDateTime.of(zi, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(zi, LocalTime.MAX);
        List<Programare> programariZi = programareRepository.findByDataOraBetween(start, end);
        long countMedic = programariZi.stream().filter(p -> p.getMedic()!=null && p.getMedic().getId()!=null && p.getMedic().getId().equals(medic.getId())).count();
        if (countMedic >= 10) {
            throw new IllegalStateException("Medic ocupat in acea zi (10 programari)"); 
        }

        Programare p = new Programare(dataOra, scop);
        pacient.adaugaProgramare(p);
        medic.adaugaProgramare(p);
        return programareRepository.save(p);
    }

    public List<Programare> gasesteProgramariMedic(Long medicId) {
        return programareRepository.findByMedicId(medicId);
    }
}
