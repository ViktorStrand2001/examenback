package com.example.examenback.services;

import com.example.examenback.models.PostModel;
import com.example.examenback.models.UserModel;
import com.example.examenback.repositories.IPostRepository;
import com.example.examenback.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class PostService {

    private final IPostRepository iPostrepository;
    private final IUserRepository iUserRepository;

    @Autowired
    public PostService(IPostRepository iPostrepository, IUserRepository iUserRepository) {
        this.iPostrepository = iPostrepository;
        this.iUserRepository = iUserRepository;
    }

    public List<PostModel> getAllPosts(){
        return iPostrepository.findAll();
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

        System.out.println("----post---- " + postModel);
        System.out.println("----username---- " + currentUser);

        return iPostrepository.save(postModel);
    }



}
