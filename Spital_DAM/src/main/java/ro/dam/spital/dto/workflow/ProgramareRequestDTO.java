package ro.dam.spital.dto.workflow;

import java.time.LocalDateTime;

public class ProgramareRequestDTO {
    private Long pacientId;
    private Long medicId;
    private LocalDateTime dataOra;
    private String scop;

    public Long getPacientId() { return pacientId; }
    public void setPacientId(Long pacientId) { this.pacientId = pacientId; }

    public Long getMedicId() { return medicId; }
    public void setMedicId(Long medicId) { this.medicId = medicId; }

    public LocalDateTime getDataOra() { return dataOra; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }

    public String getScop() { return scop; }
    public void setScop(String scop) { this.scop = scop; }
}
