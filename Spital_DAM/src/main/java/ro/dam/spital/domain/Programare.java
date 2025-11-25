package ro.dam.spital.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Programare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Future
    private LocalDateTime dataOra;

    @NotBlank
    private String scop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "pacient_id")
    private Pacient pacient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "medic_id")
    private Medic medic;

    public Programare() {}

    public Programare(LocalDateTime dataOra, String scop) {
        this.dataOra = dataOra;
        this.scop = scop;
    }

    public Long getId() { return id; }
    public LocalDateTime getDataOra() { return dataOra; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }
    public String getScop() { return scop; }
    public void setScop(String scop) { this.scop = scop; }
    public Pacient getPacient() { return pacient; }
    public void setPacient(Pacient pacient) { this.pacient = pacient; }
    public Medic getMedic() { return medic; }
    public void setMedic(Medic medic) { this.medic = medic; }
}
