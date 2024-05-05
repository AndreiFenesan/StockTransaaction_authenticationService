package com.example.authenticationservice.datasource.repository;

import com.example.authenticationservice.datasource.model.BasicUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasicUserRepository extends JpaRepository<BasicUser, Integer> {
}
