package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.dataTransferObject.ReservationDto;
import pl.coderslab.javaGym.dataTransferObject.UserDto;
import pl.coderslab.javaGym.entityDtoConverter.ReservationEntityDtoConverter;
import pl.coderslab.javaGym.entityDtoConverter.UserEntityDtoConverter;
import pl.coderslab.javaGym.service.userService.UserService;

import javax.validation.constraints.*;
import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

//    Regular user can do:
//    - show his details,
//    - change his password,
//    - change newsletter consent,
//    - change his names,
//    - change his email,
//    - reserve class,
//    - cancel future reserved classes,
//    - show future reservations,
//    - show past reservations,

    private UserService userService;
    private UserEntityDtoConverter userEntityDtoConverter;
    private ReservationEntityDtoConverter reservationEntityDtoConverter;

    @Autowired
    public UserController(UserService userService,
                          UserEntityDtoConverter userEntityDtoConverter,
                          ReservationEntityDtoConverter reservationEntityDtoConverter) {
        this.userService = userService;
        this.userEntityDtoConverter = userEntityDtoConverter;
        this.reservationEntityDtoConverter = reservationEntityDtoConverter;
    }

    @GetMapping("/show-details/{userId}")
    public UserDto getUser(@PathVariable @Min(1) Long userId) {
        return userEntityDtoConverter.convertUserToDto
                (userService.getAuthenticatedUserById(userId));
    }

    @PatchMapping("/change-password/{id}")
    public Boolean changeUserPassword(@PathVariable @Min(1) Long id,
         @NotBlank @RequestParam String oldPassword,
         @Size(min = 5) @NotBlank @RequestParam String newPassword) {
        return userService.changePassword(id, oldPassword, newPassword);
    }

    @PatchMapping("/newsletter/{id]")
    public UserDto changeNewsletterConsent(@PathVariable @Min(1) Long id,
                                        @RequestParam @NotNull Boolean newsletter) {
        return userEntityDtoConverter.convertUserToDto
                (userService.changeNewsletterConsent(id, newsletter));
    }

    @PatchMapping("/change-names/{id}")
    public UserDto changeUserFirstAndLastName(@PathVariable @Min(1) Long id,
                                              @NotBlank @RequestParam String firstName,
                                              @NotBlank @RequestParam String lastName) {
        return userEntityDtoConverter.convertUserToDto
                (userService.changeFirstAndLastName(id, firstName, lastName));
    }

    @PatchMapping("/change-email/{id}")
    public Boolean changeUserEmail(@PathVariable @Min(1) Long id,
                                   @Email @NotBlank @RequestParam String newEmail) {
        return userService.sendUserEmailChangeMessage(id, newEmail);
    }

    @PostMapping("/reserve-class/{classId}/{userId}")
    public ReservationDto reserveClassById(@PathVariable @Min(1) Long classId,
                                           @PathVariable @Min(1) Long userId) {
        return reservationEntityDtoConverter.convertReservationToDtoUserView
                (userService.reserveClassById(userId, classId));
    }

    @DeleteMapping("/cancel-class/{classId}/{userId}")
    public Boolean cancelClassById(@PathVariable @Min(1) Long classId,
                                   @PathVariable @Min(1) Long userId) {
        return userService.cancelClassById(classId, userId);
    }

    @GetMapping("/future-reservations/{userId}")
    public List<ReservationDto> showFutureReservationsByUserId(@PathVariable @Min(1) Long userId) {
        return reservationEntityDtoConverter.convertReservationsToDtoListUserView
                (userService.showFutureReservationsByUserId(userId));
    }

    @GetMapping("/past-reservations/{userId}")
    public List<ReservationDto> showPastReservationsByUserId(@PathVariable @Min(1) Long userId) {
        return reservationEntityDtoConverter.convertReservationsToDtoListUserView
                (userService.showPastReservationsByUserId(userId));
    }
}
