package org.example.sushibar.mappers;

import com.example.models.MenuItem;
import com.example.models.OrderResponse;
import org.example.sushibar.entities.MenuItemEntity;
import org.example.sushibar.entities.OrderEntity;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import com.example.models.BuyerSummary;

public class OrderMapper {

    public static OrderResponse toDto(OrderEntity entity) {
        OrderResponse dto = new OrderResponse();

        if (entity == null) return dto; // 🔒 حماية من null لو وصلت قيمة خاطئة

        dto.setOrderId(entity.getId() != null ? entity.getId().intValue() : null);

        dto.setStatus(OrderResponse.StatusEnum.valueOf(entity.getStatus().name()));
        dto.setNotes(entity.getNotes());
        dto.setPaid(entity.isPaid());
        dto.setPhone(entity.getPhone());
        dto.setTotalCost((float) entity.getTotalCost());

        if (entity.getUser() != null) {
            BuyerSummary buyer = new BuyerSummary();
            buyer.setEmail(entity.getUser().getEmail());
            buyer.setUsername(entity.getUser().getUsername());
            dto.setBuyer(buyer);
        }

        if (entity.getCreatedOn() != null) {
            dto.setCreatedOn(OffsetDateTime.of(entity.getCreatedOn(), ZoneOffset.UTC));
        }

        List<MenuItemEntity> itemEntities = entity.getItems();
        if (itemEntities != null && !itemEntities.isEmpty()) {
            List<MenuItem> items = itemEntities.stream().map(menuItem -> {
                MenuItem dtoItem = new MenuItem()
                        .id(menuItem.getId().intValue())
                        .name(menuItem.getName())
                        .price((float) menuItem.getPrice())
                        .description(menuItem.getDescription())
                        .category(menuItem.getCategory());

                if (menuItem.getImageUrl() != null) {
                    dtoItem.setImageUrl(java.net.URI.create(menuItem.getImageUrl()));
                }
                return dtoItem;
            }).collect(Collectors.toList());

            dto.setItems(items);
        }

        return dto;
    }
}
