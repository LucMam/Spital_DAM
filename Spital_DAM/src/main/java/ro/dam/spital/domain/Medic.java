package ro.dam.spital.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Medic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nume;

    @NotBlank
    private String specializare;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name = "sectie_id")
    private Sectie sectie;



    @OneToMany(mappedBy = "medic", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Programare> programari = new ArrayList<>();

    public Medic() {}

    public Medic(String nume, String specializare) {
        this.nume = nume;
        this.specializare = specializare;
    }

    public Long getId() { return id; }
    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public String getSpecializare() { return specializare; }
    public void setSpecializare(String specializare) { this.specializare = specializare; }
    public Sectie getSectie() { return sectie; }
    public void setSectie(Sectie sectie) { this.sectie = sectie; }
    public List<Programare> getProgramari() { return programari; }

    public void adaugaProgramare(Programare p) {
        p.setMedic(this);
        programari.add(p);
    }

    public void stergeProgramare(Programare p) {
        programari.remove(p);
        p.setMedic(null);
    }
}
