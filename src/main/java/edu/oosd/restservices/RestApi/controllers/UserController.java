package edu.oosd.restservices.RestApi.controllers;

import edu.oosd.restservices.RestApi.models.User;
import edu.oosd.restservices.RestApi.repository.MySqlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private MySqlRepository mySqlRepository;

    @GetMapping
    public List<User> getUsers() {
        return mySqlRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setPassword(User.hashPassword(user.getPassword()));
        User savedUser = mySqlRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id,
                                           @RequestBody User updatedUser) {

        User existingUser = mySqlRepository.findById(id).orElse(null);
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

        User savedUser = mySqlRepository.save(existingUser);
        return ResponseEntity.ok(savedUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUserField(@PathVariable Integer id,
                                                @RequestBody User partialUser) {

        User existingUser = mySqlRepository.findById(id).orElse(null);
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

        User savedUser = mySqlRepository.save(existingUser);
        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {

        if (!mySqlRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        mySqlRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
