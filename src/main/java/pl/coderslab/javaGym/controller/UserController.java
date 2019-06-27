package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.service.userService.UserService;

import javax.validation.constraints.*;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/show-details")
    public User getUser(@RequestParam @Min(1) Long userId) {
        return userService.getAuthenticatedUserById(userId);
    }

    @PatchMapping("/change-password")
    public Boolean changeUserPassword(@RequestParam @Min(1) Long userId,
         @NotBlank(message = "*Please provide your old password.")
         @RequestParam String oldPassword,
         @Size(min = 5, message = "*Your password must have at least 5 characters.")
         @NotBlank(message = "*Please provide your old password.")
         @RequestParam String newPassword) {
        return userService.changePassword(userId, oldPassword, newPassword);
    }

    @PatchMapping("/newsletter")
    public User changeNewsletterConsent(@RequestParam @Min(1) Long userId,
                                        @RequestParam @NotNull Boolean newsletter) {
        return userService.changeNewsletterConsent(userId, newsletter);
    }

    @PatchMapping("/change-names")
    public User changeUserFirstAndLastName(@RequestParam @Min(1) Long userId,
                @NotBlank(message = "*Name can not be empty.") @RequestParam String firstName,
                @NotBlank(message = "*Last name can not be empty.") @RequestParam String lastName) {
        return userService.changeFirstAndLastName(userId, firstName, lastName);
    }

    @PostMapping("/change-email")
    public Boolean changeUserEmail(@RequestParam @Min(1) Long userId,
                                @Email(message = "*Please provide a valid confirmationEmail.")
                                @NotBlank(message = "*Please provide an confirmationEmail.")
                                @RequestParam String newEmail) {
        return userService.sendUserEmailChangeMessage(userId, newEmail);
    }

//    User can do:
//    - show his details
//    - change his password
//    - change newsletter consent
//    - change his names
//    - change his email
//    TODO
//     - reserve classes,
//     - cancel classes,
//     - show reserved classes,
//     - check response status for other methods,

}
