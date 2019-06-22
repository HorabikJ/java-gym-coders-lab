package pl.coderslab.javaGym.controller;

import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.entity.User;
import pl.coderslab.javaGym.service.UserService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @PatchMapping("/password/{id}")
    public String updateUserPassword(@PathVariable @Min(1) Long id,
            @Size(min = 5, message = "*Your password must have at least 5 characters.")
            @NotBlank(message = "*Please provide your password.")
            @RequestParam String password) {
//        userService.updatePassword(...);
            return password + id;
    }

}
