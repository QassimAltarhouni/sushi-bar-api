package org.example.sushibar.repositories;

import org.example.sushibar.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmailAndPassword(String email, String password);
    Optional<UserEntity> findByEmailIgnoreCase(String email);
    Optional<UserEntity> findByPhone(String phone);

}
