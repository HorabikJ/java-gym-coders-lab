package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.entity.data.Instructor;
import pl.coderslab.javaGym.service.InstructorService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/instructor")
@Validated
public class AdminInstructorController {

    private InstructorService instructorService;

    @Autowired
    public AdminInstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

//    TODO think about the same solution as in save User
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Instructor saveInstructor(@RequestBody @Valid Instructor instructor) {
        return instructorService.save(instructor);
    }

}
