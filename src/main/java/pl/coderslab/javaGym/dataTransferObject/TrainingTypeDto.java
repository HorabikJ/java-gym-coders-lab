package pl.coderslab.javaGym.dataTransferObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.coderslab.javaGym.entity.data.TrainingClass;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrainingTypeDto {

    private Long id;

    @NotBlank(message = "*Training name can not be empty.")
    private String name;

    @Size(min = 1, max = 1000, message = "*Description can not be empty and can not be longer that 1000 signs.")
    private String description;

    private List<TrainingClass> trainingClasses = new ArrayList<>();

//    public TrainingTypeDto(String name, String description) {
//        this.name = name;
//        this.description = description;
//    }
}
