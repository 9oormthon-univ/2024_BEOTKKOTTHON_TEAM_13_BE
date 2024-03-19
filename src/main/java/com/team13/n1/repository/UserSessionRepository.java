package com.team13.n1.repository;

import com.team13.n1.entity.UserSession;
import org.springframework.data.repository.CrudRepository;

// Redis와 연결될 레포지토리
public interface UserSessionRepository extends CrudRepository<UserSession, String> { }
