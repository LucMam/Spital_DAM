package ro.dam.spital.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.dam.spital.domain.Programare;
import ro.dam.spital.service.ProgramareService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/programari")
public class ProgramareController {

    private final ProgramareService programareService;

    public ProgramareController(ProgramareService programareService) {
        this.programareService = programareService;
    }

    @GetMapping
    public List<Programare> toateProgramarile() {
        return programareService.cautaProgramari(null, null, null, null, null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Programare> getById(@PathVariable Long id) {
        return programareService.cautaProgramari(null, null, null, null, null)
                .stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateProgramareRequest req) {
        try {
            Programare saved = programareService.creeazaProgramareByIds(
                    req.getPacientId(),
                    req.getMedicId(),
                    req.getDataOra(),
                    req.getScop()
            );
            return ResponseEntity.created(URI.create("/api/programari/" + saved.getId())).body(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateProgramareRequest req) {
        try {
            Programare updated = programareService.updateProgramare(
                    id,
                    req.getPacientId(),
                    req.getMedicId(),
                    req.getDataOra(),
                    req.getScop()
            );
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            programareService.deleteProgramare(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }


    public static class CreateProgramareRequest {
        private Long pacientId;
        private Long medicId;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
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

    public static class UpdateProgramareRequest {
        private Long pacientId;
        private Long medicId;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
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

    public static class ErrorResponse {
        private String mesaj;
        public ErrorResponse(String mesaj) { this.mesaj = mesaj; }
        public String getMesaj() { return mesaj; }
    }
}
