package pl.coderslab.javaGym.error.customException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NotAuthenticatedException extends RuntimeException {

    private final String fieldName = "user";

    private String message = "*Not authenticated exception.";

}
