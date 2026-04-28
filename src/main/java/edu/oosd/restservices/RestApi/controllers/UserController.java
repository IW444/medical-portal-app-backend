package edu.oosd.restservices.RestApi.controllers;

import edu.oosd.restservices.RestApi.models.User;
import edu.oosd.restservices.RestApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing Users.
 * Provides functions for creating, retrieving, updating, and deleting users
 * within the medical portal application.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * Data access object for performing CRUD operations on the User table.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves a list of all users registered in the database.
     * * @return A list of all User objects.
     */
    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Creates a new user in the system.
     * Validates that the username is unique and hashes the password before storing.
     * * @param user The user object to be created, passed in the request body.
     * @return A ResponseEntity containing the saved user and HTTP 201, created,
     * or HTTP 409, conflict, if the username already exists.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        //Check if the chosen username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else {
            user.setPassword(User.hashPassword(user.getPassword()));
            User savedUser = userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        }
    }

    /**
     * Performs a full update of an existing user.
     * * @param id The unique ID of the user to update.
     * @param updatedUser The User object containing the new details.
     * @return A ResponseEntity with the updated user and HTTP 200, OK,
     * or HTTP 404, Not Found, if the ID does not exist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id,
                                           @RequestBody User updatedUser) {

        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setRole(updatedUser.getRole());
        existingUser.setLastLogin(updatedUser.getLastLogin());
        existingUser.setLastPasswordChange(updatedUser.getLastPasswordChange());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(User.hashPassword(updatedUser.getPassword()));
        }

        User savedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * Performs a partial update on a user.
     * This is used for updating specific fields like a password or a role
     * without sending the entire user object.
     * * @param id The unique ID of the user to partially update.
     * @param partialUser A User object containing only the fields that need to be changed.
     * @return A ResponseEntity with the updated user and HTTP 200, OK,
     * or HTTP 404, Not Found, if the ID does not exist.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUserField(@PathVariable Integer id,
                                                @RequestBody User partialUser) {

        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        if (partialUser.getFirstName() != null) {
            existingUser.setFirstName(partialUser.getFirstName());
        }
        if (partialUser.getLastName() != null) {
            existingUser.setLastName(partialUser.getLastName());
        }
        if (partialUser.getUsername() != null) {
            existingUser.setUsername(partialUser.getUsername());
        }
        if (partialUser.getPassword() != null && !partialUser.getPassword().isEmpty()) {
            existingUser.setPassword(User.hashPassword(partialUser.getPassword()));
        }
        if (partialUser.getRole() != null) {
            existingUser.setRole(partialUser.getRole());
        }
        if (partialUser.getLastLogin() != null) {
            existingUser.setLastLogin(partialUser.getLastLogin());
        }
        if (partialUser.getLastPasswordChange() != null) {
            existingUser.setLastPasswordChange(partialUser.getLastPasswordChange());
        }

        User savedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * Deletes a user from the database.
     * * @param id The unique ID of the user to delete.
     * @return A ResponseEntity with HTTP 204, No Content,
     * or HTTP 404, Not Found, if the user does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {

        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
