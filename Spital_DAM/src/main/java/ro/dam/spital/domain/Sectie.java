package ro.dam.spital.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Sectie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nume;

    @OneToMany(mappedBy = "sectie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medic> medici = new ArrayList<>();

    public Sectie() {}

    public Sectie(String nume) {
        this.nume = nume;
    }

    public Long getId() { return id; }
    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public List<Medic> getMedici() { return medici; }

    public void adaugaMedic(Medic m) {
        m.setSectie(this);
        medici.add(m);
    }

    public void stergeMedic(Medic m) {
        medici.remove(m);
        m.setSectie(null);
    }
}
