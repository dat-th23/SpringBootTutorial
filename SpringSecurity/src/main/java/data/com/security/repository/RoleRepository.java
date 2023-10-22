package data.com.security.repository;

import data.com.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
