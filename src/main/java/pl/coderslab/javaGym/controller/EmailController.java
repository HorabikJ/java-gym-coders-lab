package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderslab.javaGym.repository.ChangeEmailDetailsRepository;
import pl.coderslab.javaGym.service.emailService.ChangeEmailDetailsService;
import pl.coderslab.javaGym.service.userService.UserService;

import javax.validation.constraints.NotBlank;

@RestController
@Validated
public class EmailController {

    private UserService userService;
    private ChangeEmailDetailsService changeEmailDetailsService;

    @Autowired
    public EmailController(UserService userService,
                           ChangeEmailDetailsService changeEmailDetailsService) {
        this.userService = userService;
        this.changeEmailDetailsService = changeEmailDetailsService;
    }

    @GetMapping("/change-email")
    public Boolean confirmEmailChange(@RequestParam @NotBlank String param) {
        return changeEmailDetailsService.confirmEmailChange(param);
    }

}
