package edu.oosd.restservices.RestApi.repository;

import edu.oosd.restservices.RestApi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//From JpaRepository (interface of Spring Data),the UserRepository
// interface inherits the requirement of having many methods including:
// findAll(), findById(), save(), and delete()
public interface UserRepository extends JpaRepository<User, Integer> {

    //Optional makes it so we don't crash the system if we try to
    //find something using a username not in the database.
    Optional<User> findByUsername(String username);

}

