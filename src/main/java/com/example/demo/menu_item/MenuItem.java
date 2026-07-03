package com.example.demo.menu_item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.persistence.*;

@Entity
@Table(name = "menu_items")
public class MenuItem {
	
	
	@NotBlank(message = "Item name cannot be empty")
    private String name;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
    private Double price;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
  //  private String name;
    private String category;
 //   private double price;
 private boolean available;
     // Adding the missing fields required by your controller
    private String description;
    private String imageUrl;
    private int prepTimeMinutes;

    // 1. Default No-Args Constructor
    public MenuItem() {
    }
    // 2. All-Args Constructor
    public MenuItem(String name, String category, double price, boolean available, String description, String imageUrl, int prepTimeMinutes) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = available;
        this.description = description;
        this.imageUrl = imageUrl;
        this.prepTimeMinutes = prepTimeMinutes;
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
}
