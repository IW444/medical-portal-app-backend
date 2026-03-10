package edu.oosd.restservices.RestApi;

import edu.oosd.restservices.RestApi.models.Appointment;
import edu.oosd.restservices.RestApi.models.User;
import edu.oosd.restservices.RestApi.repository.AppointmentRepository;
import edu.oosd.restservices.RestApi.repository.MySqlRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    //Logging in
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User loginRequest) {
        // 1. Find user by username
        // Note: You may need to add a findByUsername method to MySqlRepository
        User user = mySqlRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(loginRequest.getUsername()))
                .findFirst()
                .orElse(null);

        if (user != null && BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
            user.setLastLogin(LocalDateTime.now());
            mySqlRepository.save(user);
            return ResponseEntity.ok(user); // Success
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
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

    @Autowired
    AppointmentRepository appointmentRepository;

    @GetMapping("/appointments")
    public List<Appointment> getAppointments() {
        return appointmentRepository.findAll();
    }

    @PostMapping("/appointments")
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {

        // Set server-side timestamp (optional if DB defaults it)
        appointment.setTimestamp(LocalDateTime.now());

        Appointment saved = appointmentRepository.save(appointment);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/appointments/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Integer id,
                                                         @RequestBody Appointment updatedAppointment) {

        Appointment existing = appointmentRepository.findById(id).orElse(null);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        existing.setDate(updatedAppointment.getDate());
        existing.setStartTime(updatedAppointment.getStartTime());
        existing.setEndTime(updatedAppointment.getEndTime());
        existing.setPatient(updatedAppointment.getPatient());
        existing.setDoctor(updatedAppointment.getDoctor());
        existing.setTimestamp(LocalDateTime.now());

        Appointment saved = appointmentRepository.save(existing);

        return ResponseEntity.ok(saved);
    }

    @PatchMapping("/appointments/{id}")
    public ResponseEntity<Appointment> updateAppointmentField(@PathVariable Integer id,
                                                              @RequestBody Appointment partial) {

        Appointment existing = appointmentRepository.findById(id).orElse(null);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        if (partial.getDate() != null) {
            existing.setDate(partial.getDate());
        }

        if (partial.getStartTime() != null) {
            existing.setStartTime(partial.getStartTime());
        }

        if (partial.getEndTime() != null) {
            existing.setEndTime(partial.getEndTime());
        }

        if (partial.getPatient() != null) {
            existing.setPatient(partial.getPatient());
        }

        if (partial.getDoctor() != null) {
            existing.setDoctor(partial.getDoctor());
        }

        existing.setTimestamp(LocalDateTime.now());

        Appointment saved = appointmentRepository.save(existing);

        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Integer id) {

        if (!appointmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        appointmentRepository.deleteById(id);

        return ResponseEntity.noContent().build();
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
