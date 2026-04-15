package edu.oosd.restservices.RestApi.controllers;

import edu.oosd.restservices.RestApi.models.Role;
import edu.oosd.restservices.RestApi.models.User;
import edu.oosd.restservices.RestApi.repository.UserRepository;
//import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc; // Mocks HTTP requests

    @MockitoBean //
    private UserRepository userRepository; //Mocks db

    @Autowired
    private ObjectMapper objectMapper; // Converts objects to JSON

    @Test
    @DisplayName("Testing User Methods")
    void testUserModelMethods() {
        User user = new User();
        user.setUserId(1);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setUsername("username");
        user.setRole(Role.valueOf("ADMIN"));

        assertEquals(1, user.getUserId());
        assertEquals("firstName", user.getFirstName());
        assertEquals("lastName", user.getLastName());
        assertEquals("username", user.getUsername());
        assertEquals(Role.valueOf("ADMIN"), user.getRole());
    }

    @Test
    @DisplayName("Get Users Should Return All Users")
    void getUsers_ReturnAllUsers() throws Exception {
        User user1 = new User();
        user1.setUsername("iz");
        User user2 = new User();
        user2.setUsername("rf");

        // Return list
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("iz"));
    }

    @Test
    @DisplayName("Create User: Should Return Created When Username Is New")
    void createUser_UsernameIsNew() throws Exception {
        User newUser = new User();
        newUser.setUsername("rs");
        newUser.setPassword("newpassword");

        // Username not found
        when(userRepository.findByUsername("rs")).thenReturn(Optional.empty());
        // Save the user
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("rs"));
    }

    @Test
    @DisplayName("Create User Should Return Conflict")
    void createUser_UsernameExists() throws Exception {
        User existingUser = new User();
        existingUser.setUsername("doctor s");

        // doctor s already exists
        when(userRepository.findByUsername("doctor s")).thenReturn(Optional.of(existingUser));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingUser)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Update User Should Not Update Password")
    void updateUser_PasswordIsEmpty() throws Exception {
        int userId = 1;
        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setPassword("oldPassword");

        User updateRequest = new User();
        updateRequest.setFirstName("UpdatedName");
        updateRequest.setPassword(""); // Empty password test

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        mockMvc.perform(put("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Update User Should Return 404")
    void updateUser_UserDoesNotExist() throws Exception {
        int id = 99;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new User())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update User Field Only Provided Fields")
    void updateProvidedUserFields() throws Exception {
        int id = 1;
        User existing = new User();
        existing.setUserId(id);
        existing.setFirstName("oldName");
        existing.setPassword("oldPassword");

        User partialUpdate = new User();
        partialUpdate.setFirstName("newName");
        partialUpdate.setPassword("newPassword"); // will check hash branch

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(patch("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("newName"));
    }

    @Test
    @DisplayName("Update User Fields Skip Null Fields")
    void updateUserFieldNotNull() throws Exception {
        int id = 1;
        User existing = new User();
        existing.setUserId(id);
        existing.setFirstName("Iz");
        existing.setRole(Role.valueOf("ADMIN"));

        User partialUpdate = new User();
        partialUpdate.setLastName("newLastName");

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        mockMvc.perform(patch("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("newLastName"))
                .andExpect(jsonPath("$.firstName").value("Iz"));
    }

    @Test
    @DisplayName("Update User Should Skip Password")
    void updateUser_PasswordIsBlankString() throws Exception {
        int id = 1;
        User existing = new User();
        existing.setPassword("original_hash");

        User updateRequest = new User();
        updateRequest.setPassword("");

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenReturn(existing);

        mockMvc.perform(put("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Update User Should Update All Basic Fields")
    void updateUser_BasicFields() throws Exception {
        int id = 1;
        User existing = new User();

        User updateRequest = new User();
        updateRequest.setFirstName("newFirstName");
        updateRequest.setLastName("newLastName");
        updateRequest.setUsername("newUsername");
        updateRequest.setRole(Role.valueOf("DOCTOR"));

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        mockMvc.perform(put("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newUsername"))
                .andExpect(jsonPath("$.role").value("DOCTOR"));
    }

    @Test
    @DisplayName("Update User Field Should Return 404 : User Not Found")
    void updateUserField_WhenUserNotFound() throws Exception {
        int id = 999;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new User())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update User Field Should Update Date Fields")
    void updateUserDateFields() throws Exception {
        int id = 1;
        User existing = new User();
        existing.setUserId(id);

        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        User partialUpdate = new User();
        partialUpdate.setLastLogin(now);
        partialUpdate.setLastPasswordChange(now);

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        mockMvc.perform(patch("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastLogin").exists());
    }

    @Test
    @DisplayName("Update User Fields Should Skip Most Fields")
    void updateUserField_SkipFields() throws Exception {
        int id = 1;
        User existing = new User();
        existing.setUserId(id);
        existing.setUsername("oldUsername");
        existing.setFirstName("oldFirstName");

        User partialUpdate = new User();
        partialUpdate.setUsername("newUsername");

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        mockMvc.perform(patch("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newUsername"))
                .andExpect(jsonPath("$.firstName").value("oldFirstName"));
    }

    @Test
    @DisplayName("Update User Field Password Empty")
    void updateUserField_PasswordEmpty() throws Exception {
        int id = 1;
        User existing = new User();
        existing.setPassword("original_hash");

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenReturn(existing);

        mockMvc.perform(patch("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete User Should Return 404")
    void deleteUserUserDoesNotExist() throws Exception {
        int userId = 999;
        when(userRepository.existsById(userId)).thenReturn(false);

        mockMvc.perform(delete("/users/" + userId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete User Should Return No Content")
    void deleteUserNoContent() throws Exception {
        int id = 1;
        when(userRepository.existsById(id)).thenReturn(true);

        mockMvc.perform(delete("/users/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete User Should Return 404")
    void deleteUser_IdIsMissing() throws Exception {
        int id = 999;
        when(userRepository.existsById(id)).thenReturn(false);

        mockMvc.perform(delete("/users/" + id))
                .andExpect(status().isNotFound());
    }
}