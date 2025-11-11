package org.example.sushibar.services;

import org.example.sushibar.entities.UserEntity;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {

    List<UserEntity> getAll();
    Optional<UserEntity> getById(Long id);
    UserEntity create(UserEntity user);
    UserEntity update(Long id, UserEntity user);
    UserEntity delete(Long id);
    Page<UserEntity> getAllPaged(Pageable pageable);
    Optional<UserEntity> findByEmailAndPassword(String email, String password);
    Optional<UserEntity> findByEmailIgnoreCase(String email);
    Optional<UserEntity> getByPhone(String phone);
}
