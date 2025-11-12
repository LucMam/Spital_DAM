package ro.dam.spital.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.dam.spital.domain.Pacient;
import ro.dam.spital.repository.PacientRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PacientService {

    private final PacientRepository pacientRepository;

    public PacientService(PacientRepository pacientRepository) {
        this.pacientRepository = pacientRepository;
    }

    public Pacient salveaza(Pacient p) {
        return pacientRepository.save(p);
    }

    public Optional<Pacient> gasesteDupaId(Long id) {
        return pacientRepository.findById(id);
    }

    public List<Pacient> cautaDupaNume(String nume) {
        return pacientRepository.findByNumeContainingIgnoreCase(nume);
    }
}
