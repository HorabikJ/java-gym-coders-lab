package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.customValidator.Occurrence;
import pl.coderslab.javaGym.dataTransferObject.EmailDto;
import pl.coderslab.javaGym.dataTransferObject.ReservationDto;
import pl.coderslab.javaGym.dataTransferObject.TrainingClassDto;
import pl.coderslab.javaGym.entityDtoConverter.ReservationEntityDtoConverter;
import pl.coderslab.javaGym.entityDtoConverter.TrainingClassEntityDtoConverter;
import pl.coderslab.javaGym.service.dataService.TrainingClassService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/admin/training-class/")
@Validated
public class AdminTrainingClassController {

// Admin can do with training classes:
//  - add training classes,
//  do by classId and classGroupId (only for future classes):
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
//  - show all TrainingClasses in future
//  - show all TrainingClasses in past
//  - send email to all participants for given classes
//  - show all future classes for given instructor
//  - show all future classes for given training type

    private TrainingClassService trainingClassService;
    private TrainingClassEntityDtoConverter trainingClassEntityDtoConverter;
    private ReservationEntityDtoConverter reservationEntityDtoConverter;

    @Autowired
    public AdminTrainingClassController(TrainingClassService trainingClassService,
                                        TrainingClassEntityDtoConverter trainingClassEntityDtoConverter,
                                        ReservationEntityDtoConverter reservationEntityDtoConverter) {
        this.trainingClassService = trainingClassService;
        this.trainingClassEntityDtoConverter = trainingClassEntityDtoConverter;
        this.reservationEntityDtoConverter = reservationEntityDtoConverter;
    }

    @PostMapping("/add-new")
    @ResponseStatus(HttpStatus.CREATED)
    public List<TrainingClassDto> addNewTrainingClass
            (@RequestBody @Valid TrainingClassDto trainingClassDto,
             @RequestParam @Occurrence Integer occurrence,
             @RequestParam @Min(1) Integer repeat) {
        return trainingClassEntityDtoConverter.convertTrainingClassToDtoList
                (trainingClassService.saveTrainingClass(trainingClassDto, occurrence, repeat));
    }

    @PatchMapping("/set-instructor/{classGroupId}/{instructorId}")
    public List<TrainingClassDto> setInstructorByClassGroupIdWhereClassesInFuture
            (@PathVariable @NotBlank String classGroupId,
             @PathVariable @Min(1) Long instructorId) {
        return trainingClassEntityDtoConverter.convertTrainingClassToDtoList
                (trainingClassService.setInstructorByClassGroupId(classGroupId, instructorId));
    }

    @PatchMapping("/set-training-type/{classGroupId}/{trainingTypeId}")
    public List<TrainingClassDto> setTrainingTypeByClassGroupIdWhereClassesInFuture
            (@PathVariable @NotBlank String classGroupId,
             @PathVariable @Min(1) Long trainingTypeId) {
        return trainingClassEntityDtoConverter.convertTrainingClassToDtoList
                (trainingClassService.setTrainingTypeByClassGroupId(classGroupId, trainingTypeId));
    }

    @PatchMapping("/change-max-capacity/{classGroupId}")
    public List<TrainingClassDto> changeMaxCapacityByClassGroupIdWhereClassesInFuture
            (@PathVariable @NotBlank String classGroupId,
             @RequestParam @Min(1) Integer maxCapacity) {
        return trainingClassEntityDtoConverter.convertTrainingClassToDtoList(trainingClassService
                .changeMaxCapacityByClassGroupId(classGroupId, maxCapacity));
    }

   @PatchMapping("/change-duration/{classGroupId}")
    public List<TrainingClassDto> changeDurationByClassGroupIdWhereClassesInFuture
            (@PathVariable @NotBlank String classGroupId,
             @RequestParam @Min(1) Integer duration) {
        return trainingClassEntityDtoConverter.convertTrainingClassToDtoList(trainingClassService
                .changeDurationByClassGroupId(classGroupId, duration));
    }

