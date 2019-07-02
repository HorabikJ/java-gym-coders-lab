package pl.coderslab.javaGym.entity.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String classGroupId;

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

    @JsonIgnore
    @OneToMany(mappedBy = "trainingClass", cascade = CascadeType.REMOVE)
    private List<Reservation> reservations = new LinkedList<>();

}
