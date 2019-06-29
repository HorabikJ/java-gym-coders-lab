package pl.coderslab.javaGym.entity.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "trainingTypeEntity", cascade = CascadeType.REMOVE)
    private List<TrainingClass> trainingClasses = new ArrayList<>();

//    public TrainingType(String name, String description) {
//        this.name = name;
//        this.description = description;
//    }
}
