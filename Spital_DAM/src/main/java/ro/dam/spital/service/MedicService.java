package ro.dam.spital.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.dam.spital.domain.Medic;
import ro.dam.spital.repository.MedicRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MedicService {

    private final MedicRepository medicRepository;

    public MedicService(MedicRepository medicRepository) {
        this.medicRepository = medicRepository;
    }

    public Medic salveaza(Medic m) {
        return medicRepository.save(m);
    }

    public Optional<Medic> gasesteDupaId(Long id) {
        return medicRepository.findById(id);
    }

    public List<Medic> gasesteDupaSectie(String sectieNume) {
        return medicRepository.findBySectieNume(sectieNume);
    }
}
