package org.example.sushibar.mappers;

import com.example.models.MenuItem;
import org.example.sushibar.entities.MenuItemEntity;

import java.net.URI;

public class MenuItemMapper {

    public static MenuItem toDto(MenuItemEntity entity) {
        MenuItem dto = new MenuItem();
        dto.setId(entity.getId().intValue());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setDescription(entity.getDescription());
        dto.setCategory(entity.getCategory());
        dto.setImageUrl(URI.create(entity.getImageUrl()));
        return dto;
    }

    public static MenuItemEntity toEntity(MenuItem dto) {
        MenuItemEntity entity = new MenuItemEntity();
        if (dto.getId() != null) {
            entity.setId(dto.getId().longValue());
        }
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());
        entity.setCategory(dto.getCategory());
        entity.setImageUrl(String.valueOf(dto.getImageUrl()));
        return entity;
    }
}
