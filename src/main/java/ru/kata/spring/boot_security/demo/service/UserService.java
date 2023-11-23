package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {
    List<User> getAllUsers();
    void createUser(User user);
    void updateUser(User user, Long id);
    User readUser(Long id);
    void deleteUser(Long id);
    User getUserByUsername(String username);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}