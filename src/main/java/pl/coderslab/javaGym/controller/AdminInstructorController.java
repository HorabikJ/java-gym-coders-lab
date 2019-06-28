package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.entity.data.Instructor;
import pl.coderslab.javaGym.service.dataService.InstructorService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/admin/instructor")
@Validated
public class AdminInstructorController {

    private InstructorService instructorService;

    @Autowired
    public AdminInstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @PostMapping("/add-new")
    @ResponseStatus(HttpStatus.CREATED)
    public Instructor saveInstructor(@RequestBody @Valid Instructor instructor) {
        return instructorService.save(instructor);
    }

    @PutMapping("/edit/{id}")
    public Instructor updateInstructor
            (@RequestBody @Valid Instructor instructor,
             @PathVariable @Min(value = 1, message = "*Please provide id grater than 0.")
             Long id) {
        return instructorService.edit(instructor, id);
    }



}
