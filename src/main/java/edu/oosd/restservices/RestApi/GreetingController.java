package edu.oosd.restservices.RestApi;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {
    private static final String template = "Hello, %s!";
    private static final String template_home = "Hello! You are at %s";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public ResponseEntity<Greeting> greeting(
            @RequestParam(defaultValue = "World") String name) {

        if (name == null || name.trim().isEmpty()) {

            Map<String, String> links = new HashMap<>();
            links.put("home", "http://localhost:8080/");
            links.put("greeting", "http://localhost:8080/greeting");

            Greeting errorGreeting = new Greeting(
                    counter.incrementAndGet(),
                    "Name cannot be empty",
                    links
            );

            return new ResponseEntity<>(errorGreeting, HttpStatus.BAD_REQUEST);
        }

        Map<String, String> links = new HashMap<>();
        links.put("home", "http://localhost:8080/");
        links.put("greeting", "http://localhost:8080/greeting");

        Greeting greeting = new Greeting(
                counter.incrementAndGet(),
                String.format(template, name), links
        );

        return new ResponseEntity<>(greeting, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Greeting> home() {

        Map<String, String> links = new HashMap<>();
        links.put("home", "http://localhost:8080/");
        links.put("greeting", "http://localhost:8080/greeting");

        Greeting greeting = new Greeting(
                counter.incrementAndGet(),
                String.format(template_home, "Home Page"), links
        );

        return new ResponseEntity<>(greeting, HttpStatus.OK);
    }




}

