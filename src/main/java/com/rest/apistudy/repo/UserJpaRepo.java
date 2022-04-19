package com.rest.apistudy.repo;

import com.rest.apistudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepo extends JpaRepository<User, Long> {}