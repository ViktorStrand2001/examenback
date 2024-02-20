package com.example.examenback.services;

import com.example.examenback.config.WebPasswordConfig;
import com.example.examenback.enums.Roles;
import com.example.examenback.models.UserModel;
import com.example.examenback.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final IUserRepository iUserRepository;
    private final WebPasswordConfig webPasswordConfig;
    @Autowired
    public UserService(IUserRepository iUserRepository, WebPasswordConfig webPasswordConfig) {
        this.iUserRepository = iUserRepository;
        this.webPasswordConfig = webPasswordConfig;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return iUserRepository.findByUsername(username);
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
}
