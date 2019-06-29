package pl.coderslab.javaGym.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pl.coderslab.javaGym.entity.Person;
import pl.coderslab.javaGym.entity.data.TrainingClass;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

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
    private String email;

    @Column
    private String password;

    @Column
    private String firstName;

    private String lastName;

    @Column(nullable = false)
    private Boolean active;

    @Column
    private Boolean newsletter;

    @ManyToMany(mappedBy = "customers")
    private List<TrainingClass> trainingClasses = new LinkedList<>();

    @ManyToMany(mappedBy = "awaitingCustomers")
    private List<TrainingClass> awaitingClasses = new LinkedList<>();

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

//    public User(String email, String password, String firstName, String lastName, Boolean newsletter) {
//        this.email = email;
//        this.password = password;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.newsletter = newsletter;
//    }

}
