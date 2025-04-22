package org.example.sushibar.services;

import org.example.sushibar.entities.UserEntity;
import org.example.sushibar.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public boolean delete(Long id) {
        repository.deleteById(id);
        return false;
    }

    @Override
    public Page<UserEntity> getAllPaged(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<UserEntity> findByEmailAndPassword(String email, String password) {
        return repository.findByEmailAndPassword(email, password);
    }


}
