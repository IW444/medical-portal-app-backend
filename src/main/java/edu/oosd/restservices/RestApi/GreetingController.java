package edu.oosd.restservices.RestApi;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
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
        Greeting g = new Greeting(
                counter.incrementAndGet(),
                String.format(template, name)
        );
        return ResponseEntity.status(HttpStatus.OK).body(g);
    }

            Map<String, String> links = new HashMap<>();
            links.put("home", "http://localhost:8080/");
            links.put("greeting", "http://localhost:8080/greeting");
    @GetMapping("/")
    public ResponseEntity<HomeResponse> home() {

        Map<String, String> links = new LinkedHashMap<>();
        links.put("greeting",         "http://localhost:8080/greeting");
        links.put("greeting_custom",  "http://localhost:8080/greeting?name=YourName");
        links.put("self",             "http://localhost:8080/");

            Greeting errorGreeting = new Greeting(
                    counter.incrementAndGet(),
                    "Name cannot be empty",
                    links
            );
        HomeResponse response = new HomeResponse(
                200,
                String.format(template_home, "/"),
                links
        );

            return new ResponseEntity<>(errorGreeting, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

        Map<String, String> links = new HashMap<>();
        links.put("home", "http://localhost:8080/");
    @GetMapping("/unknown")
    public ResponseEntity<HomeResponse> notFound() {

        Map<String, String> links = new LinkedHashMap<>();
        links.put("home",     "http://localhost:8080/");
        links.put("greeting", "http://localhost:8080/greeting");

        Greeting greeting = new Greeting(
                counter.incrementAndGet(),
                String.format(template, name), links
        HomeResponse response = new HomeResponse(
                404,
                "The resource you are looking for was not found.",
                links
        );

        return new ResponseEntity<>(greeting, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @GetMapping("/")
    public ResponseEntity<Greeting> home() {
    @GetMapping("/created")
    public ResponseEntity<HomeResponse> created() {

        Map<String, String> links = new HashMap<>();
        links.put("home", "http://localhost:8080/");
        Map<String, String> links = new LinkedHashMap<>();
        links.put("home",     "http://localhost:8080/");
        links.put("greeting", "http://localhost:8080/greeting");

        Greeting greeting = new Greeting(
                counter.incrementAndGet(),
                String.format(template_home, "Home Page"), links
        HomeResponse response = new HomeResponse(
                201,
                "Resource successfully created.",
                links
        );

        return new ResponseEntity<>(greeting, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
