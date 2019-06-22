package pl.coderslab.javaGym.error.customException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResourceNotFoundException extends RuntimeException {

    private final String fieldName = "obiect";

    private String message = "*Resource not found exception.";

}
