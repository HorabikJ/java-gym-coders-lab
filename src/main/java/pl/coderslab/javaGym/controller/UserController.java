package pl.coderslab.javaGym.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.dataTransferObject.ReservationDto;
import pl.coderslab.javaGym.dataTransferObject.TrainingClassDto;
import pl.coderslab.javaGym.dataTransferObject.UserDto;
import pl.coderslab.javaGym.entity.data.Reservation;
import pl.coderslab.javaGym.entity.data.TrainingClass;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.service.userService.UserService;

import javax.validation.constraints.*;
import java.util.List;
import java.util.stream.Collectors;

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
    private ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService,
                          ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/show-details")
    public UserDto getUser(@RequestParam @Min(1) Long userId) {
        return convertUserToDto(userService.getAuthenticatedUserById(userId));
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
        return convertUserToDto(userService.changeNewsletterConsent(id, newsletter));
    }

    @PatchMapping("/change-names/{id}")
    public UserDto changeUserFirstAndLastName(@PathVariable @Min(1) Long id,
                                              @NotBlank @RequestParam String firstName,
                                              @NotBlank @RequestParam String lastName) {
        return convertUserToDto(userService.changeFirstAndLastName(id, firstName, lastName));
    }

    @PatchMapping("/change-email/{id}")
    public Boolean changeUserEmail(@PathVariable @Min(1) Long id,
                                   @Email @NotBlank @RequestParam String newEmail) {
        return userService.sendUserEmailChangeMessage(id, newEmail);
    }

    @PostMapping("/reserve-class/{classId}/{userId}")
    public ReservationDto reserveClassById(@PathVariable @Min(1) Long classId,
                                           @PathVariable @Min(1) Long userId) {
        return convertReservationToDto(userService.reserveClassById(userId, classId));
    }

    @DeleteMapping("/cancel-class/{classId}/{userId}")
    public Boolean cancelClassById(@PathVariable @Min(1) Long classId,
                                   @PathVariable @Min(1) Long userId) {
        return userService.cancelClassById(classId, userId);
    }

    @GetMapping("/future-reservations/{userId}")
    public List<ReservationDto> showFutureReservationsByUserId(@PathVariable @Min(1) Long userId) {
        return convertReservationToDtoList(userService.showFutureReservationsByUserId(userId));
    }

    @GetMapping("/past-reservations/{userId}")
    public List<ReservationDto> showPastReservationsByUserId(@PathVariable @Min(1) Long userId) {
        return convertReservationToDtoList(userService.showPastReservationsByUserId(userId));
    }

    private UserDto convertUserToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setPassword(null);
        return userDto;
    }

    private ReservationDto convertReservationToDto(Reservation reservation) {
        ReservationDto reservationDto = modelMapper.map(reservation, ReservationDto.class);
        reservationDto.setUserDto(null);
        reservationDto.setTrainingClassDto(convertTrainingClassToDto(reservation.getTrainingClass()));
        return reservationDto;
    }

    private TrainingClassDto convertTrainingClassToDto(TrainingClass trainingClass) {
        TrainingClassDto trainingClassDto = modelMapper
                .map(trainingClass, TrainingClassDto.class);
        trainingClassDto.setReservedPlaces(trainingClass.getReservations().size());
        return trainingClassDto;
    }

    private List<ReservationDto> convertReservationToDtoList(List<Reservation> reservations) {
        return reservations.stream()
                .map(reservation -> convertReservationToDto(reservation))
                .collect(Collectors.toList());
    }
}
