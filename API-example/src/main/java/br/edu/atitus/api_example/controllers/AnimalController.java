package br.edu.atitus.api_example.controllers;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.edu.atitus.api_example.entities.AnimalEntity;
import br.edu.atitus.api_example.entities.UserEntity;
import br.edu.atitus.api_example.repositories.UserRepository;
import br.edu.atitus.api_example.services.AnimalService;
import br.edu.atitus.api_example.services.UserService;

@RestController
@RequestMapping("/api/animais")
@CrossOrigin(origins = "*") // Permite acesso de qualquer lugar (Koyeb/Netlify)
public class AnimalController {

    private final UserService userService;
    private final AnimalService animalService;

    // O Spring injeta automaticamente (não precisa de @Autowired no construtor a
    // partir do Spring 4.3)
    public AnimalController(UserService userService, AnimalService animalService) {
        this.userService = userService;
        this.animalService = animalService;
    }

    // Endpoint de Cadastro: Recebe JSON ("animal") e Arquivo ("foto")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> cadastrar(
            @RequestPart(value = "animal") AnimalEntity animal,
            @RequestPart(value = "foto", required = false) MultipartFile foto) {
        try {
            AnimalEntity novoAnimal = animalService.cadastrar(animal, foto);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoAnimal);
        } catch (Exception e) {
            e.printStackTrace(); // Imprime o erro no console do Backend
            return ResponseEntity.badRequest().body("Erro ao cadastrar: " + e.getMessage());
        }
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<AnimalEntity>> listarDisponiveis() {
        return ResponseEntity.ok(animalService.listarDisponiveis());
    }

    @PatchMapping("/{id}/adotar")
    public ResponseEntity<?> adotar(
            @PathVariable UUID id,
            Principal principal) {
        try {
            // Busca usuário pelo email do token JWT
            String email = principal.getName();
            UserEntity adotante = userService.findByEmail(email);

            AnimalEntity adotado = animalService.adotar(id, adotante);
            return ResponseEntity.ok(adotado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}