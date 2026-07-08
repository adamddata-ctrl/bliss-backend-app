package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.repository.MenuRepository;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/metrics")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class MetricsController {

    @Autowired
    private MenuRepository menuRepository;

    @GetMapping("/category-distribution")
    public ResponseEntity<Map<String, Long>> getCategoryDistribution() {
        Map<String, Long> metrics = menuRepository.findAll().stream()
                .filter(item -> item.getCategory() != null)
                .collect(Collectors.groupingBy(
                        item -> item.getCategory().toLowerCase().trim(),
                        Collectors.counting()
                ));
        return ResponseEntity.ok(metrics);
    }
}