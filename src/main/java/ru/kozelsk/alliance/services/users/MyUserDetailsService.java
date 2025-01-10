package ru.kozelsk.alliance.services.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kozelsk.alliance.models.users.User;
import ru.kozelsk.alliance.repositories.users.UserRepository;
import ru.kozelsk.alliance.utils.MyUserDetails;

import java.util.Optional;

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


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public boolean checkPassword(String rawPassword, String encoderPassword) {
        return passwordEncoder.matches(rawPassword, encoderPassword);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
}
