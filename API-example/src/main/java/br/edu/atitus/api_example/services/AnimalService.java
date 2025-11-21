package br.edu.atitus.api_example.services;

import br.edu.atitus.api_example.entities.AnimalEntity;
import br.edu.atitus.api_example.entities.UserEntity;
import br.edu.atitus.api_example.repositories.AnimalRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public AnimalEntity cadastrar(AnimalEntity animal) {
        animal.setAdotado(false);
        return animalRepository.save(animal);
    }

    public List<AnimalEntity> listarDisponiveis() {
        return animalRepository.findByIsAdotadoFalse();
    }

    public AnimalEntity adotar(UUID animalId, UserEntity adotante) {
        AnimalEntity animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new RuntimeException("Animal não encontrado."));

        if (animal.isAdotado()) {
            throw new RuntimeException("Este animal já foi adotado!");
        }

        animal.setAdotado(true);
        animal.setAdotante(adotante);

        return animalRepository.save(animal);
    }
}