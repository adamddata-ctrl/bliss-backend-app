package com.example.demo.controller;

import jakarta.validation.Valid;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.bliss.service.FileStorageService;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.menu_item.MenuItem;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.repository.MenuRepository;
@RestController
@RequestMapping("/api/menu")
//@CrossOrigin(origins = "*")
	@CrossOrigin(origins = {"http://localhost:4200", "https://bliss-front-2x0f.onrender.com"}, allowCredentials = "true")
//@CrossOrigin(origins = "http://localhost:4200")
public class MenuController {
    
	 @Autowired
	    private MenuRepository menuRepository;

	    @Autowired
	    private FileStorageService fileStorageService; // Inject the file storage service

	    @GetMapping
	    public List<MenuItem> getAllItems() {
	        return menuRepository.findAll();
	    }

	    // 1. Updated create endpoint to consume multipart form data
	    @PostMapping(consumes = {"multipart/form-data"})
	    public MenuItem createItem(
	    	    @RequestPart("item") @Valid MenuItem item,
	    	    @RequestPart(value = "file", required = false) MultipartFile file) {
	        if (file != null && !file.isEmpty()) {
	        	 String imagePath = fileStorageService.storeFile(file);
	             item.setImageUrl(imagePath);
	         }
	         return menuRepository.save(item);
	     }

	     // 2. Updated update endpoint to consume multipart form data
	     @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
	     public ResponseEntity<MenuItem> updateItem(
	    		    @PathVariable Long id,
	    		    @RequestPart("item") @Valid MenuItem details,
	             @RequestPart(value = "file", required = false) MultipartFile file) {
	         
	         MenuItem item = menuRepository.findById(id)
	                 .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

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

	     @DeleteMapping("/{id}")
	     public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
	         menuRepository.deleteById(id);
	         return ResponseEntity.noContent().build();
	        }
}

