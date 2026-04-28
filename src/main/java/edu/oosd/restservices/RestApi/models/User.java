package edu.oosd.restservices.RestApi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import org.mindrot.jbcrypt.BCrypt;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity class representing a User in the database.
 * This class maps to the users table and includes all user
 * attributes and features such as password hashing and JSON
 * access control.
 */
@Entity
@Table(name = "users")
public class User {

    /** Unique identifier for the user, auto-generated in the database. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Integer userId;

    /** The user's first name. */
    @Column(name = "firstName")
    private String firstName;

    /** The user's last name. */
    @Column(name = "lastName")
    private String lastName;

    /** Unique login id for the user, this can't be null. */
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    /** The hashed version of the user's password. */
    @Column(name = "password")
    //Don't display passwords in the JSON response
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /** The role assigned to the user. */
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    /** Timestamp of the user's most recent successful login. */
    @Column(name = "lastLogin")
    private LocalDateTime lastLogin;

    /** Timestamp recording the last time the user's password was changed. */
    @Column(name = "lastPasswordChange")
    private LocalDateTime lastPasswordChange;

    /**
     * Default no arg constructor.
     */
    public User() {
    }

    /**
     * Full arg constructor for creating a User.
     * @param userId             The unique ID of the user.
     * @param firstName          The user's first name.
     * @param lastName           The user's last name.
     * @param username           The unique username.
     * @param password           The hashed password string.
     * @param role               The user's role.
     * @param lastLogin          The last login timestamp.
     * @param lastPasswordChange The last password change timestamp.
     */
    public User(Integer userId, String firstName, String lastName, String username, String password, Role role, LocalDateTime lastLogin, LocalDateTime lastPasswordChange) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.lastLogin = lastLogin;
        this.lastPasswordChange = lastPasswordChange;
    }

    /**
     * Hashes a password using the BCrypt hashing function.
     * @param password The password to hash.
     * @return A secure hashed string.
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /** @return The unique user id. */
    public Integer getUserId() {
        return userId;
    }

    /** @param userId The unique user id to set. */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /** @return The user's first name. */
    public String getFirstName() {
        return firstName;
    }

    /** @param firstName The user's first name to set. */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /** @return The user's last name. */
    public String getLastName() {
        return lastName;
    }

    /** @param lastName The user's last name to set. */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /** @return The unique username. */
    public String getUsername() {
        return username;
    }

    /** @param username The unique username to set. */
    public void setUsername(String username) {
        this.username = username;
    }

    /** @return The hashed password string. */
    public String getPassword() {
        return password;
    }

    /** @param password The hashed password to set. */
    public void setPassword(String password) {
        this.password = password;
    }

    /** @return The Role of the user. */
    public Role getRole() {
        return role;
    }

    /** @param role The Role of the user to set. */
    public void setRole(Role role) {
        this.role = role;
    }

    /** @return The user's last login timestamp. */
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    /** @param lastLogin The user's last login timestamp to set. */
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    /** @return The user's last password change timestamp. */
    public LocalDateTime getLastPasswordChange() {
        return lastPasswordChange;
    }

    /** @param lastPasswordChange The user's last password change timestamp to set. */
    public void setLastPasswordChange(LocalDateTime lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }
}
