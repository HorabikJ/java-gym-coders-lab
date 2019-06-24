package pl.coderslab.javaGym.entity.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.coderslab.javaGym.entity.user.User;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @NotNull
    @Column
    @Min(value = 1, message = "*Minimum value for capacity is 1.")
    private Integer maxCapacity;

    @Column
    @Future(message = "*Start date for a class must be in future.")
    private LocalDateTime startDate;

    @ManyToOne
    @NotNull(message = "*Please provide instructor for a class.")
    private Instructor instructor;

    @ManyToOne
    @NotNull(message = "*Please provide training type for a class.")
    private TrainingType trainingType;

    @ManyToMany
    private List<User> customers = new ArrayList<>();

}
