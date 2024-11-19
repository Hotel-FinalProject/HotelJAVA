package com.example.backend.repository;

import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByName(String name);
    List<User> findByLastLoginTimeBeforeAndIsActiveTrue(LocalDateTime date);

    List<User> findByRole(String role_user);

    List<User> findByRoleAndNameContainingIgnoreCaseOrRoleAndEmailContainingIgnoreCase(String role1, String name,String role2,String email);

    int countByRole(String role_user);

    int countByRoleAndIsActive(String role_user, boolean b);
}