package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.service.emailService.ActivationEmailService;
import pl.coderslab.javaGym.service.userService.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
public class RegistrationController {

    private UserService userService;
    private ActivationEmailService activationEmailService;

    @Autowired
    public RegistrationController(UserService userService,
                                  ActivationEmailService activationEmailService) {
        this.userService = userService;
        this.activationEmailService = activationEmailService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody @Valid User user) {
        return userService.save(user, false);
    }

    @GetMapping("/confirm-account")
    public Boolean activateUserAccount(@RequestParam @NotBlank String param) {
        return activationEmailService.activateUserAccount(param);
    }

}
