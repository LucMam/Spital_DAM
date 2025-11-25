package ro.dam.spital.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DocumentMedical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titlu;

    private LocalDate dataEmiterii;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pacient_id")
    @JsonIgnore
    private Pacient pacient;

    public DocumentMedical() {}

    public DocumentMedical(String titlu, LocalDate dataEmiterii) {
        this.titlu = titlu;
        this.dataEmiterii = dataEmiterii;
    }

    public Long getId() { return id; }
    public String getTitlu() { return titlu; }
    public void setTitlu(String titlu) { this.titlu = titlu; }
    public LocalDate getDataEmiterii() { return dataEmiterii; }
    public void setDataEmiterii(LocalDate dataEmiterii) { this.dataEmiterii = dataEmiterii; }
    public Pacient getPacient() { return pacient; }
    public void setPacient(Pacient pacient) { this.pacient = pacient; }
}
