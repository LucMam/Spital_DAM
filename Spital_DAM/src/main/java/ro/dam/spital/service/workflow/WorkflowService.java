package ro.dam.spital.service.workflow;

import org.springframework.stereotype.Service;
import ro.dam.spital.domain.Medic;
import ro.dam.spital.domain.Pacient;
import ro.dam.spital.dto.workflow.ProgramareRequestDTO;
import ro.dam.spital.dto.workflow.ProgramareResponseDTO;
import ro.dam.spital.repository.MedicRepository;
import ro.dam.spital.repository.PacientRepository;
import ro.dam.spital.service.ProgramareService;

@Service
public class WorkflowService {

    private final PacientRepository pacientRepo;
    private final MedicRepository medicRepo;
    private final ProgramareService programareService;

    public WorkflowService(PacientRepository pacientRepo,
                           MedicRepository medicRepo,
                           ProgramareService programareService) {
        this.pacientRepo = pacientRepo;
        this.medicRepo = medicRepo;
        this.programareService = programareService;
    }

    public ProgramareResponseDTO proceseazaWorkflow(ProgramareRequestDTO dto) {

        Pacient pacient = pacientRepo.findById(dto.getPacientId())
                .orElse(null);
        if (pacient == null)
            return new ProgramareResponseDTO(false, "Pacient inexistent");

        Medic medic = medicRepo.findById(dto.getMedicId())
                .orElse(null);
        if (medic == null)
            return new ProgramareResponseDTO(false, "Medic inexistent");

        try {
            programareService.creeazaProgramare(
                    pacient,
                    medic,
                    dto.getDataOra(),
                    dto.getScop()
            );

            return new ProgramareResponseDTO(true, "Programare creata cu succes!");

        } catch (Exception e) {
            return new ProgramareResponseDTO(false, "Eroare: " + e.getMessage());
        }
    }
}
