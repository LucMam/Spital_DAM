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
import java.util.Optional;

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

    // existing method kept (used by workflow)
    public Programare creeazaProgramare(Pacient pacient, Medic medic, LocalDateTime dataOra, String scop) {
        validateDateNotPast(dataOra);
        validateMedicLimitForDay(medic.getId(), dataOra, null);
        validatePatientNoSameTime(pacient.getId(), dataOra, null);

        Programare p = new Programare(dataOra, scop);
        pacient.adaugaProgramare(p);
        medic.adaugaProgramare(p);

        return programareRepository.save(p);
    }

    // helper used by controllers (create by ids)
    public Programare creeazaProgramareByIds(Long pacientId, Long medicId, LocalDateTime dataOra, String scop) {
        Pacient pacient = pacientRepository.findById(pacientId)
                .orElseThrow(() -> new IllegalArgumentException("Pacient inexistent"));
        Medic medic = medicRepository.findById(medicId)
                .orElseThrow(() -> new IllegalArgumentException("Medic inexistent"));

        return creeazaProgramare(pacient, medic, dataOra, scop);
    }

    // update programare
    public Programare updateProgramare(Long id, Long pacientId, Long medicId, LocalDateTime dataOra, String scop) {
        Programare existing = programareRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Programare inexistenta"));

        validateDateNotPast(dataOra);
        // when updating, exclude current programare from conflict checks
        validateMedicLimitForDay(medicId, dataOra, id);
        validatePatientNoSameTime(pacientId, dataOra, id);

        Pacient pacient = pacientRepository.findById(pacientId)
                .orElseThrow(() -> new IllegalArgumentException("Pacient inexistent"));
        Medic medic = medicRepository.findById(medicId)
                .orElseThrow(() -> new IllegalArgumentException("Medic inexistent"));

        // detach from previous relations (safe)
        if (existing.getPacient() != null && !existing.getPacient().getId().equals(pacientId)) {
            existing.getPacient().stergeProgramare(existing);
        }
        if (existing.getMedic() != null && !existing.getMedic().getId().equals(medicId)) {
            existing.getMedic().stergeProgramare(existing);
        }

        existing.setDataOra(dataOra);
        existing.setScop(scop);
        pacient.adaugaProgramare(existing);
        medic.adaugaProgramare(existing);

        return programareRepository.save(existing);
    }

    // delete programare
    public void deleteProgramare(Long id) {
        Programare p = programareRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Programare inexistenta"));
        // clean association
        if (p.getPacient() != null) p.getPacient().stergeProgramare(p);
        if (p.getMedic() != null) p.getMedic().stergeProgramare(p);
        programareRepository.deleteById(id);
    }

    // search existing (kept)
    public List<Programare> cautaProgramari(
            Long pacientId,
            Long medicId,
            Long sectieId,
            String deLa,
            String panaLa
    ) {
        LocalDateTime d1 = null;
        LocalDateTime d2 = null;

        try {
            if (deLa != null && !deLa.isBlank())
                d1 = LocalDateTime.parse(deLa);

            if (panaLa != null && !panaLa.isBlank())
                d2 = LocalDateTime.parse(panaLa);

        } catch (Exception e) {
            throw new IllegalArgumentException("Format invalid pentru data (yyyy-MM-ddTHH:mm)");
        }

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

    public List<Programare> gasesteProgramariMedic(Long medicId) {
        return programareRepository.findByMedicId(medicId);
    }

    // -------------------------
    // Validation helpers
    // -------------------------
    private void validateDateNotPast(LocalDateTime dataOra) {
        if (dataOra.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Nu se poate seta o dată/ora în trecut");
        }
    }

    /**
     * Verifică dacă medicul nu are deja >=10 programări în ziua respectivă.
     * excludeProgramareId - daca e update, ID-ul curent al programarii; poate fi null.
     */
    private void validateMedicLimitForDay(Long medicId, LocalDateTime dataOra, Long excludeProgramareId) {
        LocalDate zi = dataOra.toLocalDate();
        LocalDateTime start = LocalDateTime.of(zi, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(zi, LocalTime.MAX);

        List<Programare> programariZi = programareRepository.findByDataOraBetween(start, end);

        long countMedic = programariZi.stream()
                .filter(p -> p.getMedic() != null
                        && p.getMedic().getId() != null
                        && p.getMedic().getId().equals(medicId)
                        && (excludeProgramareId == null || !p.getId().equals(excludeProgramareId)))
                .count();

        if (countMedic >= 10) {
            throw new IllegalStateException("Medic ocupat in acea zi (10 programari)");
        }
    }

    /**
     * Verifică dacă pacientul nu are deja o programare exact la aceeasi dataOra (conflict exact).
     * excludeProgramareId - daca e update, ID-ul curent al programarii; poate fi null.
     */
    private void validatePatientNoSameTime(Long pacientId, LocalDateTime dataOra, Long excludeProgramareId) {
        LocalDate zi = dataOra.toLocalDate();
        LocalDateTime start = LocalDateTime.of(zi, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(zi, LocalTime.MAX);

        List<Programare> programariZi = programareRepository.findByDataOraBetween(start, end);

        boolean conflict = programariZi.stream()
                .filter(p -> p.getPacient() != null
                        && p.getPacient().getId() != null
                        && p.getPacient().getId().equals(pacientId)
                        && (excludeProgramareId == null || !p.getId().equals(excludeProgramareId)))
                .anyMatch(p -> p.getDataOra().isEqual(dataOra));

        if (conflict) {
            throw new IllegalStateException("Pacientul are deja o programare la aceeasi ora");
        }
    }
}
