package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.entity.data.TrainingClass;
import pl.coderslab.javaGym.dataTransferObject.TrainingClassDto;
import pl.coderslab.javaGym.service.dataService.TrainingClassService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/admin/training-class/")
@Validated
public class AdminTrainingClassController {

    private TrainingClassService trainingClassService;

    @Autowired
    public AdminTrainingClassController(TrainingClassService trainingClassService) {
        this.trainingClassService = trainingClassService;
    }

//    @PostMapping("/add-new")
//    @ResponseStatus(HttpStatus.CREATED)
//    public List<TrainingClass> addNewTrainingClass(@RequestBody @Valid TrainingClassDto trainingClass,
//                                                   @RequestParam @Min(1) Integer frequency,
//                                                   @RequestParam @Min(1) Integer occurrence) {
//        return trainingClassService.saveTrainingClass(trainingClass, frequency, occurrence);
//    }



}
