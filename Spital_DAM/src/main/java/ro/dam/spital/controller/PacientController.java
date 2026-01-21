package ro.dam.spital.controller;

import org.springframework.web.bind.annotation.*;
import ro.dam.spital.domain.Pacient;
import ro.dam.spital.repository.PacientRepository;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/pacienti")
public class PacientController {

    private final PacientRepository pacientRepository;

    public PacientController(PacientRepository pacientRepository) {
        this.pacientRepository = pacientRepository;
    }

    @GetMapping
    public List<Pacient> getAll() {
        return pacientRepository.findAll();
    }

    @PostMapping
    public Pacient create(@RequestBody Pacient pacient) {
        return pacientRepository.save(pacient);
    }
}
