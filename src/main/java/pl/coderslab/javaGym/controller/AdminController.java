package pl.coderslab.javaGym.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.dataTransferObject.EmailDto;
import pl.coderslab.javaGym.dataTransferObject.UserDto;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.service.userService.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@Validated
public class AdminController {

// Regular admin can do:
// - add new admin,
// - show any user by id,
// - delete any user,
// - show all users,
// - show all admins,
// - search for user by email,
// - search for admin by email,
// - search for user by names,
// - search for admin by names,
// - set any user as active/inactive,
// - send email any to user,
// - send new activation email to any user,
// - send newsletter email,

    private UserService userService;
    private ModelMapper modelMapper;

    public AdminController(UserService userService,
                           ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto registerNewAdmin(@RequestBody @Valid UserDto userDto) {
        User user = convertToEntity(userDto);
        return convertToDto(userService.save(user, true));
    }

    @GetMapping("/show-user/{id}")
    public UserDto showUserDetails(@PathVariable @Min(1) Long id) {
        return convertToDto(userService.findUserById(id));
    }

    @DeleteMapping("/delete-user/{id}")
    public Boolean deleteUser(@PathVariable @Min(1) Long id) {
        return userService.deleteUserById(id);
    }

    @GetMapping("/show-all-users")
    public List<UserDto> showAllUsers() {
        return convertEntityToDtoList(userService.showAllUsersWithUserRoleOnly());
    }

    @GetMapping("/show-all-admins")
    public List<UserDto> showAllAdmins() {
        return convertEntityToDtoList(userService.showAllAdmins());
    }

    @GetMapping("/users-by-email")
    public List<UserDto> searchForUsersByEmail(@RequestParam @NotBlank String email) {
        return convertEntityToDtoList(userService.searchForUsersByEmail(email));
    }

    @GetMapping("/admins-by-email")
    public List<UserDto> searchForAdminsByEmail
            (@RequestParam @NotBlank String email) {
        return convertEntityToDtoList(userService.searchForAdminsByEmail(email));
    }

    @GetMapping("/users-by-names")
    public List<UserDto> searchForUsersByNames(@RequestParam @NotBlank String firstName,
                                               @RequestParam @NotBlank String lastName) {
        return convertEntityToDtoList(userService.findAllUsersByNames(firstName, lastName));
    }

    @GetMapping("/admins-by-names")
    public List<UserDto> searchForAdminsByNames(@RequestParam @NotBlank String firstName,
                                                @RequestParam @NotBlank String lastName) {
        return convertEntityToDtoList(userService.findAllAdminsByNames(firstName, lastName));
    }

    @PatchMapping("/set-active/{id}")
    public UserDto changeUserAccountActiveValue(@PathVariable @Min(1) Long id,
                                                @RequestParam @NotNull Boolean active) {
        return convertToDto(userService.changeUserActiveAccount(id, active));
    }

    @GetMapping("/send-activation-email/{id}")
    public Boolean sendActivationEmail(@PathVariable @Min(1) Long id) {
        return userService.sendActivationEmail(id);
    }

    @PostMapping("/send-email/{id}")
    public Boolean sendEmailToUser(@PathVariable @Min(1) Long id,
                                   @RequestBody @Valid EmailDto emailData) {
        return userService.sendEmailToUser(id, emailData);
    }

    @PostMapping("/send-newsletter")
    public Boolean sendNewsletter(@RequestBody @Valid EmailDto newsletter) {
        return userService.sendNewsletter(newsletter);
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        user.setPassword(null);
        return userDto;
    }

    private User convertToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    private List<UserDto> convertEntityToDtoList(List<User> users) {
        return users.stream()
                .map(user -> convertToDto(user))
                .collect(Collectors.toList());
    }

}

