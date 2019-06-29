package pl.coderslab.javaGym.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.customValidator.Occurrence;
import pl.coderslab.javaGym.dataTransferObject.TrainingClassDto;
import pl.coderslab.javaGym.entity.data.TrainingClass;
import pl.coderslab.javaGym.service.dataService.TrainingClassService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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
             @RequestParam @Min(value = 1, message = "*Please provide number grater than 0.") Integer repeat) {
        return convertEntityToDtoList
                (trainingClassService.saveTrainingClass(trainingClassDto, occurrence, repeat));
    }

    private TrainingClass convertToEntity(TrainingClassDto trainingClassDto) {
        return modelMapper.map(trainingClassDto, TrainingClass.class);
    }

    private TrainingClassDto convertToDto(TrainingClass trainingClass) {
        TrainingClassDto trainingClassDto = modelMapper
                .map(trainingClass, TrainingClassDto.class);
        trainingClassDto.setReservedPlaces(trainingClass.getCustomers().size());
        return trainingClassDto;
    }

    private List<TrainingClassDto> convertEntityToDtoList(List<TrainingClass> trainingClasses) {
        return trainingClasses.stream()
                .map(trainingClass -> convertToDto(trainingClass))
                .collect(Collectors.toList());
    }
}
