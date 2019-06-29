package pl.coderslab.javaGym.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.dataTransferObject.EmailDto;
import pl.coderslab.javaGym.dataTransferObject.InstructorDto;
import pl.coderslab.javaGym.entity.data.Instructor;
import pl.coderslab.javaGym.service.dataService.InstructorService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/instructor")
@Validated
public class AdminInstructorController {

    private InstructorService instructorService;
    private ModelMapper modelMapper;

    @Autowired
    public AdminInstructorController(InstructorService instructorService,
                                     ModelMapper modelMapper) {
        this.instructorService = instructorService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add-new")
    @ResponseStatus(HttpStatus.CREATED)
    public InstructorDto saveInstructor(@RequestBody @Valid InstructorDto instructorDto) {
        Instructor instructor = convertToEntity(instructorDto);
        return convertToDto(instructorService.save(instructor));
    }

    @PutMapping("/edit/{id}")
    public InstructorDto updateInstructor
            (@PathVariable @Min(value = 1, message = "*Please provide id grater than 0.") Long id,
             @RequestBody @Valid InstructorDto instructorDto) {
        Instructor instructor = convertToEntity(instructorDto);
        return convertToDto(instructorService.edit(instructor, id));
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteInstructor
            (@PathVariable @Min(value = 1, message = "*Please provide id grater than 0.") Long id) {
        return instructorService.deleteById(id);
    }

    @GetMapping("/find-by-email")
    public List<InstructorDto> findInstructorByEmail
            (@RequestParam @NotBlank(message = "*Please provide not blank input.") String email) {
        return instructorService.findByEmail(email)
                .stream()
                .map(instructor -> convertToDto(instructor))
                .collect(Collectors.toList());
    }

    @PostMapping("/send-email/{id}")
    public Boolean sendEmailToInstructor
            (@PathVariable @Min(value = 1, message = "*Please provide id grater than 0.") Long id,
             @RequestBody @Valid EmailDto emailDto) {
        return instructorService.sendEmailToInstructor(emailDto, id);
    }

    private Instructor convertToEntity(InstructorDto instructorDto) {
        return modelMapper.map(instructorDto, Instructor.class);
    }

    private InstructorDto convertToDto(Instructor instructor) {
        return modelMapper.map(instructor, InstructorDto.class);
    }

// admin can do with instructors:
// - add new instructor,
// - edit existing instructor,
// - delete instructor,
// - send email to instructor,
// - find instructor by email


}
