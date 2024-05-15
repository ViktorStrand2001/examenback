package com.example.examenback.controllers;

import com.example.examenback.models.PostModel;
import com.example.examenback.models.UserModel;
import com.example.examenback.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@CrossOrigin
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostModel>> getAllPosts(){
        List<PostModel> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USERPOST')")
    @PostMapping()
    public ResponseEntity<PostModel> createPost(@RequestBody PostModel postModel){
        try{
            postService.createPost(postModel);
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage() + e);
        }
        return new ResponseEntity<>(postModel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USERPOST')")
    public ResponseEntity<PostModel> updatePost(@RequestBody PostModel post) {

        PostModel updatedPost = postService.updatePost(post);
        if (updatedPost != null) {
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAuthority('USERDELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        try {
            postService.deletePost(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
