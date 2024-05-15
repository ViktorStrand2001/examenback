package com.example.examenback.services;

import com.example.examenback.config.WebPasswordConfig;
import com.example.examenback.enums.Roles;
import com.example.examenback.models.UserModel;
import com.example.examenback.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final IUserRepository iUserRepository;
    private final WebPasswordConfig webPasswordConfig;
    @Autowired
    public UserService(IUserRepository iUserRepository, WebPasswordConfig webPasswordConfig) {
        this.iUserRepository = iUserRepository;
        this.webPasswordConfig = webPasswordConfig;
    }

    public UserModel createUser(UserModel newUser) {

        newUser.setPassword(webPasswordConfig.bCryptPasswordEncoder().encode(newUser.getPassword()));
        newUser.setRoles(Roles.USER);
        newUser.setAccountNonExpired(true);
        newUser.setAccountNonLocked(true);
        newUser.setAccountEnabled(true);
        newUser.setCredentialsNonExpired(true);

        return iUserRepository.save(newUser);
    }

    public List<UserModel> getAllUsers() {
        return iUserRepository.findAll();
    }

    public Optional<UserModel> getUserById(UUID id) {
        return iUserRepository.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return iUserRepository.findByUsername(username);
    }

    public boolean doesUsernameAndEmailExist(String username, String email) {
        UserModel existingUsername = iUserRepository.findByUsername(username);
        UserModel existingEmail = iUserRepository.findByEmail(email);

        if (existingUsername != null){
            return true;
        }
        if (existingEmail != null){
            return true;
        }
        return false;
    }

    public UserModel updateUser(UserModel user) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<UserModel> existingUserOptional = Optional.ofNullable(iUserRepository.findByUsername(username));

        try {
            if (existingUserOptional.isPresent()) {
                UserModel existingUser = existingUserOptional.get();

                existingUser.setAccountEnabled(user.isEnabled());
                existingUser.setAccountNonExpired(user.isAccountNonExpired());
                existingUser.setCredentialsNonExpired(user.isCredentialsNonExpired());
                existingUser.setAccountNonLocked(user.isAccountNonLocked());

                if (user.getUsername() != null) {
                    existingUser.setUsername(user.getUsername());
                }

                if (user.getEmail() != null) {
                    existingUser.setEmail(user.getEmail());
                }

                if (user.getPosts() != null) {
                    existingUser.setPosts(user.getPosts());
                }

                if (!user.getPassword().equals(existingUser.getPassword())){
                    existingUser.setPassword(webPasswordConfig.bCryptPasswordEncoder().encode(existingUser.getPassword()));
                    System.out.println("new password : " + existingUser.getPassword());
                }else{
                    existingUser.setPassword(user.getPassword());
                }

                if (user.getRoles() != null) {
                    existingUser.setRoles(user.getRoles());
                }

                if (
                        existingUser.isAccountNonExpired() == false ||
                        existingUser.isAccountNonLocked() == false ||
                        existingUser.isCredentialsNonExpired() == false
                ){
                    existingUser.setAccountEnabled(false);
                }else{
                    existingUser.setAccountEnabled(true);
                }

                iUserRepository.save(existingUser);

            } else {
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return user;
    }

    public void deleteUser(UUID userId) {
        iUserRepository.deleteById(userId);
    }

}
