package com.sp.ScientificPublications.service.logic;

import com.sp.ScientificPublications.dto.UserDTO;
import com.sp.ScientificPublications.exception.ApiAuthException;
import com.sp.ScientificPublications.exception.ApiBadRequestException;
import com.sp.ScientificPublications.models.Author;
import com.sp.ScientificPublications.models.Authority;
import com.sp.ScientificPublications.models.Editor;
import com.sp.ScientificPublications.models.User;
import com.sp.ScientificPublications.repository.AuthorRepository;
import com.sp.ScientificPublications.repository.AuthorityRepository;
import com.sp.ScientificPublications.repository.EditorRepository;
import com.sp.ScientificPublications.repository.UserRepository;
import com.sp.ScientificPublications.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AuthenticationService {

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private EditorRepository editorRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public UserDTO login(String email, String password) {
		UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(email, password);

		Authentication auth;
		try {
			auth = authManager.authenticate(loginToken);
		} catch (BadCredentialsException ex) {
			throw new ApiAuthException("Invalid credentials.");
		}

		String token = jwtUtils.generateToken(auth.getName());
		SecurityContextHolder.getContext().setAuthentication(auth);
		UserDTO user = new UserDTO(userRepository.findByEmail(email).get());
		user.setToken(token);

		return user;
	}

	public UserDTO register(UserDTO userDTO) {
		Optional<User> optionalUser = userRepository.findByEmail(userDTO.getEmail());
		if (!optionalUser.isPresent()) {
			Author author = new Author();
			author.setEmail(userDTO.getEmail());
			author.setPassword(passwordEncoder.encode(userDTO.getPassword()));
			author.setFirstname(userDTO.getFirstname());
			author.setLastname(userDTO.getLastname());
			author.getCollectionOfAuthorities().add(
					authorityRepository.findByName("ROLE_AUTHOR").get());
			authorRepository.save(author);
			return new UserDTO(author);
		} else {
			throw new ApiBadRequestException("Email is not available.");
		}
	}

	public void changePassword(String newPassword, String oldPassword) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmail(email).get();

		if (passwordEncoder.matches(oldPassword, user.getPassword())) {
			newPassword = passwordEncoder.encode(newPassword);
			user.setPassword(newPassword);
			userRepository.save(user);
		} else {
			throw new ApiAuthException("Please enter current password to verify ownership of account.");
		}
	}

	public Editor getCurrentEditor() {
		Optional<Editor> optionalEditor = editorRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		if (optionalEditor.isPresent()) {
			return optionalEditor.get();
		} else {
			throw new ApiAuthException("You are not authorized as editor.");
		}
	}

	public Author getCurrentAuthor() {
		Optional<Author> optionalAuthor = authorRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		if (optionalAuthor.isPresent()) {
			return optionalAuthor.get();
		} else {
			throw new ApiAuthException("You are not authorized as author.");
		}
	}
}