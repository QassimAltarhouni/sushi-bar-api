package org.example.sushibar.services;

import org.example.sushibar.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserEntity> getAll();
    Optional<UserEntity> getById(Long id);
    UserEntity create(UserEntity user);
    UserEntity update(Long id, UserEntity user);
    void delete(Long id);
}
