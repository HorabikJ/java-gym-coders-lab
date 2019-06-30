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

    @DeleteMapping("/delete-user/{id}")
    public Boolean deleteAnyUser(@PathVariable @Min(1) Long id) {
        return userService.deleteAnyUserById(id);
    }

    @PatchMapping("/set-active/{id}")
    public User changeAccountActiveValue(@PathVariable @Min(1) Long id,
                                         @RequestParam @NotNull Boolean active) {
        return userService.changeAnyUserActiveAccount(id, active);
    }

}

// super admin can do:
// - delete any regular admin and user
// - set admin account as inactive/active
