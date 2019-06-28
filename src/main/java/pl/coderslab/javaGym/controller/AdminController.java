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

    @GetMapping("/show-user/{id}")
    public User showUserDetails(@PathVariable
            @Min(value = 1, message = "*Please provide id grater than 0.") Long id) {
        return userService.findById(id);
    }

    @DeleteMapping("/delete-user/{id}")
    public Boolean deleteUser(@PathVariable
              @Min(value = 1, message = "*Please provide id grater than 0.") Long id) {
        return userService.deleteUserById(id);
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

    @GetMapping("/users-by-names")
    public List<User> searchForUsersByNames
            (@RequestParam @NotBlank(message = "*Please provide not blank input.") String firstName,
             @RequestParam @NotBlank(message = "*Please provide not blank input.") String lastName) {
        return userService.findAllUsersByNames(firstName, lastName);
    }

    @GetMapping("/admins-by-names")
    public List<User> searchForAdminsByNames
            (@RequestParam @NotBlank(message = "*Please provide not blank input.") String firstName,
             @RequestParam @NotBlank(message = "*Please provide not blank input.") String lastName) {
        return userService.findAllAdminsByNames(firstName, lastName);
    }

    @PatchMapping("/set-active/{id}")
    public User changeAccountActiveValue
            (@PathVariable @Min(value = 1,  message = "*Please provide user id grater than 0.") Long id,
             @RequestParam @NotNull(message = "*Active can not be null.") Boolean active) {
        return userService.changeUserActiveAccount(id, active);
    }

    @GetMapping("/send-activation-email/{id}")
    public Boolean sendActivationEmail
            (@PathVariable @Min( value = 1, message = "*Please provide user id grater than 0.")
             Long id) {
        return userService.sendActivationEmail(id);
    }

    @PostMapping("/send-email/{id}")
    public Boolean sendEmailToUser
            (@PathVariable @Min(value = 1, message = "*Please provide user id grater than 0.") Long id,
            @RequestBody @Valid Email email) {
        return userService.sendEmailToUser(id, email);
    }

    @PostMapping("/send-newsletter")
    public Boolean sendNewsletter(@RequestBody @Valid Email newsletter) {
        return userService.sendNewsletter(newsletter);
    }

}

// regular admin can do:
// - add new admin
// - show any user by id
// - delete any user
// - show all users
// - show all admins
// - search for user by email
// - search for admin by email
// - search for user by names
// - search for admin by names
// - set any user as active/inactive
// - send email any to user
// - send new activation email to any user
// - send newsletter email

