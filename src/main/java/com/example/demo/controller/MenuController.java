package com.example.demo.controller;

import com.example.demo.menu_item.MenuItem;
import com.example.demo.repository.MenuRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.user.User;
import com.example.demo.bliss.service.FileStorageService;
import com.example.demo.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
@CrossOrigin(origins = {"http://localhost:4200", "https://onrender.com"}, allowCredentials = "true")
@RestController
@RequestMapping("/api/menu-items") // Adjust this mapping if your base path is different
public class MenuController {

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FileStorageService fileStorageService;

	// 1. Get all items belonging ONLY to the logged-in user
	@GetMapping
	public List<MenuItem> getAllItems(@AuthenticationPrincipal UserDetails userDetails) {
		User currentUser = userRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		return menuRepository.findByUserId(currentUser.getId());
	}

	// 2. Create a new item linked directly to the logged-in user
	@PostMapping(consumes = {"multipart/form-data"})
	public MenuItem createItem(
			@RequestPart("item") @Valid MenuItem item,
			@RequestPart(value = "file", required = false) MultipartFile file,
			@AuthenticationPrincipal UserDetails userDetails) {

		User currentUser = userRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		if (file != null && !file.isEmpty()) {   String imagePath = fileStorageService.storeFile(file);
			item.setImageUrl(imagePath);
		}

		item.setUser(currentUser); // Link the item to the current user
		return menuRepository.save(item);
	}

	// 3. Update an existing item after confirming the owner matches
	@PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
	public ResponseEntity<MenuItem> updateItem(
			@PathVariable Long id,
			@RequestPart("item") @Valid MenuItem details,
			@RequestPart(value = "file", required = false) MultipartFile file,
			@AuthenticationPrincipal UserDetails userDetails) {  User currentUser = userRepository.findByUsername(userDetails.getUsername())
			.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		MenuItem item = menuRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Item not found"));

		// Security Check: Verify that the current user owns this menu item
		if (!item.getUser().getId().equals(currentUser.getId())) {
			return ResponseEntity.status(403).build(); // Forbidden access if trying to edit another user's item
		}

		item.setName(details.getName());
		item.setCategory(details.getCategory());
		item.setPrice(details.getPrice());
		item.setAvailable(details.isAvailable());
		item.setDescription(details.getDescription());
		item.setPrepTimeMinutes(details.getPrepTimeMinutes());
		if (file != null && !file.isEmpty()) {
			String imagePath = fileStorageService.storeFile(file);
			item.setImageUrl(imagePath);
		}

		return ResponseEntity.ok(menuRepository.save(item));
	}

	// 4. Delete an item after confirming ownership
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
		User currentUser = userRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		MenuItem item = menuRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Item not found"));

		// Security Check: Verify that the current user owns this menu item
		if (!item.getUser().getId().equals(currentUser.getId())) {
			return ResponseEntity.status(403).build();
		}

		menuRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}