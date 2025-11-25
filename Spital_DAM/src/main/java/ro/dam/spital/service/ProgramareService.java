package ro.dam.spital.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.dam.spital.domain.Medic;
import ro.dam.spital.domain.Pacient;
import ro.dam.spital.domain.Programare;
import ro.dam.spital.repository.MedicRepository;
import ro.dam.spital.repository.PacientRepository;
import ro.dam.spital.repository.ProgramareRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class ProgramareService {

    private final ProgramareRepository programareRepository;
    private final PacientRepository pacientRepository;
    private final MedicRepository medicRepository;

    public ProgramareService(ProgramareRepository programareRepository,
                             PacientRepository pacientRepository,
                             MedicRepository medicRepository) {
        this.programareRepository = programareRepository;
        this.pacientRepository = pacientRepository;
        this.medicRepository = medicRepository;
    }

    // -------------------------------------------------------
    // 1) CREATE by Entities (folosit în WorkflowService)
    // -------------------------------------------------------
    public Programare creeazaProgramare(Pacient pacient, Medic medic, LocalDateTime dataOra, String scop) {

        if (dataOra.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Nu se poate crea programare în trecut.");
        }

        LocalDate zi = dataOra.toLocalDate();
        LocalDateTime start = LocalDateTime.of(zi, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(zi, LocalTime.MAX);

        List<Programare> programariZi =
                programareRepository.findByDataOraBetween(start, end);

        long countMedic = programariZi.stream()
                .filter(p -> p.getMedic() != null
                        && p.getMedic().getId().equals(medic.getId()))
                .count();

        if (countMedic >= 10)
            throw new IllegalStateException("Medic ocupat — are deja 10 programări în acea zi.");

        Programare p = new Programare(dataOra, scop);
        p.setPacient(pacient);
        p.setMedic(medic);

        return programareRepository.save(p);
    }

    // -------------------------------------------------------
    // 2) CREATE by IDs (folosit în ProgramareController)
    // -------------------------------------------------------
    public Programare creeazaProgramareByIds(Long pacientId, Long medicId, LocalDateTime dataOra, String scop) {

        Pacient pacient = pacientRepository.findById(pacientId)
                .orElseThrow(() -> new IllegalArgumentException("Pacient inexistent."));

        Medic medic = medicRepository.findById(medicId)
                .orElseThrow(() -> new IllegalArgumentException("Medic inexistent."));

        return creeazaProgramare(pacient, medic, dataOra, scop);
    }

    // -------------------------------------------------------
    // 3) UPDATE
    // -------------------------------------------------------
    public Programare updateProgramare(Long id, Long pacientId, Long medicId,
                                       LocalDateTime dataOra, String scop) {

        Programare programare = programareRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Programare inexistentă."));

        if (pacientId != null) {
            Pacient pacient = pacientRepository.findById(pacientId)
                    .orElseThrow(() -> new IllegalArgumentException("Pacient inexistent."));
            programare.setPacient(pacient);
        }

        if (medicId != null) {
            Medic medic = medicRepository.findById(medicId)
                    .orElseThrow(() -> new IllegalArgumentException("Medic inexistent."));
            programare.setMedic(medic);
        }

        if (dataOra != null) programare.setDataOra(dataOra);
        if (scop != null) programare.setScop(scop);

        return programareRepository.save(programare);
    }

    // -------------------------------------------------------
    // 4) DELETE
    // -------------------------------------------------------
    public void deleteProgramare(Long id) {
        if (!programareRepository.existsById(id)) {
            throw new IllegalArgumentException("Programare inexistentă.");
        }
        programareRepository.deleteById(id);
    }

    // -------------------------------------------------------
    // 5) CAUTARE GENERALĂ
    // -------------------------------------------------------
    public List<Programare> cautaProgramari(Long pacientId,
                                            Long medicId,
                                            Long sectieId,
                                            String deLa,
                                            String panaLa) {

        LocalDateTime d1 = (deLa == null || deLa.isBlank()) ? null : LocalDateTime.parse(deLa);
        LocalDateTime d2 = (panaLa == null || panaLa.isBlank()) ? null : LocalDateTime.parse(panaLa);

        final LocalDateTime fd1 = d1;
        final LocalDateTime fd2 = d2;

        List<Programare> lista = programareRepository.cautaFaraData(
                pacientId, medicId, sectieId
        );

        return lista.stream()
                .filter(p -> fd1 == null || !p.getDataOra().isBefore(fd1))
                .filter(p -> fd2 == null || !p.getDataOra().isAfter(fd2))
                .toList();
    }
}
