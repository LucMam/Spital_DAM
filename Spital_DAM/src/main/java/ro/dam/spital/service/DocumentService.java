package ro.dam.spital.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.dam.spital.domain.DocumentMedical;
import ro.dam.spital.domain.Pacient;
import ro.dam.spital.repository.DocumentMedicalRepository;

import java.util.List;

@Service
@Transactional
public class DocumentService {

    private final DocumentMedicalRepository documentRepo;

    public DocumentService(DocumentMedicalRepository documentRepo) {
        this.documentRepo = documentRepo;
    }

    public DocumentMedical salveazaDocument(Pacient pacient, DocumentMedical doc) {
        if (pacient.getDocumente().size() >= 20) {
            throw new IllegalStateException("Pacientul are deja 20 de documente");
        }
        pacient.adaugaDocument(doc);
        return documentRepo.save(doc);
    }

    public List<DocumentMedical> gasesteDocumentePacient(Long pacientId) {
        return documentRepo.findByPacientId(pacientId);
    }
}
