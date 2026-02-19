package edu.oosd.restservices.RestApi;

import edu.oosd.restservices.RestApi.models.User;
import edu.oosd.restservices.RestApi.repository.MySqlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PortalController {

    @Autowired
    MySqlRepository mySqlRepository;

    @GetMapping("/users")
    public List<User> getUsers(){
        return mySqlRepository.findAll();
    }
}
