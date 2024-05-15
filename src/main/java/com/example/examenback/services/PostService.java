package com.example.examenback.services;

import com.example.examenback.models.PostModel;
import com.example.examenback.models.UserModel;
import com.example.examenback.repositories.IPostRepository;
import com.example.examenback.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    private final IPostRepository iPostRepository;
    private final IUserRepository iUserRepository;

    @Autowired
    public PostService(IPostRepository iPostRepository, IUserRepository iUserRepository) {
        this.iPostRepository = iPostRepository;
        this.iUserRepository = iUserRepository;
    }

    public PostModel createPost(PostModel postModel){

        ZoneId swedishTimeZone = ZoneId.of("Europe/Stockholm");
        LocalDateTime currentDateTime = LocalDateTime.now(swedishTimeZone);
        Date currentDate = Date.from(currentDateTime.atZone(swedishTimeZone).toInstant());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserModel currentUser = iUserRepository.findByUsername(username);

        postModel.setPublished(currentDate);
        postModel.setUser(currentUser);

        System.out.println("----------------------------post------------------------------ " + postModel);
        System.out.println("------------------------------username------------------------ " + currentUser);

        return iPostRepository.save(postModel);
    }

    public List<PostModel> getAllPosts(){
        return iPostRepository.findAll();
    }

    public PostModel updatePost(PostModel post) {
        Optional<PostModel> existingPostOptional = iPostRepository.findById(post.getId());

        try {
            if (existingPostOptional.isPresent()) {
                PostModel existingPost = existingPostOptional.get();

                if (post.getUser() != null) {
                    existingPost.setUser(post.getUser());
                }

                if (post.getContent() != null) {
                   existingPost.setContent(post.getContent());
                }

                if (post.getEMail() != null) {
                    existingPost.setEMail(post.getEMail());
                }

                if (post.getPublished() != null) {
                    existingPost.setPublished(post.getPublished());
                }

                if (post.getHuntingParty() != null) {
                    existingPost.setHuntingParty(post.getHuntingParty());
                }

                if (post.getPhoneNumber() != null) {
                    existingPost.setPhoneNumber(post.getPhoneNumber());
                }
                iPostRepository.save(existingPost);

            } else {
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return post;
    }

    public void deletePost(UUID postId) {
        iPostRepository.deleteById(postId);
    }

}
