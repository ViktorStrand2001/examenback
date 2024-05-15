package com.example.examenback.repositories;

import com.example.examenback.models.PostModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IPostRepository extends JpaRepository<PostModel, UUID> {

    Optional<PostModel> findById(UUID id);

    void deleteById(UUID id);

}
