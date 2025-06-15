package org.example.sushibar.controllers;

import com.example.api.TablesApi;
import com.example.models.Table;
import org.example.sushibar.entities.TableEntity;
import org.example.sushibar.mappers.TableMapper;
import org.example.sushibar.services.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TablesController implements TablesApi {

    private final TableService tableService;

    @Autowired
    public TablesController(TableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public ResponseEntity<List<Table>> getAllTables() {
        List<Table> result = tableService.getAll().stream()
                .map(TableMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Table> createTable(Table table) {
        TableEntity created = tableService.create(TableMapper.toEntity(table));
        return ResponseEntity.status(201).body(TableMapper.toDto(created));
    }

    @Override
    public ResponseEntity<Table> updateTableAvailability(Integer id, Boolean available,Integer seats) {
        TableEntity updated = tableService.updateAvailability(id.longValue(), available,seats);
        return ResponseEntity.ok(TableMapper.toDto(updated));
    }
}
