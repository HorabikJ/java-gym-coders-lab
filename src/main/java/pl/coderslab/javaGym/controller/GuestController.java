package pl.coderslab.javaGym.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.dataTransferObject.InstructorDto;
import pl.coderslab.javaGym.dataTransferObject.TrainingClassDto;
import pl.coderslab.javaGym.dataTransferObject.TrainingTypeDto;
import pl.coderslab.javaGym.entity.data.Instructor;
import pl.coderslab.javaGym.entity.data.TrainingClass;
import pl.coderslab.javaGym.entity.data.TrainingType;
import pl.coderslab.javaGym.service.dataService.InstructorService;
import pl.coderslab.javaGym.service.dataService.TrainingClassService;
import pl.coderslab.javaGym.service.dataService.TrainingTypeService;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

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
    private ModelMapper modelMapper;

    @Autowired
    public GuestController(TrainingClassService trainingClassService,
                           InstructorService instructorService,
                           TrainingTypeService trainingTypeService,
                           ModelMapper modelMapper) {
        this.trainingClassService = trainingClassService;
        this.instructorService = instructorService;
        this.trainingTypeService = trainingTypeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/classes")
    public List<TrainingClassDto> showAllClassesAvailableForUsers() {
        return convertTrainingClassEntityToDtoList(trainingClassService
                .findAllClassesAvailableForUsers());
    }

    @GetMapping("/instructors")
    public List<InstructorDto> showAllInstructors() {
        return convertInstructorEntityToDtoList(instructorService.findAll());
    }

    @GetMapping("/instructors-names")
    public List<InstructorDto> showInstructorByNames(@RequestParam @Size(min = 1) String firstName,
                                                     @RequestParam @Size(min = 1) String lastName) {
        return convertInstructorEntityToDtoList(instructorService.findByNames(firstName, lastName));
    }

    @GetMapping("/instructor/{instructorId}")
    public InstructorDto showInstructorById(@PathVariable @Min(1) Long instructorId) {
        return convertInstructorToDto(instructorService.findInstructorById(instructorId));
    }

    @GetMapping("/training-types")
    public List<TrainingTypeDto> showAllTrainingTypes() {
        return convertTrainingTypeEntityToDtoList(trainingTypeService.findAllTrainingTypes());
    }

    @GetMapping("/training-type/{trainingTypeId}")
    public TrainingTypeDto showTrainingTypeById(@PathVariable @Min(1) Long trainingTypeId) {
        return convertTrainingTypeToDto(trainingTypeService.findTrainingTypeBy(trainingTypeId));
    }

    @GetMapping("/class-instructor/{instructorId}")
    public List<TrainingClassDto> showTrainingClassesByInstructorId
            (@PathVariable @Min(1) Long instructorId) {
        return convertTrainingClassEntityToDtoList(trainingClassService
                .findAllTrainingClassesForUsersByInstructorId(instructorId));
    }

    @GetMapping("/class-training-type/{trainingTypeId}")
    public List<TrainingClassDto> showTrainingClassesByTrainingTypeId
            (@PathVariable @Min(1) Long trainingTypeId) {
        return convertTrainingClassEntityToDtoList(trainingClassService
                .findAllTrainingClassesForUsersByTrainingTypeId(trainingTypeId));
    }

    private InstructorDto convertInstructorToDto(Instructor instructor) {
        return modelMapper.map(instructor, InstructorDto.class);
    }

    private List<InstructorDto> convertInstructorEntityToDtoList(List<Instructor> instructors) {
        return instructors.stream()
                .map(instructor -> convertInstructorToDto(instructor))
                .collect(Collectors.toList());
    }

    private TrainingClassDto convertTrainingClassToDto(TrainingClass trainingClass) {
        TrainingClassDto trainingClassDto = modelMapper
                .map(trainingClass, TrainingClassDto.class);
        trainingClassDto.setReservedPlaces(trainingClass.getReservations().size());
        return trainingClassDto;
    }

    private List<TrainingClassDto> convertTrainingClassEntityToDtoList(List<TrainingClass> trainingClasses) {
        return trainingClasses.stream()
                .map(trainingClass -> convertTrainingClassToDto(trainingClass))
                .collect(Collectors.toList());
    }

    private TrainingTypeDto convertTrainingTypeToDto(TrainingType trainingType) {
        return modelMapper.map(trainingType, TrainingTypeDto.class);
    }

    private List<TrainingTypeDto> convertTrainingTypeEntityToDtoList(List<TrainingType> trainingTypes) {
         return trainingTypes.stream()
                .map(trainingType -> convertTrainingTypeToDto(trainingType))
                .collect(Collectors.toList());
    }


}
