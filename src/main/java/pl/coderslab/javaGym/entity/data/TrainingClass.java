package pl.coderslab.javaGym.entity.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.coderslab.javaGym.entity.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private LocalDateTime startDate;

    @Column
    private Integer durationInMinutes;

    @ManyToOne
    private Instructor instructor;

    @ManyToOne
    private TrainingType trainingType;

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
