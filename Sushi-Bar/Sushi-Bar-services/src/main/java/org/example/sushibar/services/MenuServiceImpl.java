package org.example.sushibar.services;

import org.example.sushibar.entities.MenuItemEntity;
import org.example.sushibar.repositories.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
 import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuItemRepository repository;

    @Autowired
    public MenuServiceImpl(MenuItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MenuItemEntity> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<MenuItemEntity> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public MenuItemEntity create(MenuItemEntity item) {
        return repository.save(item);
    }

    @Override
    public MenuItemEntity update(Long id, MenuItemEntity updatedItem) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(updatedItem.getName());
                    existing.setPrice(updatedItem.getPrice());
                    existing.setDescription(updatedItem.getDescription());
                    existing.setCategory(updatedItem.getCategory());
                    existing.setImageUrl(updatedItem.getImageUrl());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<MenuItemEntity> getAllPaged(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<MenuItemEntity> getAllByCategoryPaged(String category, Pageable pageable) {
        return repository.findByCategoryIgnoreCase(category, pageable);
    }
    @Override
    public List<MenuItemEntity> findTopMenuItems() {
        Pageable topFive = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "price"));
         return repository.findTop5ByOrderByPriceDesc();
    }

}
