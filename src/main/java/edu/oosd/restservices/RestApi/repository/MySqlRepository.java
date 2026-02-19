package edu.oosd.restservices.RestApi.repository;

import edu.oosd.restservices.RestApi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MySqlRepository extends JpaRepository<User, Integer> {
}
