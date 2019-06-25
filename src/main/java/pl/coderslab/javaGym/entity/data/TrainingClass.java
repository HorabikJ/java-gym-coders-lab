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
import java.util.List;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table
public class TrainingClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NonNull
    @Column
    private String uniqueClassId;

    @NonNull
    @NotNull
    @Column
    @Min(value = 1, message = "*Minimum value for capacity is 1.")
    private Integer maxCapacity;

    @NonNull
    @Column
    @Future(message = "*Start date for a class must be in future.")
    private ZonedDateTime startDate;

    @NonNull
    @ManyToOne
    @NotNull(message = "*Please provide instructor for a class.")
    private Instructor instructor;

    @NonNull
    @ManyToOne
    @NotNull(message = "*Please provide training type for a class.")
    private TrainingType trainingType;

    @ManyToMany
    private List<User> customers = new ArrayList<>();

}
