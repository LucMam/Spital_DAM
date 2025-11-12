package ro.dam.spital;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ro.dam.spital.domain.*;
import ro.dam.spital.repository.MedicRepository;
import ro.dam.spital.repository.PacientRepository;
import ro.dam.spital.repository.SectieRepository;
import ro.dam.spital.service.DocumentService;
import ro.dam.spital.service.ProgramareService;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class BusinessRulesTests {

    @Autowired private PacientRepository pacientRepo;
    @Autowired private MedicRepository medicRepo;
    @Autowired private ProgramareService programareService;
    @Autowired private SectieRepository sectieRepo;
    @Autowired private DocumentService documentService;


    @Test
    void testProgramareInTrecutNotAllowed() {
        Pacient p = pacientRepo.save(
                new Pacient("Test", java.time.LocalDate.of(1990,1,1), ro.dam.spital.domain.Sex.NERELAVANT)
        );

        Sectie s = new Sectie("Cardiologie");
        sectieRepo.save(s);

        Medic m = new Medic("Dr Test", "Cardiologie");
        s.adaugaMedic(m);
        medicRepo.save(m);

        LocalDateTime trecut = LocalDateTime.now().minusDays(1);
        assertThrows(IllegalArgumentException.class,
                () -> programareService.creeazaProgramare(p, m, trecut, "control"));
    }
    @Test
    void testPacientNuPoateAveaPeste20Documente() {
        Pacient p = pacientRepo.save(new Pacient("Pacient Documente", LocalDate.of(1980,1,1), Sex.FEMININ));

        // 20 documente valide
        for (int i = 1; i <= 20; i++) {
            documentService.salveazaDocument(p, new DocumentMedical("Doc #" + i, LocalDate.now()));
        }

        // A 21-a trebuie să dea eroare
        assertThrows(IllegalStateException.class,
                () -> documentService.salveazaDocument(p, new DocumentMedical("Doc depășit", LocalDate.now())));
    }


}
