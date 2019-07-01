package pl.coderslab.javaGym.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.dataTransferObject.TrainingClassDto;
import pl.coderslab.javaGym.dataTransferObject.UserDto;
import pl.coderslab.javaGym.entity.data.TrainingClass;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.service.userService.UserService;

import javax.validation.constraints.*;
import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

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

    @GetMapping("/reserve-classes/{userId}/{classId}")
    public TrainingClassDto reserveClassesById(@PathVariable @Min(1) Long userId,
                                                  @PathVariable @Min(1) Long classId) {
        return convertTrainingClassToDto(userService.reserveClassById(userId, classId));
    }

    private UserDto convertUserToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        user.setPassword(null);
        return userDto;
    }

    private User convertUserToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    private TrainingClass convertTrainingClassToEntity(TrainingClassDto trainingClassDto) {
        return modelMapper.map(trainingClassDto, TrainingClass.class);
    }

    private TrainingClassDto convertTrainingClassToDto(TrainingClass trainingClass) {
        TrainingClassDto trainingClassDto = modelMapper
                .map(trainingClass, TrainingClassDto.class);
        trainingClassDto.setReservedPlaces(trainingClass.getCustomers().size());
        return trainingClassDto;
    }

}

//    User can do:
//    - show his details
//    - change his password
//    - change newsletter consent
//    - change his names
//    - change his email
//    TODO
//     - reserve classes,
//     - cancel reserved classes if future,
//     - show reserved classes,
//     - show reserved classes in past,
