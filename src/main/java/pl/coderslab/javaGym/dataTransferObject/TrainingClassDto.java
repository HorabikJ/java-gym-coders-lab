package pl.coderslab.javaGym.dataTransferObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.coderslab.javaGym.entity.data.Instructor;
import pl.coderslab.javaGym.entity.data.TrainingType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

//    @NotNull
//    @Future(message = "*Start date for a class must be in future.")
//    private ZonedDateTime startDate;

    @NotNull
    @Min(value = 1, message = "*Please provide valid duration time in minutes.")
    private Integer durationInMinutes;

    @NotNull
    @Min(value = 1, message = "*Please provide instructor for a class.")
    private Instructor instructor;

    @NotNull
    @Min(value = 1, message = "*Please provide training type for a class.")
    private TrainingType trainingType;

}
