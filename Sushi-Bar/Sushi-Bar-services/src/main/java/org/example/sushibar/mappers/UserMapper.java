package org.example.sushibar.mappers;

import com.example.models.User;
import org.example.sushibar.entities.UserEntity;

public class UserMapper {

    public static User toDto(UserEntity entity) {
        User dto = new User();

        if (entity.getId() != null) {
            dto.setId((int) (long) entity.getId()); // Long → long → int
        }

        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setRole(entity.getRole());
        dto.setPassword(entity.getPassword()); // optional, if you return passwords

        return dto;
    }

    public static UserEntity toEntity(User dto) {
        UserEntity entity = new UserEntity();

        if (dto.getId() != null) {
            entity.setId(dto.getId().longValue()); // Integer → Long
        }

        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setRole(dto.getRole());
        entity.setPassword(dto.getPassword());

        return entity;
    }
}
