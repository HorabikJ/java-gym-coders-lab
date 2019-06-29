package pl.coderslab.javaGym.dataTransferObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.coderslab.javaGym.entity.data.Instructor;
import pl.coderslab.javaGym.entity.data.TrainingType;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TrainingClassDto {

    private Long id;

    private String uniqueClassId;

    @NotNull
    @Min(value = 1, message = "*Minimum value for capacity is 1.")
    private Integer maxCapacity;

    private Integer reservedPlaces;

//  local date time correct format  "2019-06-29T14:34:50"
    @NotNull
    @Future(message = "*Start date for a class must be in future.")
    private LocalDateTime startDate;

    @NotNull
    @Min(value = 1, message = "*Please provide valid duration time in minutes.")
    private Integer durationInMinutes;

    private Instructor instructor;

    private TrainingType trainingType;

}
