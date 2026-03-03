package edu.oosd.restservices.RestApi;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Greeting> handleNotFound(NoHandlerFoundException ex) {

        Map<String, String> links = new HashMap<>();
        links.put("home", "http://localhost:8080/");
        links.put("greeting", "http://localhost:8080/greeting");

        Greeting errorGreeting = new Greeting(
                0,
                "Not Found. Try the following links: ",
                links
        );

        return new ResponseEntity<>(errorGreeting, HttpStatus.NOT_FOUND);
    }
}
