package pl.coderslab.javaGym.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pl.coderslab.javaGym.entity.Person;
import pl.coderslab.javaGym.entity.data.Reservation;
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

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Reservation> reservations = new LinkedList<>();

    @JsonIgnore
    @ManyToMany
//     TODO       (cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

}
