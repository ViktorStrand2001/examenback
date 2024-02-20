package com.example.examenback.enums;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Roles {

    ADMIN("ADMINGET_ADMINPOST_ADMINPUT_ADMINDELETE"),
    USER("USERGET_USERPOST_USERPUT_USERDELETE");

    public String getPermissions() {
        return permissions;
    }

    private final String permissions;

    Roles(String permissions) {
        this.permissions = permissions;
    }

    public List<GrantedAuthority> splitPermissions() {
        String[] permissionsArray = permissions.split("_");

        return Arrays.stream(permissionsArray)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public List<GrantedAuthority> getAuthorities(){

        SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_" + name());
        List<GrantedAuthority> permissions = new ArrayList<>();

        permissions.add(role);
        permissions.addAll(splitPermissions());

        return permissions;
    }

}
