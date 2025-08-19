package iuh.se.team.webbookstore_backend.dao;

import iuh.se.team.webbookstore_backend.entities.PasswordResetToken;
import iuh.se.team.webbookstore_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByUserAndOtp(User user, String otp);
    void deleteByUser(User user);
}