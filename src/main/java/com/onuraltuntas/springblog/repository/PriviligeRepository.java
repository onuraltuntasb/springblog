package com.onuraltuntas.springblog.repository;

import com.onuraltuntas.springblog.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriviligeRepository extends JpaRepository<Privilege,Long> {
  Privilege findByName(String name);
}
