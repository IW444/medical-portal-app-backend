package edu.oosd.restservices.RestApi.controllers;

import edu.oosd.restservices.RestApi.models.User;
import edu.oosd.restservices.RestApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    //Login
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User loginRequest) {
        //If the username matches one in the database, we will get it here.
        // Otherwise there is no return from the Optional function.
        Optional<User> foundUser = userRepository.findByUsername(loginRequest.getUsername());

        //If the username doesn't match our database, this login attempt is invalid.
        if (foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //Otherwise unwrap our optional object to get the user:
        User user = foundUser.get();


        //If the username is in the database and the password matches, return the user
        //object.  The frontend will navigate to the appropriate dashboard for the role.
        if(user.getPassword().equals(User.hashPassword(loginRequest.getPassword()))){
            return ResponseEntity.ok(user);
        }

        //If the username is in the database, but the entered password is incorrect, this
        //login attempt is invalid.
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {

        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
