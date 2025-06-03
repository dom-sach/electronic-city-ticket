package pl.biletmiejski.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.biletmiejski.backend.model.Token;
import pl.biletmiejski.backend.model.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends JpaRepository<Token, Long> {
    // find token
    Optional<Token> findByToken(String token);

    // find all user's tokens
    @Query("SELECT t FROM Token t WHERE t.user.id = :userId AND t.expired = false AND t.revoked = false")
    List<Token> findAllValidTokensByUser(@Param("userId") Long userId);
}
