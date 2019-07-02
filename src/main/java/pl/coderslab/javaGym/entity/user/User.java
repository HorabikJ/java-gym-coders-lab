package pl.coderslab.javaGym.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.coderslab.javaGym.entity.Person;
import pl.coderslab.javaGym.entity.data.Reservation;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
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
