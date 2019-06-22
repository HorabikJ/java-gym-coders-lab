package pl.coderslab.javaGym.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderslab.javaGym.service.UserService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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

    @PatchMapping("/change-password")
    public Boolean updateUserPassword(@RequestParam @Min(1) Long userId,
                                     @Size(min = 5) @NotBlank @RequestParam String oldPassword,
                                     @Size(min = 5) @NotBlank @RequestParam String newPassword) {
        return userService.changePassword(userId, oldPassword, newPassword);
    }

//    TODO resetPassword(situation when user forgotten his password), changeNewsletter, change Name and Surname,


}
