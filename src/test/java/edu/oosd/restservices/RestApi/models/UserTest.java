package edu.oosd.restservices.RestApi.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import org.mindrot.jbcrypt.BCrypt;

class UserTest {
    private static User newGuy;

//--------------------------
//Mock Patient for test usage
//--------------------------
    @BeforeEach
    public void loadTestUser(){
        newGuy = new User(150,"Greg","Groog", "ggroog1", "password",
                Role.PATIENT, LocalDateTime.of(2026,2,10,2,36), LocalDateTime.of(2026,2,10,2,50));
    }

//--------------------------
//Password Hash Tests
//--------------------------
    @Test
    @DisplayName("Hashed password not null or empty confirmation")
    void hashPassword() {
        //Template password to test with
        String password = "mySecretPassword";
        String hashed = User.hashPassword(password);

        //Verify that the hashed result is not null or empty
        assertNotNull(hashed, "Hashed password should not be null");
        assertFalse(hashed.isEmpty(), "Hashed password should not be empty");

        //Verify that the hashed password is different from the original password
        assertNotEquals(password, hashed, "Hashed password should be changed from the original");

        // Verify the password against the hash
        assertTrue(BCrypt.checkpw(password, hashed), "The hashed password should only match the given password");
    }

//--------------------------
//GETTER Tests
//--------------------------
    @Test
    @DisplayName("UserID Confirmation")
    void getUserId() {
        //Verify that the User ID matches the correct user
        assertEquals(150, newGuy.getUserId(), "Different Guy in there. Figure it out");
    }

    @Test
    @DisplayName("First name Confirmation")
    void getFirstName() {
        //Verify that the user's first name matches
        assertEquals("Greg", newGuy.getFirstName(), "Incorrect First name found.");
    }

    @Test
    @DisplayName("Last name Confirmation")
    void getLastName() {
        //Verify that the user's last name matches
        assertEquals("Groog", newGuy.getLastName(), "Incorrect Last name found.");
    }

    @Test
    @DisplayName("Username Confirmation")
    void getUsername() {
        //Verify that the user's username matches
        assertEquals("ggroog1", newGuy.getUsername(), "Incorrect username found.");
    }

    @Test
    @DisplayName("Password Confirmation")
    void getPassword() {
        //Verify that the user's password matches
        assertEquals("password", newGuy.getPassword(), "Incorrect password found.");
    }

    @Test
    @DisplayName("Role Confirmation")
    void getRole() {
        //Verify that the user has been assigned to the correct role
        assertEquals(Role.PATIENT, newGuy.getRole(), "Incorrect role found.");
    }

    @Test
    @DisplayName("Most recent login Confirmation")
    void getLastLogin() {
        //Verify that the last login time has been stored properly
        assertEquals(LocalDateTime.of(2026,2,10,2,36),newGuy.getLastLogin(),
                "Incorrect time and date logged.");
    }

    @Test
    void getLastPasswordChange() {
    }

//--------------------------
//SETTER Tests
//--------------------------

//    @Test
//    void setUserId() {
//    }

    @Test
    @DisplayName("Update user's first name")
    void setFirstName() {
        newGuy.setFirstName("George");
        assertEquals("George", newGuy.getFirstName(), "First name was not updated :( ");
    }

    @Test
    @DisplayName("Update user's last name")
    void setLastName() {
        newGuy.setLastName("Grungleson");
        assertEquals("Grungleson", newGuy.getLastName(),"Last name not updated :( ");
    }

    @Test
    @DisplayName("Update username")
    void setUsername() {
        newGuy.setUsername("George2004");
        assertEquals("George2004", newGuy.getUsername(),"Failed to set Username.");
    }

    @Test
    @DisplayName("Update user's password")
    void setPassword() {
        newGuy.setPassword("gg1234");
        assertEquals("gg1234", newGuy.getPassword(),"Failed to set new password.");
    }

//    @Test
//    void setRole() {
//    }

//    @Test
//    void setLastLogin() {
//    }

//    @Test
//    void setLastPasswordChange() {
//    }
}