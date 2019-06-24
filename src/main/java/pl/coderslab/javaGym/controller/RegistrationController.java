package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
public class RegistrationController {

    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody @Valid User user) {
        return userService.save(user, false);
    }

//    TODO set link expiration time
    @GetMapping("/confirm-account")
    public Boolean userAccountActivation(@RequestParam @NotBlank String param) {
        return userService.activateUserAccount(param);
    }

}
