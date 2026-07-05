package com.example.demo;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BlissCafeteriaApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(BlissCafeteriaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Seed an admin account automatically if the table is empty
		if (userRepository.findByUsername("admin").isEmpty()) {
			User admin = new User();
			admin.setUsername("admin");
			// Hashes the plain text string 'admin' into a secure BCrypt string before saving
			admin.setPassword(passwordEncoder.encode("admin"));
			userRepository.save(admin);
			System.out.println("✅ Secure database admin user seeded successfully!");
		}
	}
}