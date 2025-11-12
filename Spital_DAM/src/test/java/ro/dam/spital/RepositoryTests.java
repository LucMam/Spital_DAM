package ro.dam.spital;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ro.dam.spital.domain.Pacient;
import ro.dam.spital.domain.Sex;
import ro.dam.spital.repository.PacientRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class RepositoryTests {

    @Autowired
    private PacientRepository pacientRepository;

    @Test
    void testPersistPacient() {
        Pacient p = new Pacient("Ion Popescu", LocalDate.of(1985,5,10), Sex.MASCULIN);
        pacientRepository.save(p);

        List<Pacient> list = pacientRepository.findByNumeContainingIgnoreCase("Ion");
        assertThat(list).isNotEmpty();
    }
}
