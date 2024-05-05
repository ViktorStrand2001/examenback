package com.example.examenback.controllers;

import com.example.examenback.models.PostModel;
import com.example.examenback.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/getAllPosts")
    public List<PostModel> getAllPosts(){
        return postService.getAllPosts();
    }

    @PostMapping("/create")
    public ResponseEntity<PostModel> createPost(@RequestBody PostModel postModel){

        postService.createPost(postModel);

        return new ResponseEntity<>(postModel, HttpStatus.CREATED);
    }

}
