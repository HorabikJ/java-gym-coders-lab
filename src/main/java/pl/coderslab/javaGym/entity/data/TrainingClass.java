package pl.coderslab.javaGym.entity.data;

import lombok.*;
import pl.coderslab.javaGym.entity.user.User;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
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
    private Integer maxCapacity;

    @Column
    private ZonedDateTime startDate;

    @Column
    private Integer durationInMinutes;

    @ManyToOne
    private Instructor instructor;

    @ManyToOne
    private TrainingType trainingTypeEntity;

    @ManyToMany
    private List<User> customers = new LinkedList<>();

    @ManyToMany
    private List<User> awaitingCustomers = new LinkedList<>();

//    public TrainingClass(String uniqueClassId, Integer maxCapacity, ZonedDateTime startDate,
//                         Instructor instructor, TrainingType trainingTypeEntity) {
//        this.uniqueClassId = uniqueClassId;
//        this.maxCapacity = maxCapacity;
//        this.startDate = startDate;
//        this.instructor = instructor;
//        this.trainingTypeEntity = trainingTypeEntity;
//    }
}
