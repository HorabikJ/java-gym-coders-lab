package pl.coderslab.javaGym.dataTransferObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

}
