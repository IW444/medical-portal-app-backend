package edu.oosd.restservices.RestApi.controllers;

import edu.oosd.restservices.RestApi.models.User;
import edu.oosd.restservices.RestApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.mindrot.jbcrypt.BCrypt;
import java.time.LocalDateTime;

import java.util.Optional;

/**
 * REST controller responsible for user authentication.
 *
 * <p>Handles login requests by verifying credentials against the database.
 * Passwords are compared using BCrypt hashing, so plaintext passwords are
 * never stored or directly compared.</p>
 *
 * <p>Base URL: {@code /login}</p>
 */

@RestController
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Authenticates a user based on their username and password.
     *
     * <p>The login process follows these steps:</p>
     * <ol>
     *   <li>Look up the user by username in the database.</li>
     *   <li>Return {@code 401 Unauthorized} if no matching username is found.</li>
     *   <li>Use {@link BCrypt#checkpw} to verify the provided password against
     *       the stored hash.</li>
     *   <li>Return the full {@link User} object on success, allowing the frontend
     *       to route the user to the appropriate role-based dashboard.</li>
     *   <li>Return {@code 401 Unauthorized} if the password does not match.</li>
     * </ol>
     *
     * @param loginRequest a {@link User} object containing the username and plaintext
     *                     password submitted by the client
     * @return {@code 200 OK} with the authenticated {@link User} if credentials are valid,
     *         or {@code 401 Unauthorized} if the username is not found or the password
     *         is incorrect
     */


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
        //object after storing the last login time.  The frontend will navigate to the
        // appropriate dashboard for the role.
        if(BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())){
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }

        //If the username is in the database, but the entered password is incorrect, this
        //login attempt is invalid.
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }
}
