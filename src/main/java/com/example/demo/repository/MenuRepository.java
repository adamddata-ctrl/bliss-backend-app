package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.menu_item.MenuItem;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<MenuItem, Long> {

    // Automatically executes a query filtered by the specific owner's user ID
    List<MenuItem> findByUserId(Long userId);
}