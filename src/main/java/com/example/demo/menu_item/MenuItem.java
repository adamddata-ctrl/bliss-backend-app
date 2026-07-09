package com.example.demo.menu_item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import com.example.demo.user.User;

@Entity
@Table(name = "menu_items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Item name cannot be empty")
    private String name;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
    private Double price;

    private String category;
    private boolean available;
    private String description;
    private String imageUrl;
    private int prepTimeMinutes;

    // Establishes data ownership link to the MySQL users table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 1. Default No-Args Constructor
    public MenuItem() {
    }

    // 2. All-Args Constructor
    public MenuItem(String name, String category, double price, boolean available, String description, String imageUrl, int prepTimeMinutes, User user) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = available;
        this.description = description;
        this.imageUrl = imageUrl;
        this.prepTimeMinutes = prepTimeMinutes;
        this.user = user;
    }
    // 3. Complete Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getPrepTimeMinutes() { return prepTimeMinutes; }
    public void setPrepTimeMinutes(int prepTimeMinutes) { this.prepTimeMinutes = prepTimeMinutes; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}