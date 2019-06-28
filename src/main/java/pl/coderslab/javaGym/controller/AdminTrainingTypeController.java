package pl.coderslab.javaGym.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.entity.data.TrainingType;
import pl.coderslab.javaGym.service.dataService.TrainingTypeService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/admin/training-type/")
@Validated
public class AdminTrainingTypeController {

    private TrainingTypeService trainingTypeService;

    public AdminTrainingTypeController(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    @PostMapping("/add-new")
    public TrainingType addTrainingType(@RequestBody @Valid TrainingType trainingType) {
        return trainingTypeService.save(trainingType);
    }

    @PutMapping("/edit/{id}")
    public TrainingType editTrainingType
            (@PathVariable @Min(value = 1, message = "*Please provide id grater than 0") Long id,
             @RequestBody @Valid TrainingType trainingType) {
        return trainingTypeService.edit(trainingType, id);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteTrainingType
            (@PathVariable @Min(value = 1, message = "*Please provide id grater than 0") Long id) {
        return trainingTypeService.deleteById(id);
    }

// admin can do with training types:
// - add new training type,
// - edit existing training type,
// - delete training type,


}
