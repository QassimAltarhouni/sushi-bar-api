package org.example.sushibar.controllers;

import com.example.api.MenuApi;
import com.example.models.MenuItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MenuController implements MenuApi {

    private final List<MenuItem> menuItems = new ArrayList<>();

    @Override
    public ResponseEntity<MenuItem> addMenuItem(MenuItem menuItem) {
        menuItem.setId(menuItems.size() + 1); // Simple mock ID
        menuItems.add(menuItem);
        return ResponseEntity.status(201).body(menuItem);
    }

    @Override
    public ResponseEntity<Void> deleteAllMenuItems() {
        menuItems.clear();
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteMenuItem(Integer id) {
        menuItems.removeIf(item -> item.getId().equals(id));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(menuItems);
    }

    @Override
    public ResponseEntity<MenuItem> getMenuItemById(Integer id) {
        return menuItems.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<MenuItem> updateMenuItem(Integer id, MenuItem menuItem) {
        for (int i = 0; i < menuItems.size(); i++) {
            if (menuItems.get(i).getId().equals(id)) {
                menuItem.setId(id);
                menuItems.set(i, menuItem);
                return ResponseEntity.ok(menuItem);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
