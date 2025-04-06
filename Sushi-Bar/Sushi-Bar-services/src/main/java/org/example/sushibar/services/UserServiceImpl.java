package org.example.sushibar.services;

import org.example.sushibar.entities.UserEntity;
import org.example.sushibar.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserEntity> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<UserEntity> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public UserEntity create(UserEntity user) {
        return repository.save(user);
    }

    @Override
    public UserEntity update(Long id, UserEntity updatedUser) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setUsername(updatedUser.getUsername());
                    existing.setEmail(updatedUser.getEmail());
                    existing.setPhone(updatedUser.getPhone());
                    existing.setRole(updatedUser.getRole());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
