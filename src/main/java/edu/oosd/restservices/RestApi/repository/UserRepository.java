package edu.oosd.restservices.RestApi.repository;

import edu.oosd.restservices.RestApi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}

