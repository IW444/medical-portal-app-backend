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

import java.util.Optional;

@RestController
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;


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
        if(BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())){
            return ResponseEntity.ok(user);
        }

        //If the username is in the database, but the entered password is incorrect, this
        //login attempt is invalid.
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }
}
