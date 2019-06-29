package pl.coderslab.javaGym.dataTransferObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.coderslab.javaGym.entity.data.TrainingClass;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InstructorDto {

    private Long id;

    @Email(message = "*Please provide a valid confirmationEmail.")
    @NotBlank(message = "*Please provide an confirmationEmail.")
    private String email;

    @NotBlank(message = "*Name can not be empty.")
    private String firstName;

    @NotBlank(message = "*Last name can not be empty.")
    private String lastName;

    @NotNull(message = "*Please provide a valid date of birth.")
    private LocalDate dateOfBirth;

    @Size(min = 1, max = 1000, message = "*Description can not be empty and can not be longer that 1000 signs.")
    private String description;

    private List<TrainingClass> trainingClassList = new ArrayList<>();

//    public InstructorDto(String email, String firstName, String lastName,
//                      LocalDate dateOfBirth, String description) {
//        this.email = email;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.dateOfBirth = dateOfBirth;
//        this.description = description;
//    }

}
