package java.com.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.com.security.entity.User;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
}
