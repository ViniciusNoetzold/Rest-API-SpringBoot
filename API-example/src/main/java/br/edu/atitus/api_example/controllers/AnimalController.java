package br.edu.atitus.api_example.controllers;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.atitus.api_example.dtos.AnimalDTO;
import br.edu.atitus.api_example.entities.AnimalEntity;
import br.edu.atitus.api_example.entities.UserEntity;
import br.edu.atitus.api_example.services.AnimalService;
import br.edu.atitus.api_example.services.UserService;

@RestController
@RequestMapping("/api/animais")
@CrossOrigin(origins = "*")
public class AnimalController {

    private final UserService userService;
    private final AnimalService animalService;

    public AnimalController(UserService userService, AnimalService animalService) {
        this.userService = userService;
        this.animalService = animalService;
    }

    @PostMapping
    public ResponseEntity<AnimalEntity> cadastrar(@RequestBody AnimalDTO dto) {
        AnimalEntity animal = new AnimalEntity();
        BeanUtils.copyProperties(dto, animal);

        // Define valores padrão se não vierem
        if (animal.getNome() == null)
            animal.setNome("Pet sem nome");

        AnimalEntity novoAnimal = animalService.cadastrar(animal);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAnimal);
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<AnimalEntity>> listarDisponiveis() {
        return ResponseEntity.ok(animalService.listarDisponiveis());
    }

    @PatchMapping("/{id}/adotar")
    public ResponseEntity<AnimalEntity> adotar(@PathVariable UUID id, Principal principal) {
        UserEntity adotante = userService.findByEmail(principal.getName());
        AnimalEntity adotado = animalService.adotar(id, adotante);
        return ResponseEntity.ok(adotado);
    }
}