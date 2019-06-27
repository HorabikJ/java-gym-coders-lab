package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.service.userService.UserService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/super")
@Validated
public class SuperAdminController {

    private UserService userService;

    @Autowired
    public SuperAdminController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/delete-user")
    public Boolean deleteAnyUser
            (@RequestParam @Min(value = 1, message = "*Please provide user id grater than 0.")
            Long userId) {
        return userService.deleteAnyUserById(userId);
    }

    @PatchMapping("/set-active")
    public User changeAccountActiveValue
            (@RequestParam @Min(value = 1,  message = "*Please provide user id grater than 0.") Long userId,
             @RequestParam @NotNull(message = "*Active can not be null.") Boolean active) {
        return userService.changeAnyUserActiveAccount(userId, active);
    }

}

// super admin can do:
// - delete any regular admin and user
// - set admin account as inactive/active
