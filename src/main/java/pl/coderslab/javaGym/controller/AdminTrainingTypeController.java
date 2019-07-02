package pl.coderslab.javaGym.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.dataTransferObject.TrainingTypeDto;
import pl.coderslab.javaGym.entity.data.TrainingType;
import pl.coderslab.javaGym.service.dataService.TrainingTypeService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/admin/training-type/")
@Validated
public class AdminTrainingTypeController {

// Admin can do with training types:
// - add new training type,
// - editTrainingType existing training type,
// - delete training type,

    private TrainingTypeService trainingTypeService;
    private ModelMapper modelMapper;

    @Autowired
    public AdminTrainingTypeController(TrainingTypeService trainingTypeService,
                                       ModelMapper modelMapper) {
        this.trainingTypeService = trainingTypeService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add-new")
    @ResponseStatus(HttpStatus.CREATED)
    public TrainingTypeDto addTrainingType(@RequestBody @Valid TrainingTypeDto trainingTypeDto) {
        TrainingType trainingType = convertTrainingTypeToEntity(trainingTypeDto);
        return convertTrainingTypeToDto(trainingTypeService.saveTrainingType(trainingType));
    }

    @PutMapping("/edit/{id}")
    public TrainingTypeDto editTrainingType(@PathVariable @Min(1) Long id,
                                            @RequestBody @Valid TrainingTypeDto trainingTypeDto) {
        TrainingType trainingType = convertTrainingTypeToEntity(trainingTypeDto);
        return convertTrainingTypeToDto(trainingTypeService.editTrainingType(trainingType, id));
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteTrainingType(@PathVariable @Min(1) Long id) {
        return trainingTypeService.deleteTrainingTypeById(id);
    }

    private TrainingType convertTrainingTypeToEntity(TrainingTypeDto trainingTypeDto) {
        return modelMapper.map(trainingTypeDto, TrainingType.class);
    }

    private TrainingTypeDto convertTrainingTypeToDto(TrainingType trainingType) {
        return modelMapper.map(trainingType, TrainingTypeDto.class);
    }

}
