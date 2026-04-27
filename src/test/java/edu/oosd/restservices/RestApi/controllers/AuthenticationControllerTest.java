package edu.oosd.restservices.RestApi.controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import edu.oosd.restservices.RestApi.models.User;
import edu.oosd.restservices.RestApi.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc; // Mocks HTTP requests

    @MockitoBean //
    private UserRepository userRepository; //Mocks db

    @Autowired
    private ObjectMapper objectMapper; // Converts objects to JSON

    @Test
    @DisplayName("Login: Should Login User If Proper Credentials Are Given And Should Return 200")
    void login_UsernameAndPasswordValid() throws Exception {
        User user = new User();
        user.setUsername("rsmith");
        user.setPassword(BCrypt.hashpw("12345", BCrypt.gensalt()));

        // Finds username
        when(userRepository.findByUsername("rsmith")).thenReturn(Optional.of(user));

        // Checks password
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"rsmith\", \"password\":\"12345\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Login: Should NOT Login User If Password Is Wrong And Should Return 401")
    void login_WrongPassword() throws Exception {
        User user = new User();
        user.setUsername("rsmith");
        user.setPassword(BCrypt.hashpw("12345", BCrypt.gensalt()));

        // Finds username
        when(userRepository.findByUsername("rsmith")).thenReturn(Optional.of(user));

        // Checks password
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"rsmith\", \"password\":\"wrongPassword\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Login: Should NOT Login User If Username Is Not Found And Should Return 401")
    void login_InvalidUsername() throws Exception {

        // Fails to find username
        when(userRepository.findByUsername("rsmith")).thenReturn(Optional.empty());

        // Checks username (and password
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"rsmith\", \"password\":\"doesNotMatter\"}"))
                .andExpect(status().isUnauthorized());
    }
}