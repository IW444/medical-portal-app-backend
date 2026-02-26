package edu.oosd.restservices.RestApi;

import edu.oosd.restservices.RestApi.models.User;
import edu.oosd.restservices.RestApi.repository.MySqlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PortalController {

    @Autowired
    MySqlRepository mySqlRepository;

    @GetMapping("/users")
    public List<User> getUsers(){
        return mySqlRepository.findAll();
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {

        user.setPassword(User.hashPassword(user.getPassword()));

        User savedUser = mySqlRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedUser); // 201 Created
    }

    @PutMapping("users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id,
                                           @RequestBody User updatedUser) {

        User existingUser = mySqlRepository.findById(id).orElse(null);

        if (existingUser == null) {
            return ResponseEntity.notFound().build(); // 404
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

        return ResponseEntity.ok(savedUser); // 200 OK
    }

    @PatchMapping("users/{id}")
    public ResponseEntity<User> updateUserField(@PathVariable Integer id,
                                                @RequestBody User partialUser) {

        User existingUser = mySqlRepository.findById(id).orElse(null);

        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        // Only update fields that are NOT null
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

    @DeleteMapping("users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {

        if (!mySqlRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // 404
        }

        mySqlRepository.deleteById(id);

        return ResponseEntity.noContent().build(); // 204 No Content
    }


    /*
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        // Hash the password before saving
        user.setPassword(User.hashPassword(user.getPassword()));
        return mySqlRepository.save(user);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Integer id) {
        User user = mySqlRepository.findById(id).orElse(null);
        if (user != null) {
            mySqlRepository.deleteById(id);
            return "User with ID " + id + " has been deleted.";
        } else {
            return "User with ID " + id + " not found.";
        }
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        return mySqlRepository.findById(id).map(user -> {
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setUsername(updatedUser.getUsername());
            // Only update password if provided
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(User.hashPassword(updatedUser.getPassword()));
            }
            user.setRole(updatedUser.getRole());
            user.setLastLogin(updatedUser.getLastLogin());
            user.setLastPasswordChange(updatedUser.getLastPasswordChange());
            return mySqlRepository.save(user);
        }).orElseGet(() -> {
            // If user not found, optionally create a new one
            updatedUser.setUserId(id);
            if (updatedUser.getPassword() != null) {
                updatedUser.setPassword(User.hashPassword(updatedUser.getPassword()));
            }
            return mySqlRepository.save(updatedUser);
        });
    } */

}
