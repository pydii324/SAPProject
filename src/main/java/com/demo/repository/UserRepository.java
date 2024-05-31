package com.demo.repository;

import com.demo.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // Repository ни дава обикновена функционалност с операциите CRUD, без да сме ги дефинирали.
    // Затова тук няма нищо.
    Optional<UserEntity> findByFirstName(String firstName);
    UserEntity findByEmailIgnoreCase(String email);
    UserEntity findByUsername(String username);
}
