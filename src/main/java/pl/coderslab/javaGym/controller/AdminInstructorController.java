package pl.coderslab.javaGym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.dataTransferObject.EmailDto;
import pl.coderslab.javaGym.dataTransferObject.InstructorDto;
import pl.coderslab.javaGym.entity.data.Instructor;
import pl.coderslab.javaGym.entityDtoConverter.InstructorEntityDtoConverter;
import pl.coderslab.javaGym.service.dataService.InstructorService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/admin/instructor")
@Validated
public class AdminInstructorController {

// Admin can do with instructors:
// - add new instructor,
// - editTrainingType existing instructor,
// - delete instructor,
// - send email to instructor,
// - find instructor by email

    private InstructorService instructorService;
    private InstructorEntityDtoConverter instructorEntityDtoConverter;

    @Autowired
    public AdminInstructorController(InstructorService instructorService,
                                     InstructorEntityDtoConverter instructorEntityDtoConverter) {
        this.instructorService = instructorService;
        this.instructorEntityDtoConverter = instructorEntityDtoConverter;
    }

    @PostMapping("/add-new")
    @ResponseStatus(HttpStatus.CREATED)
    public InstructorDto saveInstructor(@RequestBody @Valid InstructorDto instructorDto) {
        Instructor instructor = instructorEntityDtoConverter.convertInstructorToEntity(instructorDto);
        return instructorEntityDtoConverter.convertInstructorToDto(instructorService.save(instructor));
    }

    @PutMapping("/edit/{id}")
    public InstructorDto updateInstructor(@PathVariable @Min(1) Long id,
                                          @RequestBody @Valid InstructorDto instructorDto) {
        Instructor instructor = instructorEntityDtoConverter.convertInstructorToEntity(instructorDto);
        return instructorEntityDtoConverter.convertInstructorToDto(instructorService.edit(instructor, id));
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteInstructor(@PathVariable @Min(1) Long id) {
        return instructorService.deleteById(id);
    }

    @GetMapping("/find-by-email")
    public List<InstructorDto> findInstructorByEmail(@RequestParam @NotBlank String email) {
        return instructorEntityDtoConverter.convertInstructorEntityToDtoList(instructorService.findByEmail(email));
    }

    @PostMapping("/send-email/{id}")
    public Boolean sendEmailToInstructor(@PathVariable @Min(1) Long id,
                                         @RequestBody @Valid EmailDto emailDto) {
        return instructorService.sendEmailToInstructor(emailDto, id);
    }
}