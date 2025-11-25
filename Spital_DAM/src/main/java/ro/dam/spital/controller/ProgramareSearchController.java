package ro.dam.spital.controller;

import org.springframework.web.bind.annotation.*;
import ro.dam.spital.domain.Programare;
import ro.dam.spital.service.ProgramareService;

import java.util.List;

@RestController
@RequestMapping("/api/programari")
public class ProgramareSearchController {

    private final ProgramareService programareService;

    public ProgramareSearchController(ProgramareService programareService) {
        this.programareService = programareService;
    }

    @GetMapping("/cauta")
    public List<Programare> cautaProgramari(
            @RequestParam(required = false) Long pacientId,
            @RequestParam(required = false) Long medicId,
            @RequestParam(required = false) Long sectieId,
            @RequestParam(required = false) String deLa,
            @RequestParam(required = false) String panaLa
    ) {
        return programareService.cautaProgramari(pacientId, medicId, sectieId, deLa, panaLa);
    }
}
