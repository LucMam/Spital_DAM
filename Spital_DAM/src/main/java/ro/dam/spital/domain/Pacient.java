package ro.dam.spital.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pacient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nume;

    @Past
    private LocalDate dataNasterii;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @OneToMany(mappedBy = "pacient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentMedical> documente = new ArrayList<>();

    @OneToMany(mappedBy = "pacient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Programare> programari = new ArrayList<>();

    public Pacient() {}

    public Pacient(String nume, LocalDate dataNasterii, Sex sex) {
        this.nume = nume;
        this.dataNasterii = dataNasterii;
        this.sex = sex;
    }

    public Long getId() { return id; }
    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public LocalDate getDataNasterii() { return dataNasterii; }
    public void setDataNasterii(LocalDate dataNasterii) { this.dataNasterii = dataNasterii; }
    public Sex getSex() { return sex; }
    public void setSex(Sex sex) { this.sex = sex; }

    public List<DocumentMedical> getDocumente() { return documente; }
    public List<Programare> getProgramari() { return programari; }

    public void adaugaDocument(DocumentMedical d) {
        d.setPacient(this);
        documente.add(d);
    }

    public void stergeDocument(DocumentMedical d) {
        documente.remove(d);
        d.setPacient(null);
    }

    public void adaugaProgramare(Programare p) {
        p.setPacient(this);
        programari.add(p);
    }

    public void stergeProgramare(Programare p) {
        programari.remove(p);
        p.setPacient(null);
    }
}
