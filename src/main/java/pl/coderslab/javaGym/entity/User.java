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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column
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
    private Integer active;

    @Column
    @NotNull(message = "*Please agree or disagree for newsletter.")
    private Boolean newsletter;

    @ManyToMany(mappedBy = "customerList")
    private List<TrainingClass> trainingClassList = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

}
