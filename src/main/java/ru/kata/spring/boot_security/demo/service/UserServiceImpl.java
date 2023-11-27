package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UsersRepository;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void createUser(User user) {
        if (usersRepository.findByUsername(user.getUsername()).isPresent()) {
            return;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
    }

    @Transactional
    @Override
    public void updateUser(User user, Long id) {
        User userFromDb = usersRepository.findById(id).get();
        if (userFromDb.getPassword().equals(user.getPassword())) {
            usersRepository.save(user);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            usersRepository.save(user);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return (List<User>) usersRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User readUser(Long id) {
        Optional<User> user = usersRepository.findById(id);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User with this id is not found");
        }
        return user.get();
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (usersRepository.findById(id).isPresent()) {
            usersRepository.deleteById(id);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserByUsername(String username) {
        Optional<User> foundUser = usersRepository.findByUsername(username);
        return foundUser.orElseThrow(UserNotFoundException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opUser = usersRepository.findByUsername(username);

        if (opUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return opUser.get();
//        User user = opUser.get();
//                return new org.springframework.security.core.userdetails.User(user.getUsername(),
//                user.getPassword(), user.getAuthorities());
//        return new User(user.getUsername(),user.getSurName(),user.getEmail(),user.getAge(),user.getPassword(),
//                user.getRoles());
    }

    // метод преобразует коллекцию Role в Authority

//    private Collection<? extends GrantedAuthority> mapRoleToAuthorities(Collection<Role> roles) {

//        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRole())).toList();

//    }
}