   @PatchMapping("/change-hour/{classGroupId}")
   public List<TrainingClassDto> changeClassStartHourByClassGroupIdWhereClassesInFuture
           (@PathVariable @NotBlank String classGroupId,
            @RequestParam @Min(0) @Max(23) Integer hour,
            @RequestParam @Min(0) @Max(59)  Integer minute) {
        return trainingClassEntityDtoConverter.convertTrainingClassToDtoList(trainingClassService
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
        return trainingClassEntityDtoConverter.convertTrainingClassToDto
                (trainingClassService.setInstructorByClassId(classId, instructorId));
    }

    @PatchMapping("/set-training-type-one/{classId}/{trainingTypeId}")
    public TrainingClassDto setTrainingTypeForClassIdWhereClassInFuture
                                    (@PathVariable @Min(1) Long classId,
                                    @PathVariable @Min(1) Long trainingTypeId) {
        return trainingClassEntityDtoConverter.convertTrainingClassToDto
                (trainingClassService.setTrainingTypeByClassId(classId, trainingTypeId));
    }

    @PatchMapping("/change-max-capacity-one/{classId}")
    public TrainingClassDto changeMaxCapacityForClassIdWhereClassInFuture
                                    (@PathVariable @Min(1) Long classId,
                                    @RequestParam @Min(1) Integer maxCapacity) {
        return trainingClassEntityDtoConverter.convertTrainingClassToDto
                (trainingClassService.changeMaxCapacityByClassId(classId, maxCapacity));
    }

    @PatchMapping("/change-duration-one/{classId}")
    public TrainingClassDto changeDurationForClassIdWhereClassInFuture
                                    (@PathVariable @Min(1) Long classId,
                                     @RequestParam @Min(1) Integer duration) {
        return trainingClassEntityDtoConverter.convertTrainingClassToDto
                (trainingClassService.changeDurationForClassId(classId, duration));
    }

    @PatchMapping("/change-hour-one/{classId}")
    public TrainingClassDto changeClassStartHourByClassIdWhereClassInFuture
            (@PathVariable @Min(1) Long classId,
             @RequestParam @Min(0) @Max(23) Integer hour,
             @RequestParam @Min(0) @Max(59)  Integer minute) {
        return trainingClassEntityDtoConverter.convertTrainingClassToDto
                (trainingClassService.changeClassStartHourByClassId(classId, hour, minute));
    }

    @DeleteMapping("/delete-one/{classId}")
    public Boolean deleteClassByClassIdWhereClassInFuture(@PathVariable @Min(1) Long classId) {
        return trainingClassService.deleteClassByClassId(classId);
    }

    @GetMapping("/null-relation")
    public List<TrainingClassDto> showAllTrainingClassInFutureWhereInstructorOrTrainingTypeIsNull() {
        return trainingClassEntityDtoConverter.convertTrainingClassToDtoList
                (trainingClassService.findAllInFutureWhereAnyRelationIsNull());
    }

    @GetMapping("/by-id/{classId}")
    public TrainingClassDto showAnyTrainingClass(@PathVariable @Min(1) Long classId) {
        return trainingClassEntityDtoConverter.convertTrainingClassToDto
                (trainingClassService.findTrainingClassById(classId));
    }

    @GetMapping("/by-class-group-id/{classGroupId}")
    public List<TrainingClassDto> showAllTrainingClassesByClassGroupId(@PathVariable @NotBlank String classGroupId) {
        return trainingClassEntityDtoConverter.convertTrainingClassToDtoList
                (trainingClassService.findAllByClassGroupId(classGroupId));
    }

    @GetMapping("/reservations/{classId}")
    public List<ReservationDto> showAllReservationsByClassId(@PathVariable @Min(1) Long classId) {
        return reservationEntityDtoConverter.convertReservationsToDtoListAdminView
                (trainingClassService.findAllReservationsByClassId(classId));
    }

    @GetMapping("/all-future")
    public List<TrainingClassDto> showAllTrainingClassesInFuture() {
        return trainingClassEntityDtoConverter.convertTrainingClassToDtoList
                (trainingClassService.findAllByStartDateIsInFuture());
    }

    @GetMapping("/all-past")
    public List<TrainingClassDto> showAllTrainingClassesInPast() {
        return trainingClassEntityDtoConverter.convertTrainingClassToDtoList
                (trainingClassService.findAllByStartDateIsInPast());
    }

    @GetMapping("/all-future-instructor/{instructorId}")
    public List<TrainingClassDto> showAllFutureClassesByInstructorId(@PathVariable @Min(1) Long instructorId) {
        return trainingClassEntityDtoConverter.convertTrainingClassToDtoList
                (trainingClassService.findAllFutureClassesByInstructor(instructorId));
    }

    @GetMapping("/all-future-training-type/{trainingTypeId}")
    public List<TrainingClassDto> showAllFutureClassesByTrainingTypeId(@PathVariable @Min(1) Long trainingTypeId) {
        return trainingClassEntityDtoConverter.convertTrainingClassToDtoList
                (trainingClassService.findAllFutureClassesByTrainingType(trainingTypeId));
    }

    @PostMapping("/send-email/{classId}")
    public Boolean sendEmailToAllClassCustomers(@PathVariable @Min(1) Long classId,
                                                @RequestBody @Valid EmailDto email) {
        return trainingClassService.sendEmailToAllCustomersByTrainingClass(classId, email);
    }
}
