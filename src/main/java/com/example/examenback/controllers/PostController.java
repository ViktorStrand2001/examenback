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
    public ResponseEntity<List<PostModel>> getAllPosts(){
        List<PostModel> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<PostModel> createPost(@RequestBody PostModel postModel){
        try{
            postService.createPost(postModel);
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage() + e);
        }
        return new ResponseEntity<>(postModel, HttpStatus.CREATED);
    }

}
