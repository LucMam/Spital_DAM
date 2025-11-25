package ro.dam.spital.controller;

import org.springframework.web.bind.annotation.*;
import ro.dam.spital.dto.workflow.ProgramareRequestDTO;
import ro.dam.spital.dto.workflow.ProgramareResponseDTO;
import ro.dam.spital.service.workflow.WorkflowService;

@RestController
@RequestMapping("/api/workflow")
public class ProgramareWorkflowController {

    private final WorkflowService workflowService;

    public ProgramareWorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping("/programare")
    public ProgramareResponseDTO proceseaza(@RequestBody ProgramareRequestDTO dto) {
        return workflowService.proceseazaWorkflow(dto);
    }
}
