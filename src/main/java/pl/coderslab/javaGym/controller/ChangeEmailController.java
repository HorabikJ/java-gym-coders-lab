package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderslab.javaGym.service.confirmationEmailService.ChangeEmailDetailsService;

import javax.validation.constraints.NotBlank;

@RestController
@Validated
public class ChangeEmailController {

    private ChangeEmailDetailsService changeEmailDetailsService;

    @Autowired
    public ChangeEmailController(ChangeEmailDetailsService changeEmailDetailsService) {
        this.changeEmailDetailsService = changeEmailDetailsService;
    }

    @GetMapping("/change-email")
    public Boolean confirmEmailChange(@RequestParam @NotBlank String param) {
        return changeEmailDetailsService.confirmEmailChange(param);
    }

}
