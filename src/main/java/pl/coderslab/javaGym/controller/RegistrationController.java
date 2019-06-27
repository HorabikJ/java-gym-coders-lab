package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.emailSender.EmailSender;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.error.customException.EmailSendingException;
import pl.coderslab.javaGym.service.confirmationEmailService.ActivationEmailService;
import pl.coderslab.javaGym.service.userService.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/register")
@Validated
public class RegistrationController {

    private UserService userService;
    private ActivationEmailService activationEmailService;
    private EmailSender emailSender;

    @Autowired
    public RegistrationController(UserService userService,
                                  ActivationEmailService activationEmailService,
                                  EmailSender emailSender) {
        this.userService = userService;
        this.activationEmailService = activationEmailService;
        this.emailSender = emailSender;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody @Valid User user) {
        return userService.save(user, false);
    }

    @GetMapping("/confirm-account")
    public Boolean activateUserAccount(@RequestParam @NotBlank String param) {
        User activatedUser = activationEmailService.activateUserAccount(param);
        sendWelcomeEmail(activatedUser);
        return (activatedUser != null);
    }

    private void sendWelcomeEmail(User activatedUser) {
        if (activatedUser != null) {
            try {
                emailSender.sendUserWelcomeEmail(activatedUser);
            } catch (MailException e) {
                throw new EmailSendingException();
            }
        }
    }
}
