package ru.kozelsk.alliance.repositories.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Repository;
import ru.kozelsk.alliance.models.users.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
//    UserDetails findByUsername(String username);

    Optional<User> findByUsername(String username);
}
