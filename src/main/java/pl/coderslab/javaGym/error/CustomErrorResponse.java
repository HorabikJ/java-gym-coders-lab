package pl.coderslab.javaGym.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
class CustomErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;

    private String HttpStatus;

    private Map<String, String> errors;

}
