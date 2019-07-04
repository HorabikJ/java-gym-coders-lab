package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.dataTransferObject.UserDto;
import pl.coderslab.javaGym.entityDtoConverter.UserEntityDtoConverter;
import pl.coderslab.javaGym.service.userService.UserService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/super")
@Validated
public class SuperAdminController {

// Super admin can do:
// - delete any regular admin and any user,
// - set admin account as inactive/active,

    private UserService userService;
    private UserEntityDtoConverter userEntityDtoConverter;

    @Autowired
    public SuperAdminController(UserService userService,
                                UserEntityDtoConverter userEntityDtoConverter) {
        this.userService = userService;
        this.userEntityDtoConverter = userEntityDtoConverter;
    }

    @DeleteMapping("/delete-user/{id}")
    public Boolean deleteAnyUser(@PathVariable @Min(1) Long id) {
        return userService.deleteAnyUserById(id);
    }

    @PatchMapping("/set-active/{id}")
    public UserDto changeAccountActiveValue(@PathVariable @Min(1) Long id,
                                            @RequestParam @NotNull Boolean active) {
        return userEntityDtoConverter.convertUserToDto(userService.changeUserActiveAccountStatus(id, active));
    }

}
