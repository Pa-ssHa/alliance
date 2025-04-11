package ru.kozelsk.alliance.services.users;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.collection.spi.PersistentSet;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kozelsk.alliance.models.users.Role;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.repositories.users.UserRepository;

import java.util.*;

@Slf4j
@Service
public class MyUserDetailsService implements UserDetailsService {

//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public MyUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public MyUserDetailsService() {}

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findOne(int id) {
        return userRepository.findById(id).orElse(null);
    }

    // with email
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadUserByUsername in service is starting");

        User user = userRepository.findByEmail(username) // или findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        log.info("User roles: {}", user.getRoles().getClass());
        log.info("loadUserByUsername in service is finished");


        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail()) // или getName()
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();
    }

    // with name
//    @Override
//    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
//        User user = userRepository.findByName(name)
//                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
//
//        return org.springframework.security.core.userdetails.User
//                .withUsername(user.getName())
//                .password(user.getPassword())
//                .authorities(user.getAuthorities()) // используем authorities вместо roles
//                .build();
//    }

    public void registerUser(User user) {

        log.info("registerUser in service is starting");

        if (user.getEmail().equals("passapdom@gmail.com") || user.getEmail().equals("admin@gmail.com")) {
            user.setRoles(Collections.singleton(Role.ADMIN)); // без ROLE_
        } else {
            user.setRoles(Collections.singleton(Role.USER)); // без ROLE_
        }

        log.info("User roles: {}", user.getRoles().getClass());

        if(user.getPassword().equals("google")){
            String randomPassword = UUID.randomUUID().toString();
            user.setPassword(passwordEncoder.encode(randomPassword));
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);

        log.info("registerUser in service is finished and user was saved");
    }

//    User roles: class java.util.Collections$SingletonSet         with OAuth2
//    User roles: class org.hibernate.collection.spi.PersistentSet    обычная


    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        userRepository.save(user);
        return user;
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }

}
