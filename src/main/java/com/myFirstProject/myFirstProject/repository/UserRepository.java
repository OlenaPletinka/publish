package com.myFirstProject.myFirstProject.repository;

import com.myFirstProject.myFirstProject.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
     Optional<User> getUserByLoginAndPassword(String login, String password);
     boolean existsByLogin(String login);
}
