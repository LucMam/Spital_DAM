package ro.dam.spital;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import ro.dam.spital.domain.*;
import ro.dam.spital.repository.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class SpitalDamApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpitalDamApplication.class, args);
    }


    private void resetSequences() {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Spital",
                "postgres",
                "postgres"
        )) {
            Statement stmt = conn.createStatement();

            stmt.execute("ALTER SEQUENCE pacient_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE medic_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE sectie_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE programare_id_seq RESTART WITH 1");
            stmt.execute("ALTER SEQUENCE document_medical_id_seq RESTART WITH 1");

            System.out.println("Secvențele au fost resetate!");
        } catch (Exception e) {
            System.err.println("Eroare resetare secvente:");
            e.printStackTrace();
        }
    }

    @Bean
    @Profile("!test")
    CommandLineRunner initDatabase(
            SectieRepository sectieRepo,
            MedicRepository medicRepo,
            PacientRepository pacientRepo,
            ProgramareRepository programareRepo,
            DocumentMedicalRepository documentRepo
    ) {
        return args -> {

            System.out.println("Se populeaza baza de date Spital...");


            programareRepo.deleteAll();
            documentRepo.deleteAll();
            medicRepo.deleteAll();
            pacientRepo.deleteAll();
            sectieRepo.deleteAll();


            resetSequences();


            Sectie cardiologie = new Sectie("Cardiologie");
            Sectie pediatrie = new Sectie("Pediatrie");
            Sectie ortopedie = new Sectie("Ortopedie");
            Sectie neurologie = new Sectie("Neurologie");
            sectieRepo.save(cardiologie);
            sectieRepo.save(pediatrie);
            sectieRepo.save(ortopedie);
            sectieRepo.save(neurologie);


            Medic m1 = new Medic("Dr. Popa Ioan", "Cardiologie");
            Medic m2 = new Medic("Dr. Georgescu Ana", "Pediatrie");
            Medic m3 = new Medic("Dr. Ionescu Radu", "Neurologie");
            Medic m4 = new Medic("Dr. Lupu Mihai", "Ortopedie");
            m1.setSectie(cardiologie);
            m2.setSectie(pediatrie);
            m3.setSectie(neurologie);
            m4.setSectie(ortopedie);
            medicRepo.save(m1);
            medicRepo.save(m2);
            medicRepo.save(m3);
            medicRepo.save(m4);


            Pacient p1 = new Pacient("Ion Popescu", LocalDate.of(1985, 5, 10), Sex.MASCULIN);
            Pacient p2 = new Pacient("Maria Ionescu", LocalDate.of(1992, 3, 14), Sex.FEMININ);
            Pacient p3 = new Pacient("Andrei Vasilescu", LocalDate.of(2000, 8, 25), Sex.MASCULIN);
            Pacient p4 = new Pacient("Elena Dumitru", LocalDate.of(1978, 11, 2), Sex.FEMININ);
            pacientRepo.save(p1);
            pacientRepo.save(p2);
            pacientRepo.save(p3);
            pacientRepo.save(p4);


            DocumentMedical d1 = new DocumentMedical("Rezultat Analize Sânge", LocalDate.of(2024, 1, 10));
            DocumentMedical d2 = new DocumentMedical("Raport Radiografie", LocalDate.of(2024, 2, 5));
            DocumentMedical d3 = new DocumentMedical("Certificat Concediu Medical", LocalDate.of(2024, 3, 1));
            DocumentMedical d4 = new DocumentMedical("Rețetă Medicală", LocalDate.of(2024, 4, 15));
            d1.setPacient(p1);
            d2.setPacient(p2);
            d3.setPacient(p3);
            d4.setPacient(p1);
            documentRepo.save(d1);
            documentRepo.save(d2);
            documentRepo.save(d3);
            documentRepo.save(d4);


            Programare pr1 = new Programare(LocalDateTime.now().plusDays(1), "Control periodic");
            Programare pr2 = new Programare(LocalDateTime.now().plusDays(2), "Consult neurologic");
            Programare pr3 = new Programare(LocalDateTime.now().plusDays(3), "Verificare analize");
            Programare pr4 = new Programare(LocalDateTime.now().plusDays(5), "Control ortopedic");

            pr1.setPacient(p1);
            pr1.setMedic(m1);
            pr2.setPacient(p3);
            pr2.setMedic(m3);
            pr3.setPacient(p2);
            pr3.setMedic(m2);
            pr4.setPacient(p4);
            pr4.setMedic(m4);
            programareRepo.save(pr1);
            programareRepo.save(pr2);
            programareRepo.save(pr3);
            programareRepo.save(pr4);

            System.out.println("Baza de date a fost populată cu succes!");
        };
    }
}
