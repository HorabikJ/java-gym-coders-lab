package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.entity.User;
import pl.coderslab.javaGym.service.UserService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
        return userService.getUserById(userId);
    }

    @PatchMapping("/change-password")
    public Boolean changeUserPassword(@RequestParam @Min(1) Long userId,
                                     @Size(min = 5) @NotBlank @RequestParam String oldPassword,
                                     @Size(min = 5) @NotBlank @RequestParam String newPassword) {
        return userService.changePassword(userId, oldPassword, newPassword);
    }

    @PatchMapping("/newsletter")
    public User changeNewsletterConsent(@RequestParam @Min(1) Long userId,
                                        @RequestParam @NotNull Boolean newsletter) {
        return userService.changeNewsletterConsent(userId, newsletter);
    }

    @PatchMapping("/change-names")
    public User changeUserFirstAndLastName(@RequestParam @Min(1) Long userId,
                                @NotBlank @RequestParam String firstName,
                                @NotBlank @RequestParam String lastName) {
        return userService.changeFirstAndLastName(userId, firstName, lastName);
    }

//    TODO
//     resetPassword(situation when user forgotten his password),
//     reserve classes,
//     cancel classes,

}
