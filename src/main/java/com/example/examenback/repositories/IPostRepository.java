package com.example.examenback.repositories;

import com.example.examenback.models.PostModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPostRepository extends JpaRepository<PostModel, Long> {
}
