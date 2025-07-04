package org.example.sushibar.controllers;

import com.example.api.MenuApi;
import com.example.models.GetAllMenuItems200Response;
import com.example.models.MenuItem;
import org.example.sushibar.aop.annotations.LogMethod;
import org.example.sushibar.entities.MenuItemEntity;
import org.example.sushibar.mappers.MenuItemMapper;
import org.example.sushibar.services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
public class MenuController implements MenuApi {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }
    @LogMethod
    @Override
    public ResponseEntity<MenuItem> addMenuItem(MenuItem menuItem) {
        MenuItemEntity entity = MenuItemMapper.toEntity(menuItem);
        MenuItemEntity saved = menuService.create(entity);
        return ResponseEntity.status(201).body(MenuItemMapper.toDto(saved));
    }
    @LogMethod
    @Override
    public ResponseEntity<Void> deleteAllMenuItems() {
        menuService.getAll().forEach(item -> menuService.delete(item.getId()));
        return ResponseEntity.noContent().build();
    }
    @LogMethod
    @Override
    public ResponseEntity<Void> deleteMenuItem(Integer id) {
        menuService.delete(id.longValue());
        return ResponseEntity.noContent().build();
    }
    @LogMethod
    @Override

    public ResponseEntity<GetAllMenuItems200Response> getAllMenuItems(Integer page, Integer size, String category) {
        var pageable = org.springframework.data.domain.PageRequest.of(page, size);

        var pageResult = (category != null && !category.isBlank())
                ? menuService.getAllByCategoryPaged(category, pageable)
                : menuService.getAllPaged(pageable);

        GetAllMenuItems200Response response = new GetAllMenuItems200Response();
        response.setContent(
                pageResult.getContent().stream()
                        .map(MenuItemMapper::toDto)
                        .toList()
        );
        response.setNumber(pageResult.getNumber());
        response.setSize(pageResult.getSize());
        response.setTotalElements((int) pageResult.getTotalElements());
        response.setTotalPages(pageResult.getTotalPages());

        return ResponseEntity.ok(response);
    }
    @LogMethod
    @Override
    public ResponseEntity<List<MenuItem>> getTopMenuItems() {
        List<MenuItemEntity> topItems = menuService.findTopMenuItems();
        List<MenuItem> response = topItems.stream()
                .map(MenuItemMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @LogMethod
    @Override
    public ResponseEntity<MenuItem> getMenuItemById(Integer id) {
        Optional<MenuItemEntity> found = menuService.getById(id.longValue());
        return found.map(entity -> ResponseEntity.ok(MenuItemMapper.toDto(entity)))
                .orElse(ResponseEntity.notFound().build());
    }
    @LogMethod
    @Override
    public ResponseEntity<MenuItem> updateMenuItem(Integer id, MenuItem menuItem) {
        MenuItemEntity updatedEntity = MenuItemMapper.toEntity(menuItem);
        MenuItemEntity result = menuService.update(id.longValue(), updatedEntity);
        return ResponseEntity.ok(MenuItemMapper.toDto(result));
    }
}
