package java.com.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.com.security.entity.Role;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findByName(String name);
}
