package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderslab.javaGym.service.userService.UserService;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/super")
@Validated
public class SuperAdminController {

    private UserService userService;

    @Autowired
    public SuperAdminController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/delete-any-user")
    public Boolean deleteAnyUser
            (@RequestParam @Min(value = 1, message = "*Please provide user id grater than 0.")
            Long userId) {
        return userService.deleteAnyUserById(userId);
    }

}


// super admin can do:
// delete any regular admin and user
// set admin account as inactive/active
