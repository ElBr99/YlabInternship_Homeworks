package com.project.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    private String name;
    private String email;
    private String password;
    private Role role;
    private Boolean blocked;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
