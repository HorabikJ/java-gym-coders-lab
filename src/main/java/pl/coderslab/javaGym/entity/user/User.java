package pl.coderslab.javaGym.entity.user;

import lombok.*;
import pl.coderslab.javaGym.entity.Person;
import pl.coderslab.javaGym.entity.data.TrainingClass;

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
public class User implements Person {

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

    @Column(nullable = false)
    private Integer active;

    @Column
    @NotNull(message = "*Please agree or disagree for newsletter.")
    private Boolean newsletter;

    @ManyToMany(mappedBy = "customers")
    private List<TrainingClass> trainingClasses = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User(String email, String password, String firstName, String lastName, Boolean newsletter) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.newsletter = newsletter;
    }

}
