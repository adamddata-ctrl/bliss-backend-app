package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.menu_item.MenuItem;

@Repository
public interface MenuRepository extends JpaRepository<MenuItem, Long> {
}