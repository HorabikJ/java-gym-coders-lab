package pl.coderslab.javaGym.entity.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.coderslab.javaGym.entity.Person;

import javax.persistence.*;
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
    private String email;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private LocalDate dateOfBirth;

    @Column(length = 1000)
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.REMOVE)
    private List<TrainingClass> trainingClassList = new ArrayList<>();

//    public Instructor(String email, String firstName, String lastName,
//                      LocalDate dateOfBirth, String description) {
//        this.email = email;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.dateOfBirth = dateOfBirth;
//        this.description = description;
//    }
}
