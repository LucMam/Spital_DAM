package ro.dam.spital.controller;

import org.springframework.web.bind.annotation.*;
import ro.dam.spital.domain.Medic;
import ro.dam.spital.repository.MedicRepository;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/medici")
public class MedicController {

    private final MedicRepository medicRepository;

    public MedicController(MedicRepository medicRepository) {
        this.medicRepository = medicRepository;
    }

    @GetMapping
    public List<Medic> getAll() {
        return medicRepository.findAll();
    }

    @PostMapping
    public Medic create(@RequestBody Medic medic) {
        return medicRepository.save(medic);
    }
}
