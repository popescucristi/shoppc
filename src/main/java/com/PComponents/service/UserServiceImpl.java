package com.PComponents.service;

import com.PComponents.model.Role;
import com.PComponents.model.User;
import com.PComponents.repository.RoleRepository;
import com.PComponents.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(int id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            id = user.get().getId();
            userRepository.deleteById(id);
        } else {
            throw new Exception("Nu s-a găsit nici un produs cu acest ID!");
        }
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public User makeAdmin(User user) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isPresent()) {
            User newUser = existingUser.get();
            Role userRole = roleRepository.findByRole("ADMIN");
            newUser.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
            newUser = userRepository.save(newUser);
            return newUser;
        } else {
            user = userRepository.save(user);
            return user;
        }
    }

    @Override
    public User makeUser(User user) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isPresent()) {
            User newUser = existingUser.get();
            Role userRole = roleRepository.findByRole("USER");
            newUser.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
            newUser = userRepository.save(newUser);
            return newUser;
        } else {
            user = userRepository.save(user);
            return user;
        }
    }
}
