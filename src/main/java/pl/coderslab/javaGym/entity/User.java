package pl.coderslab.javaGym.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotEmpty(message = "*Please provide an email.")
    private String email;

    @Column
    @Size(min = 5, message = "*Your password must have at least 5 characters.")
    @NotEmpty(message = "*Please provide your password.")
    private String password;

    @Column
    @NotEmpty(message = "*Please provide your name.")
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty(message = "*Please provide your last name.")
    private String lastName;

    @Column
    private Integer active;

    @Column
    @NotNull(message = "*Please agree or disagree for newsletter.")
    private Boolean newsletter;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

}
