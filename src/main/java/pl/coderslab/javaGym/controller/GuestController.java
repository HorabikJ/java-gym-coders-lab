package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.dataTransferObject.InstructorDto;
import pl.coderslab.javaGym.dataTransferObject.TrainingClassDto;
import pl.coderslab.javaGym.dataTransferObject.TrainingTypeDto;
import pl.coderslab.javaGym.entityDtoConverter.InstructorEntityDtoConverter;
import pl.coderslab.javaGym.entityDtoConverter.TrainingClassEntityDtoConverter;
import pl.coderslab.javaGym.entityDtoConverter.TrainingTypeEntityDtoConverter;
import pl.coderslab.javaGym.service.dataService.InstructorService;
import pl.coderslab.javaGym.service.dataService.TrainingClassService;
import pl.coderslab.javaGym.service.dataService.TrainingTypeService;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/guest")
public class GuestController {

//    Guest can do:
//    - show all classes in future for 2 weeks
//    - show all instructors
//    - show instructors by names
//    - show instructor by id
//    - show all training types
//    - show training type by id
//    - show classes for given instructor
//    - show classes for given training type

    private TrainingClassService trainingClassService;
    private InstructorService instructorService;
    private TrainingTypeService trainingTypeService;
    private TrainingTypeEntityDtoConverter trainingTypeEntityDtoConverter;
    private TrainingClassEntityDtoConverter trainingClassEntityDtoConverter;
    private InstructorEntityDtoConverter instructorEntityDtoConverter;

    @Autowired
    public GuestController(TrainingClassService trainingClassService,
                           InstructorService instructorService,
                           TrainingTypeService trainingTypeService,
                           TrainingTypeEntityDtoConverter trainingTypeEntityDtoConverter,
                           TrainingClassEntityDtoConverter trainingClassEntityDtoConverter,
                           InstructorEntityDtoConverter instructorEntityDtoConverter) {
        this.trainingClassService = trainingClassService;
        this.instructorService = instructorService;
        this.trainingTypeService = trainingTypeService;
        this.trainingTypeEntityDtoConverter = trainingTypeEntityDtoConverter;
        this.trainingClassEntityDtoConverter = trainingClassEntityDtoConverter;
        this.instructorEntityDtoConverter = instructorEntityDtoConverter;
    }

    @GetMapping("/classes")
    public List<TrainingClassDto> showAllClassesAvailableForUsers() {
        return trainingClassEntityDtoConverter.convertTrainingClassToDtoList(trainingClassService
                .findAllClassesAvailableForUsers());
    }

    @GetMapping("/instructors")
    public List<InstructorDto> showAllInstructors() {
        return instructorEntityDtoConverter.convertInstructorEntityToDtoList(instructorService.findAll());
    }

    @GetMapping("/instructors-names")
    public List<InstructorDto> showInstructorByNames(@RequestParam @Size(min = 1) String firstName,
                                                     @RequestParam @Size(min = 1) String lastName) {
        return instructorEntityDtoConverter.convertInstructorEntityToDtoList
                (instructorService.findByNames(firstName, lastName));
    }

    @GetMapping("/instructor/{instructorId}")
    public InstructorDto showInstructorById(@PathVariable @Min(1) Long instructorId) {
        return instructorEntityDtoConverter.convertInstructorToDto
                (instructorService.findInstructorById(instructorId));
    }

    @GetMapping("/training-types")
    public List<TrainingTypeDto> showAllTrainingTypes() {
        return trainingTypeEntityDtoConverter.convertTrainingTypesToDtoList
                (trainingTypeService.findAllTrainingTypes());
    }

    @GetMapping("/training-type/{trainingTypeId}")
    public TrainingTypeDto showTrainingTypeById(@PathVariable @Min(1) Long trainingTypeId) {
        return trainingTypeEntityDtoConverter.convertTrainingTypeToDto
                (trainingTypeService.findTrainingTypeBy(trainingTypeId));
    }

    @GetMapping("/class-instructor/{instructorId}")
    public List<TrainingClassDto> showTrainingClassesByInstructorId
            (@PathVariable @Min(1) Long instructorId) {
        return trainingClassEntityDtoConverter.convertTrainingClassToDtoList(trainingClassService
                .findAllTrainingClassesForUsersByInstructorId(instructorId));
    }

    @GetMapping("/class-training-type/{trainingTypeId}")
    public List<TrainingClassDto> showTrainingClassesByTrainingTypeId
            (@PathVariable @Min(1) Long trainingTypeId) {
        return trainingClassEntityDtoConverter.convertTrainingClassToDtoList(trainingClassService
                .findAllTrainingClassesForUsersByTrainingTypeId(trainingTypeId));
    }
}
