package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.dataTransferObject.UserDto;
import pl.coderslab.javaGym.emailSender.EmailSender;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.entityDtoConverter.UserEntityDtoConverter;
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
    private UserEntityDtoConverter userEntityDtoConverter;

    @Autowired
    public RegistrationController(UserService userService,
                                  ActivationEmailService activationEmailService,
                                  EmailSender emailSender,
                                  UserEntityDtoConverter userEntityDtoConverter) {
        this.userService = userService;
        this.activationEmailService = activationEmailService;
        this.emailSender = emailSender;
        this.userEntityDtoConverter = userEntityDtoConverter;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto registerUser(@RequestBody @Valid UserDto userDto) {
        User user = userEntityDtoConverter.convertUserToEntity(userDto);
        return userEntityDtoConverter.convertUserToDto(userService.save(user, false));
    }

    @GetMapping("/confirm-account")
    public Boolean activateUserAccount(@RequestParam @NotBlank String param) {
        User activatedUser = activationEmailService.activateUserAccount(param);
        sendWelcomeEmail(activatedUser);
        return (activatedUser != null);
    }

    private void sendWelcomeEmail(User activatedUser) {
        if (activatedUser != null) {
            emailSender.sendUserWelcomeEmail(activatedUser);
        }
    }
}
