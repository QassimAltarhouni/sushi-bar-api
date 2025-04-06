package org.example.sushibar.services;

import org.example.sushibar.entities.MenuItemEntity;

import java.util.List;
import java.util.Optional;

public interface MenuService {
    List<MenuItemEntity> getAll();
    Optional<MenuItemEntity> getById(Long id);
    MenuItemEntity create(MenuItemEntity item);
    MenuItemEntity update(Long id, MenuItemEntity updatedItem);
    void delete(Long id);
}
