package org.example.sushibar.config;

import org.example.sushibar.entities.MenuItemEntity;
import org.example.sushibar.entities.UserEntity;
import org.example.sushibar.repositories.MenuItemRepository;
import org.example.sushibar.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.List;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public ApplicationRunner seedDatabase(UserRepository userRepository, MenuItemRepository menuItemRepository) {
        return args -> {
            seedUsers(userRepository);
            seedMenu(menuItemRepository);
        };
    }

    private void seedUsers(UserRepository userRepository) {
        if (userRepository.count() > 0) {
            return;
        }

        UserEntity demoCustomer = new UserEntity();
        demoCustomer.setUsername("Demo Diner");
        demoCustomer.setEmail("demo@sushibar.com");
        demoCustomer.setPassword("password123");
        demoCustomer.setPhone("+15555550101");
        demoCustomer.setRole("customer");

        UserEntity demoAdmin = new UserEntity();
        demoAdmin.setUsername("Sushi Admin");
        demoAdmin.setEmail("admin@sushibar.com");
        demoAdmin.setPassword("admin123");
        demoAdmin.setPhone("+15555550999");
        demoAdmin.setRole("admin");

        userRepository.saveAll(List.of(demoCustomer, demoAdmin));
        log.info("Seeded demo users for quick start ({} records)", userRepository.count());
    }

    private void seedMenu(MenuItemRepository menuItemRepository) {
        if (menuItemRepository.count() > 0) {
            return;
        }

        List<MenuItemEntity> items = List.of(
                menuItem("Spicy Tuna Roll", 10.99, "Fresh tuna with spicy mayo and cucumber.", "roll", "https://images.unsplash.com/photo-1553621042-f6e147245754"),
                menuItem("Salmon Nigiri", 8.50, "Premium salmon over seasoned rice.", "nigiri", "https://images.unsplash.com/photo-1570498839593-e565b39455fc"),
                menuItem("Shrimp Tempura", 12.25, "Crispy tempura shrimp served with tentsuyu sauce.", "appetizer", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c"),
                menuItem("Miso Ramen", 13.75, "Rich miso broth with pork belly and soft-boiled egg.", "ramen", "https://images.unsplash.com/photo-1604908176865-91f3b01b8e58"),
                menuItem("Dragon Roll", 14.10, "Tempura shrimp roll topped with avocado and eel sauce.", "roll", "https://images.unsplash.com/photo-1627308595184-00c3f4f5f113"),
                menuItem("Matcha Cheesecake", 6.25, "Creamy cheesecake infused with ceremonial matcha.", "dessert", "https://images.unsplash.com/photo-1499028344343-cd173ffc68a9")
        );

        menuItemRepository.saveAll(items);
        log.info("Seeded default menu items ({} records)", items.size());
    }

    private MenuItemEntity menuItem(String name, double price, String description, String category, String imageUrl) {
        MenuItemEntity entity = new MenuItemEntity();
        entity.setName(name);
        entity.setPrice(price);
        entity.setDescription(description);
        entity.setCategory(category);
        entity.setImageUrl(StringUtils.hasText(imageUrl) ? imageUrl : null);
        return entity;
    }
}
