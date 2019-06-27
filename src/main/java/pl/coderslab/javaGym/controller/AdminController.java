package pl.coderslab.javaGym.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.model.Email;
import pl.coderslab.javaGym.service.userService.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Validated
public class AdminController {

    private UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerNewAdmin(@RequestBody @Valid User user) {
        return userService.save(user, true);
    }

    @GetMapping("/show-user")
    public User showUserDetails(@RequestParam
            @Min(value = 1, message = "*Please provide id grater than 0.") Long userId) {
        return userService.findById(userId);
    }

    @DeleteMapping("/delete-user")
    public Boolean deleteUser(@RequestParam
              @Min(value = 1, message = "*Please provide id grater than 0.") Long userId) {
        return userService.deleteUserById(userId);
    }

    @GetMapping("/show-all-users")
    public List<User> showAllUsers() {
        return userService.showAllUsersWithUserRoleOnly();
    }

    @GetMapping("/show-all-admins")
    public List<User> showAllAdmins() {
        return userService.showAllAdmins();
    }

    @GetMapping("/users-by-email")
    public List<User> searchForUsersByEmail
            (@RequestParam @NotBlank(message = "*Please provide not blank input.")
            String email) {
        return userService.searchForUsersByEmail(email);
    }

    @GetMapping("/admins-by-email")
    public List<User> searchForAdminsByEmail
            (@RequestParam @NotBlank(message = "*Please provide not blank input.")
            String email) {
        return userService.searchForAdminsByEmail(email);
    }

    @PatchMapping("/set-active")
    public User changeAccountActiveValue
            (@RequestParam @Min(value = 1,  message = "*Please provide user id grater than 0.") Long userId,
             @RequestParam @NotNull(message = "*Active can not be null.") Boolean active) {
        return userService.changeUserActiveAccount(userId, active);
    }

    @PostMapping("/send-activation-email")
    public Boolean sendActivationEmail
            (@RequestParam @Min( value = 1, message = "*Please provide user id grater than 0.")
             Long userId) {
        return userService.sendActivationEmail(userId);
    }

    @PostMapping("/send-email")
    public Boolean sendEmailToUser(@RequestBody @Valid Email email,
           @RequestParam @Min(value = 1, message = "*Please provide user id grater than 0.") Long userId) {
        return userService.sendEmailToUser(userId, email);
    }

    @PostMapping("/send-newsletter")
    public Boolean sendNewsletter(@RequestBody @Valid Email newsletter) {
        return userService.sendNewsletter(newsletter);
    }

}

//TODO
// add new admin                    - done
// show any user by id              - done
// delete any user                  - done
// show all users                   - done
// show all admins                  - done
// search for user by email         - done
// search for admin by email        - done
// set any user as active/inactive  - done
// send email any to user           - done
// send new activation email to any user -done
// send newsletter email            - done

