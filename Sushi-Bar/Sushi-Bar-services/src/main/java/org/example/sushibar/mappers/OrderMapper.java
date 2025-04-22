package org.example.sushibar.mappers;

import com.example.models.OrderResponse;
import org.example.sushibar.entities.MenuItemEntity;
import org.example.sushibar.entities.OrderEntity;

import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderResponse toDto(OrderEntity entity) {
        OrderResponse dto = new OrderResponse();
        dto.setOrderId(entity.getId().intValue());
        dto.setCustomerNumber(entity.getCustomerNumber());
        dto.setStatus(OrderResponse.StatusEnum.valueOf(entity.getStatus().name()));
        if (entity.getItems() != null) {
            dto.setItems(entity.getItems().stream()
                    .map(menuItem -> new com.example.models.MenuItem()
                            .id(menuItem.getId().intValue())
                            .name(menuItem.getName())
                            .price((float) menuItem.getPrice())
                            .description(menuItem.getDescription())
                            .category(menuItem.getCategory())
                            .imageUrl(menuItem.getImageUrl() != null ? java.net.URI.create(menuItem.getImageUrl()) : null)
                    ).collect(Collectors.toList()));
        }

        return dto;
    }
}
