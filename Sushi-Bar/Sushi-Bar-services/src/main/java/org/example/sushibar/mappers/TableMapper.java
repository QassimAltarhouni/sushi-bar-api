package org.example.sushibar.mappers;

import com.example.models.Table;
import org.example.sushibar.entities.TableEntity;

public class TableMapper {

    public static TableEntity toEntity(Table dto) {
        TableEntity entity = new TableEntity();
        entity.setTableNumber(dto.getTableNumber());
        entity.setLocation(dto.getLocation());
        entity.setPosition(dto.getPosition());
        entity.setSeats(dto.getSeats());
        entity.setAvailable(dto.getAvailable() != null && dto.getAvailable());
        return entity;
    }

    public static Table toDto(TableEntity entity) {
        Table dto = new Table();
        dto.setId(entity.getId().intValue());
        dto.setTableNumber(entity.getTableNumber());
        dto.setLocation(entity.getLocation());
        dto.setPosition(entity.getPosition());
        dto.setAvailable(entity.isAvailable());
        dto.setSeats(entity.getSeats());
        return dto;
    }
}
