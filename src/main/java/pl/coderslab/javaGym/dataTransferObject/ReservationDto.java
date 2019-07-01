package pl.coderslab.javaGym.dataTransferObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReservationDto {

    private Long id;

    private UserDto userDto;

    private TrainingClassDto trainingClassDto;

    private LocalDateTime reservationTime;

    private Boolean onTrainingList;

}
