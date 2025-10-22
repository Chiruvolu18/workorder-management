package com.uas.auth.config;

import com.uas.auth.model.User;
import com.uas.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	// Constructor injection
	public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) throws Exception {
		// Check if test user already exists to avoid duplicates
		if (userRepository.findByUsername("testuser").isEmpty()) {
			User testUser = new User();
			testUser.setUsername("testuser");
			testUser.setPassword(passwordEncoder.encode("password123"));
			testUser.setRole("FIELD_CREW");

			userRepository.save(testUser);

			System.out.println("Test user created successfully!");
			System.out.println("Username: testuser");
			System.out.println("Password: password123");
			System.out.println("Role: FIELD_CREW");
		} else {
			System.out.println("Test user already exists in database.");
		}
	}
}
