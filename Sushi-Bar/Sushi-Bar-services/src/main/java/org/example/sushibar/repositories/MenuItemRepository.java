package org.example.sushibar.repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.example.sushibar.entities.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Long> {
    Page<MenuItemEntity> findByCategoryIgnoreCase(String category, Pageable pageable);
    List<MenuItemEntity> findTop5ByOrderByPriceDesc();

}
