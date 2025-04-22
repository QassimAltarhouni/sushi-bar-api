package org.example.sushibar.mappers;

import com.example.models.ReservationRequest;
import com.example.models.ReservationResponse;
import org.example.sushibar.entities.ReservationEntity;

public class ReservationMapper {

    public static ReservationEntity toEntity(ReservationRequest dto) {
        ReservationEntity entity = new ReservationEntity();
        entity.setCustomerNumber(dto.getCustomerNumber());
        entity.setDate(dto.getDate().toLocalDateTime());
        entity.setTableNumber(dto.getTableNumber());
        entity.setStatus(ReservationEntity.ReservationStatus.PENDING);
        return entity;
    }

    public static ReservationResponse toDto(ReservationEntity entity) {
        ReservationResponse dto = new ReservationResponse();
        dto.setReservationId(entity.getId().intValue());
        dto.setCustomerNumber(entity.getCustomerNumber());
        dto.setDate(entity.getDate().atZone(java.time.ZoneId.systemDefault()).toOffsetDateTime());
        dto.setStatus(ReservationResponse.StatusEnum.valueOf(entity.getStatus().name()));
        return dto;
    }
    public static ReservationResponse errorResponse(String message) {
        ReservationResponse response = new ReservationResponse();
        response.setStatus(null); // or keep it as is
        response.setCustomerNumber("Error: " + message);
        return response;
    }

}
