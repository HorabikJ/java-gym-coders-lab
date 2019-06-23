package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderslab.javaGym.service.UserService;

import javax.validation.constraints.NotBlank;

@RestController
@Validated
public class EmailController {

    private UserService userService;

    @Autowired
    public EmailController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/change-email")
    public Boolean confirmEmailChange(@RequestParam @NotBlank String param,
                                      @RequestParam @NotBlank String newEmail) {
        return userService.confirmUserEmailChange(param, newEmail);
    }

}
