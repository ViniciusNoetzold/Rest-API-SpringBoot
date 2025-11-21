package br.edu.atitus.api_example.services;

import br.edu.atitus.api_example.entities.AnimalEntity;
import br.edu.atitus.api_example.entities.UserEntity;
import br.edu.atitus.api_example.repositories.AnimalRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final FileStorageService fileStorageService;

    public AnimalService(AnimalRepository animalRepository, FileStorageService fileStorageService) {
        this.animalRepository = animalRepository;
        this.fileStorageService = fileStorageService;
    }

    public AnimalEntity cadastrar(AnimalEntity animal, MultipartFile foto) {
        // Validação básica
        if (animal == null) {
            throw new RuntimeException("Dados do animal inválidos (Objeto nulo).");
        }
        if (animal.getNome() == null || animal.getNome().isEmpty()) {
            throw new RuntimeException("O nome do animal é obrigatório.");
        }

        // Lógica de Foto Opcional: Só tenta salvar se o arquivo existir
        if (foto != null && !foto.isEmpty()) {
            try {
                String fotoPath = fileStorageService.salvarFoto(foto);
                animal.setFotoPath(fotoPath);
            } catch (Exception e) {
                // Se der erro ao salvar foto, loga o erro mas não impede o cadastro (opcional)
                System.err.println("Erro ao salvar foto: " + e.getMessage());
                // throw new RuntimeException("Erro ao salvar foto", e); // Descomente se quiser
                // que falhe
            }
        } else {
            animal.setFotoPath(null);
        }

        animal.setAdotado(false);

        return animalRepository.save(animal);
    }

    public List<AnimalEntity> listarDisponiveis() {
        return animalRepository.findByIsAdotadoFalse();
    }

    public AnimalEntity adotar(UUID animalId, UserEntity adotante) {
        AnimalEntity animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new RuntimeException("Animal não encontrado com ID: " + animalId));

        if (animal.isAdotado()) {
            throw new RuntimeException("Este animal já foi adotado!");
        }

        animal.setAdotado(true);
        animal.setAdotante(adotante);

        return animalRepository.save(animal);
    }
}