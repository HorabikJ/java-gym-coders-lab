package pl.coderslab.javaGym.entity.data;

import lombok.*;
import pl.coderslab.javaGym.entity.Person;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table
public class Instructor implements Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(unique = true)
    @Email(message = "*Please provide a valid confirmationEmail.")
    @NotBlank(message = "*Please provide an confirmationEmail.")
    private String email;

    @Column
    @NotBlank(message = "*Name can not be empty.")
    private String firstName;

    @Column
    @NotBlank(message = "*Last name can not be empty.")
    private String lastName;

    @Column
    @NotNull(message = "*Please provide a valid date of birth.")
    private LocalDate dateOfBirth;

    @Column(length = 1000)
    @Size(min = 1, max = 1000, message = "*Description can not be empty and can not be longer that 1000 signs.")
    private String description;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.REMOVE)
    private List<TrainingClass> trainingClassList = new ArrayList<>();

    public Instructor(String email, String firstName, String lastName,
                      LocalDate dateOfBirth, String description) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.description = description;
    }
}
