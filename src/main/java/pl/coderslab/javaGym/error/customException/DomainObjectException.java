package pl.coderslab.javaGym.error.customException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DomainObjectException extends RuntimeException {

    private String message = "*Provided email is already registered in database.";

}
