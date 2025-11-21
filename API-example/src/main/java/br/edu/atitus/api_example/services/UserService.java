package br.edu.atitus.api_example.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.atitus.api_example.entities.UserEntity;
import br.edu.atitus.api_example.repositories.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService implements UserDetailsService {

	private final UserRepository repository;
	private final PasswordEncoder encoder;

	private static final String EMAIL_PATTERN = "^.+@.+\\..+?(\\..+)?$";
	private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).*$";

	public UserService(UserRepository repository, PasswordEncoder encoder) {
		this.repository = repository;
		this.encoder = encoder;
	}

	private boolean isValidEmail(String email) {
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	private boolean isSecurePassword(String password) {
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	public UserEntity save(UserEntity user) throws Exception {
		if (user == null)
			throw new Exception("Objeto Nulo");

		if (user.getName() == null || user.getName().isEmpty())
			throw new Exception("Nome Inválido");
		user.setName(user.getName().trim());

		if (user.getEmail() == null || user.getEmail().isEmpty())
			throw new Exception("E-mail Inválido");
		user.setEmail(user.getEmail().trim());

		if (!isValidEmail(user.getEmail()))
			throw new Exception("E-mail inválido.");

		if (user.getPassword() == null || user.getPassword().length() < 8)
			throw new Exception("Senha deve ter no mínimo 8 caracteres.");

		if (!isSecurePassword(user.getPassword()))
			throw new Exception("Senha fraca.");

		if (repository.existsByEmail(user.getEmail()))
			throw new Exception("E-mail já cadastrado.");

		user.setPassword(encoder.encode(user.getPassword()));

		return repository.save(user);
	}

	// Novo método para atualizar perfil sem revalidar senha/email
	public UserEntity updateProfile(UserEntity user) {
		return repository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return repository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
	}

	public UserEntity findByEmail(String email) {
		return repository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
	}
}