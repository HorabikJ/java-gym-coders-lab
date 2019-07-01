package pl.coderslab.javaGym.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.customValidator.Occurrence;
import pl.coderslab.javaGym.dataTransferObject.*;
import pl.coderslab.javaGym.entity.data.Reservation;
import pl.coderslab.javaGym.entity.data.TrainingClass;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.service.dataService.TrainingClassService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/training-class/")
@Validated
public class AdminTrainingClassController {

    private TrainingClassService trainingClassService;
    private ModelMapper modelMapper;

    @Autowired
    public AdminTrainingClassController(TrainingClassService trainingClassService,
                                        ModelMapper modelMapper) {
        this.trainingClassService = trainingClassService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add-new")
    @ResponseStatus(HttpStatus.CREATED)
    public List<TrainingClassDto> addNewTrainingClass
            (@RequestBody @Valid TrainingClassDto trainingClassDto,
             @RequestParam @Occurrence Integer occurrence,
             @RequestParam @Min(1) Integer repeat) {
        return convertTrainingClassEntityToDtoList
                (trainingClassService.saveTrainingClass(trainingClassDto, occurrence, repeat));
    }

    @PatchMapping("/set-instructor/{classGroupId}/{instructorId}")
    public List<TrainingClassDto> setInstructorByClassGroupIdWhereClassesInFuture
            (@PathVariable @NotBlank String classGroupId,
             @PathVariable @Min(1) Long instructorId) {
        return convertTrainingClassEntityToDtoList
                (trainingClassService.setInstructorByClassGroupId(classGroupId, instructorId));
    }

    @PatchMapping("/set-training-type/{classGroupId}/{trainingTypeId}")
    public List<TrainingClassDto> setTrainingTypeByClassGroupIdWhereClassesInFuture
            (@PathVariable @NotBlank String classGroupId,
             @PathVariable @Min(1) Long trainingTypeId) {
        return convertTrainingClassEntityToDtoList
                (trainingClassService.setTrainingTypeByClassGroupId(classGroupId, trainingTypeId));
    }

    @PatchMapping("/change-max-capacity/{classGroupId}")
    public List<TrainingClassDto> changeMaxCapacityByClassGroupIdWhereClassesInFuture
            (@PathVariable @NotBlank String classGroupId,
             @RequestParam @Min(1) Integer maxCapacity) {
        return convertTrainingClassEntityToDtoList(trainingClassService
                .changeMaxCapacityByClassGroupId(classGroupId, maxCapacity));
    }

   @PatchMapping("/change-duration/{classGroupId}")
    public List<TrainingClassDto> changeDurationByClassGroupIdWhereClassesInFuture
            (@PathVariable @NotBlank String classGroupId,
             @RequestParam @Min(1) Integer duration) {
        return convertTrainingClassEntityToDtoList(trainingClassService
                .changeDurationByClassGroupId(classGroupId, duration));
    }

   @PatchMapping("/change-hour/{classGroupId}")
   public List<TrainingClassDto> changeClassStartHourByClassGroupIdWhereClassesInFuture
           (@PathVariable @NotBlank String classGroupId,
            @RequestParam @Min(0) @Max(23) Integer hour,
            @RequestParam @Min(0) @Max(59)  Integer minute) {
        return convertTrainingClassEntityToDtoList(trainingClassService
                .changeClassStartHourByClassGroupId(classGroupId, hour, minute));
   }

   @DeleteMapping("/delete/{classGroupId}")
   public Boolean deleteClassByClassGroupIdWhereClassesInFuture
           (@PathVariable @NotBlank String classGroupId) {
        return trainingClassService.deleteClassByClassGroupId(classGroupId);
   }

    @PatchMapping("/set-instructor-one/{classId}/{instructorId}")
    public TrainingClassDto setInstructorForClassIdWhereClassInFuture
                                    (@PathVariable @Min(1) Long classId,
                                    @PathVariable @Min(1) Long instructorId) {
        return convertTrainingClassToDto
                (trainingClassService.setInstructorByClassId(classId, instructorId));
    }

    @PatchMapping("/set-training-type-one/{classId}/{trainingTypeId}")
    public TrainingClassDto setTrainingTypeForClassIdWhereClassInFuture
                                    (@PathVariable @Min(1) Long classId,
                                    @PathVariable @Min(1) Long trainingTypeId) {
        return convertTrainingClassToDto
                (trainingClassService.setTrainingTypeByClassId(classId, trainingTypeId));
    }

    @PatchMapping("/change-max-capacity-one/{classId}")
    public TrainingClassDto changeMaxCapacityForClassIdWhereClassInFuture
                                    (@PathVariable @Min(1) Long classId,
                                    @RequestParam @Min(1) Integer maxCapacity) {
        return convertTrainingClassToDto(trainingClassService.changeMaxCapacityByClassId(classId, maxCapacity));
    }

    @PatchMapping("/change-duration-one/{classId}")
    public TrainingClassDto changeDurationForClassIdWhereClassInFuture
                                    (@PathVariable @Min(1) Long classId,
                                     @RequestParam @Min(1) Integer duration) {
        return convertTrainingClassToDto(trainingClassService.changeDurationForClassId(classId, duration));
    }

    @PatchMapping("/change-hour-one/{classId}")
    public TrainingClassDto changeClassStartHourByClassIdWhereClassInFuture
            (@PathVariable @Min(1) Long classId,
             @RequestParam @Min(0) @Max(23) Integer hour,
             @RequestParam @Min(0) @Max(59)  Integer minute) {
        return convertTrainingClassToDto(trainingClassService.changeClassStartHourByClassId(classId, hour, minute));
    }

    @DeleteMapping("/delete-one/{classId}")
    public Boolean deleteClassByClassIdWhereClassInFuture(@PathVariable @Min(1) Long classId) {
        return trainingClassService.deleteClassByClassId(classId);
    }

    @GetMapping("/null-relation")
    public List<TrainingClassDto> showAllTrainingClassInFutureWhereInstructorOrTrainingTypeIsNull() {
        return convertTrainingClassEntityToDtoList(trainingClassService.findAllInFutureWhereAnyRelationIsNull());
    }

    @GetMapping("/by-id/{classId}")
    public TrainingClassDto showAnyTrainingClass(@PathVariable @Min(1) Long classId) {
        return convertTrainingClassToDto(trainingClassService.findById(classId));
    }

    @GetMapping("/by-class-group-id/{classGroupId}")
    public List<TrainingClassDto> showAllTrainingClassesByClassGroupId(@PathVariable @NotBlank String classGroupId) {
        return convertTrainingClassEntityToDtoList(trainingClassService.findAllByClassGroupId(classGroupId));
    }

    @GetMapping("/reservations/{classId}")
    public List<ReservationDto> showAllUsersOnClassReservationList(@PathVariable @Min(1) Long classId) {
        return convertReservationEntityToDtoList
                (trainingClassService.findAllReservationsByClassId(classId));
    }

    @GetMapping("/all-future")
    public List<TrainingClassDto> showAllTrainingClassesInFuture() {
        return convertTrainingClassEntityToDtoList(trainingClassService.findAllByStartDateIsInFuture());
    }

    @GetMapping("/all-past")
    public List<TrainingClassDto> showAllTrainingClassesInPast() {
        return convertTrainingClassEntityToDtoList(trainingClassService.findAllByStartDateIsInPast());
    }

    @GetMapping("/all-future-instructor/{instructorId}")
    public List<TrainingClassDto> showAllFutureClassesForGivenInstructor(@PathVariable @Min(1) Long instructorId) {
        return convertTrainingClassEntityToDtoList
                (trainingClassService.findAllFutureClassesByInstructor(instructorId));
    }

    @GetMapping("/all-future-training-type/{trainingTypeId}")
    public List<TrainingClassDto> showAllFutureClassesForGivenTrainingType(@PathVariable @Min(1) Long trainingTypeId) {
        return convertTrainingClassEntityToDtoList
                (trainingClassService.findAllFutureClassesByTrainingType(trainingTypeId));
    }

    @PostMapping("/send-email/{classId}")
    public Boolean sendEmailToAllClassCustomers(@PathVariable @Min(1) Long classId,
                                                @RequestBody @Valid EmailDto email) {
        return trainingClassService.sendEmailToAllCustomersByTrainingClass(classId, email);
    }

    private TrainingClass convertTrainingClassToEntity(TrainingClassDto trainingClassDto) {
        return modelMapper.map(trainingClassDto, TrainingClass.class);
    }

    private TrainingClassDto convertTrainingClassToDto(TrainingClass trainingClass) {
        TrainingClassDto trainingClassDto = modelMapper
                .map(trainingClass, TrainingClassDto.class);
        trainingClassDto.setReservedPlaces(trainingClass.getReservations().size());
//        TODO
        return trainingClassDto;
    }

    private List<TrainingClassDto> convertTrainingClassEntityToDtoList(List<TrainingClass> trainingClasses) {
        return trainingClasses.stream()
                .map(trainingClass -> convertTrainingClassToDto(trainingClass))
                .collect(Collectors.toList());
    }

    private ReservationDto convertReservationToDto(Reservation reservation) {
        ReservationDto reservationDto  = modelMapper.map(reservation, ReservationDto.class);
        reservationDto.setUserDto(convertUserToDto(reservation.getUser()));
        reservationDto.setTrainingClassDto(convertTrainingClassToDto(reservation.getTrainingClass()));
        return reservationDto;
    }

//    TODO wywal nie uzywane metody
    private Reservation convertReservationToEntity(ReservationDto reservationDto) {
        return modelMapper.map(reservationDto, Reservation.class);
    }

    private List<ReservationDto> convertReservationEntityToDtoList(List<Reservation> reservations) {
        return reservations.stream()
                .map(reservation -> convertReservationToDto(reservation))
                .collect(Collectors.toList());
    }

    private UserDto convertUserToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setPassword(null);
        return userDto;
    }

    private User convertUserToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

}

// admin can do with training classes:
//  - add training classes
//
//  do by id and classGroupId (only for future classes):
//      - delete
//      - set instructor
//      - set training type
//      - change max capacity
//      - change duration
//      - change start hour
//
//  - show all future classes where trainingType or instructor is null
//  - show any training class by id
//  - show all by classGroupId
//  - show all reservations for any TrainingClass by classId
//  - show all in future
//  - show all in past
//  - send email to all participants for given classes
//  - show all future classes for given instructor
//  - show all future classes for given training type

