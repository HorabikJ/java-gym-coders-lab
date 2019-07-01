package pl.coderslab.javaGym.entity.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.coderslab.javaGym.entity.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private TrainingClass trainingClass;

    @Column
    private LocalDateTime reservationTime;

    @Column
    private Boolean onTrainingList;

    public Reservation(User user, TrainingClass trainingClass, LocalDateTime reservationTime,
                       Boolean onTrainingList) {
        this.user = user;
        this.trainingClass = trainingClass;
        this.reservationTime = reservationTime;
        this.onTrainingList = onTrainingList;
    }

}
