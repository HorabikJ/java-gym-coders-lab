package pl.coderslab.javaGym.dataTransferObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.coderslab.javaGym.entity.data.TrainingClass;
import pl.coderslab.javaGym.entity.user.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {

    private Long id;

    @Email(message = "*Please provide a valid confirmationEmail.")
    @NotBlank(message = "*Please provide an confirmationEmail.")
    private String email;

    @Size(min = 5, message = "*Your password must have at least 5 characters.")
    @NotBlank(message = "*Please provide your password.")
    private String password;

    @NotBlank(message = "*Name can not be empty.")
    private String firstName;

    @NotBlank(message = "*Last name can not be empty.")
    private String lastName;

    private Boolean active;

    @NotNull(message = "*Please agree or disagree for newsletter.")
    private Boolean newsletter;

    @JsonIgnore
    private List<TrainingClass> trainingClasses = new LinkedList<>();

    @JsonIgnore
    private List<TrainingClass> awaitingClasses = new LinkedList<>();

    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

}
