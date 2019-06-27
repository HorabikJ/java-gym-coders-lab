package pl.coderslab.javaGym.entity.data;

import lombok.*;
import pl.coderslab.javaGym.entity.user.User;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table
public class TrainingClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String uniqueClassId;

    @Column
    @NotNull
    @Min(value = 1, message = "*Minimum value for capacity is 1.")
    private Integer maxCapacity;

    @Column
    @Future(message = "*Start date for a class must be in future.")
    private ZonedDateTime startDate;

    @Column
    @NotNull
    @Min(value = 1, message = "*Please provide valid duration time in minutes.")
    private Integer durationInMinutes;

    @ManyToOne
    @NotNull(message = "*Please provide instructor for a class.")
    private Instructor instructor;

    @ManyToOne
    @NotNull(message = "*Please provide training type for a class.")
    private TrainingType trainingType;

    @ManyToMany
    private List<User> customers = new LinkedList<>();

    @ManyToMany
    private List<User> awaitingCustomers = new LinkedList<>();

    public TrainingClass(String uniqueClassId, Integer maxCapacity, ZonedDateTime startDate,
                         Instructor instructor, TrainingType trainingType) {
        this.uniqueClassId = uniqueClassId;
        this.maxCapacity = maxCapacity;
        this.startDate = startDate;
        this.instructor = instructor;
        this.trainingType = trainingType;
    }
}
