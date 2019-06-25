package pl.coderslab.javaGym.entity.data;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NonNull
    @Column
    @NotBlank(message = "*Training name can not be empty.")
    private String name;

    @NonNull
    @Column(length = 1000)
    @Size(min = 1, max = 1000, message = "*Description can not be empty and can not be longer that 1000 signs.")
    private String description;

    @OneToMany(mappedBy = "trainingType", cascade = CascadeType.REMOVE)
    private List<TrainingClass> trainingClasses = new ArrayList<>();

}
