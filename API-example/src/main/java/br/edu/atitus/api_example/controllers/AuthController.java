package br.edu.atitus.api_example.controllers;

import java.security.Principal;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.*;

import br.edu.atitus.api_example.components.JwtUtil;
import br.edu.atitus.api_example.dtos.SigninDTO;
import br.edu.atitus.api_example.dtos.SignupDTO;
import br.edu.atitus.api_example.dtos.UpdateUserDTO;
import br.edu.atitus.api_example.entities.TypeUser;
import br.edu.atitus.api_example.entities.UserEntity;
import br.edu.atitus.api_example.services.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

	private final UserService service;
	private final AuthenticationConfiguration authConfig;

	public AuthController(UserService service, AuthenticationConfiguration authConfig) {
		this.service = service;
		this.authConfig = authConfig;
	}

	@PostMapping("/signup")
	public ResponseEntity<UserEntity> postSignup(@RequestBody SignupDTO dto) throws Exception {
		UserEntity user = new UserEntity();
		BeanUtils.copyProperties(dto, user);
		user.setType(TypeUser.Common);
		service.save(user);
		return ResponseEntity.status(201).body(user);
	}

	@PostMapping("/signin")
	public ResponseEntity<String> signin(@RequestBody SigninDTO dto) throws Exception {
		authConfig.getAuthenticationManager()
				.authenticate(new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));
		String jwt = JwtUtil.generateToken(dto.email());
		return ResponseEntity.ok(jwt);
	}

	@GetMapping("/me")
	public ResponseEntity<UserEntity> getUserProfile(Principal principal) {
		try {
			UserEntity user = service.findByEmail(principal.getName());
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/me")
	public ResponseEntity<UserEntity> updateUserProfile(Principal principal, @RequestBody UpdateUserDTO dto) {
		try {
			UserEntity user = service.findByEmail(principal.getName());

			if (dto.name() != null && !dto.name().isEmpty())
				user.setName(dto.name());
			if (dto.bio() != null)
				user.setBio(dto.bio());
			if (dto.avatar() != null)
				user.setAvatar(dto.avatar());

			// Usa o método específico de update para não revalidar senha
			service.updateProfile(user);

			return ResponseEntity.ok(user);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> exceptionHandler(Exception e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}