package com.PComponents.service;

import com.PComponents.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public User findUserByEmail(String email);

    public User saveUser(User user);

    List<User> findAllUsers();

    public void deleteUserById(int id) throws Exception;

    public Optional<User> findById(int id);

    public User makeAdmin(User user);

    public User makeUser(User user);
}
