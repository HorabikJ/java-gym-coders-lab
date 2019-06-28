package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.entity.data.Instructor;
import pl.coderslab.javaGym.model.Email;
import pl.coderslab.javaGym.service.dataService.InstructorService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

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
            (@PathVariable @Min(value = 1, message = "*Please provide id grater than 0.") Long id,
             @RequestBody @Valid Instructor instructor) {
        return instructorService.edit(instructor, id);
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteInstructor
            (@PathVariable @Min(value = 1, message = "*Please provide id grater than 0.") Long id) {
        return instructorService.deleteById(id);
    }

    @GetMapping("/find-by-email")
    public List<Instructor> findInstructorByEmail
            (@RequestParam @NotBlank(message = "*Please provide not blank input.") String email) {
        return instructorService.findByEmail(email);
    }

    @PostMapping("/send-email/{id}")
    public Boolean sendEmailToInstructor
            (@PathVariable @Min(value = 1, message = "*Please provide id grater than 0.") Long id,
             @RequestBody @Valid Email email) {
        return instructorService.sendEmailToInstructor(email, id);
    }

// actions to do:
// - add new instructor,
// - edit existing instructor,
// - delete instructor,
// - send email to instructor,
// - find by email


}
