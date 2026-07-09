package com.example.demo.controller;

import com.example.demo.menu_item.MenuItem;
import com.example.demo.repository.MenuRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.user.User;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/metrics")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class MetricsController {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/category-distribution")
    public ResponseEntity<Map<String, Long>> getCategoryDistribution(@AuthenticationPrincipal UserDetails userDetails) {
        // Find the currently authenticated user profile
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // Fetch menu data items belonging ONLY to this specific user profile
        List<MenuItem> userItems = menuRepository.findByUserId(currentUser.getId());

        // Group and tally categories specifically for this isolated user dataset
        Map<String, Long> metrics = userItems.stream()
                .filter(item -> item.getCategory() != null)
                .collect(Collectors.groupingBy(
                        item -> item.getCategory().toLowerCase().trim(),
                        Collectors.counting()
                ));

        return ResponseEntity.ok(metrics);
    }
}