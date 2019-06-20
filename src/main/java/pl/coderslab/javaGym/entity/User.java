package pl.coderslab.javaGym.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(unique = true)
    @Email(message = "*Please provide a valid email.")
    @NotBlank(message = "*Please provide an email.")
    private String email;

    @Column
    @Size(min = 5, message = "*Your password must have at least 5 characters.")
    @NotBlank(message = "*Please provide your password.")
    private String password;

    @Column
    @NotBlank(message = "*Name can not be empty.")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "*Last name can not be empty.")
    private String lastName;

    @Column
    @Max(value = 1, message = "*Only 0 as inactive or 1 as active are acceptable.")
    @Min(value = 0, message = "*Only 0 as inactive or 1 as active are acceptable.")
    private Integer active;

    @Column
    @NotNull(message = "*Please agree or disagree for newsletter.")
    private Boolean newsletter;

    @ManyToMany(mappedBy = "customers")
    private List<TrainingClass> trainingClasses = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

}
