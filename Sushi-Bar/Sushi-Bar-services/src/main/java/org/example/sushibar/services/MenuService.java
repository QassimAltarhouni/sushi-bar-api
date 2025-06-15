package org.example.sushibar.services;

import org.example.sushibar.entities.MenuItemEntity;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MenuService {
    List<MenuItemEntity> getAll();
    Optional<MenuItemEntity> getById(Long id);
    MenuItemEntity create(MenuItemEntity item);
    MenuItemEntity update(Long id, MenuItemEntity updatedItem);
    void delete(Long id);
    Page<MenuItemEntity> getAllPaged(Pageable pageable);
    Page<MenuItemEntity> getAllByCategoryPaged(String category, Pageable pageable);
    List<MenuItemEntity> findTopMenuItems();
}
