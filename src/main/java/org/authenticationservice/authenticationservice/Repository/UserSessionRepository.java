package org.authenticationservice.authenticationservice.Repository;

import org.authenticationservice.authenticationservice.Models.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    public Optional<UserSession> findUserSessionById(long id);
    public UserSession save(UserSession userSession);
}
