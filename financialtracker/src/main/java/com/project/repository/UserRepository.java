package com.project.repository;

import com.project.model.User;

import java.util.Optional;

public interface UserRepository {

    void save(User user);

    Optional<User> findByEmail(String email);

    void update(User user);

    void delete(User user);


}
