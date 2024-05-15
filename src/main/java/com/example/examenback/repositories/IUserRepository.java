package com.example.examenback.repositories;

import com.example.examenback.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<UserModel, UUID> {

    UserModel findByUsername(String username);

    UserModel findByEmail(String email);

    Optional<UserModel> findById(UUID id);

    void deleteById(UUID id);
}
