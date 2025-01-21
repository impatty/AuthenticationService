package org.authenticationservice.authenticationservice.Repository;

import org.authenticationservice.authenticationservice.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
    public User save(User user);
}
