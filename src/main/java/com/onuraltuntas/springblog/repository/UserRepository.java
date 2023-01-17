package com.onuraltuntas.springblog.repository;

import com.onuraltuntas.springblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<Object> findUserByEmail(String userEmail);
}
