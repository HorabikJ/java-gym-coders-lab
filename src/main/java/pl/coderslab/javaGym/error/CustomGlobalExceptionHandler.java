package pl.coderslab.javaGym.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.coderslab.javaGym.error.customException.DomainObjectException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DomainObjectException.class)
    public ResponseEntity<CustomErrorResponse> customDomainObjectException
            (Exception exception, WebRequest request) {

        CustomErrorResponse errors = new CustomErrorResponse();
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("email", exception.getMessage());

        errors.setTimestamp(LocalDateTime.now());
        errors.setStatus(HttpStatus.BAD_REQUEST.value());
        errors.setErrors(errorMessage);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid
            (MethodArgumentNotValidException exception,
             HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, String> errorMessages = new HashMap<>();
        CustomErrorResponse errors = new CustomErrorResponse();
        BindingResult bindingResult = exception.getBindingResult();

        for (ObjectError objectError : bindingResult.getAllErrors()) {
            String fieldName = ((FieldError)objectError).getField();
            String defaultMessage = objectError.getDefaultMessage();
            errorMessages.put(fieldName, defaultMessage);
        }

        errors.setTimestamp(LocalDateTime.now());
        errors.setStatus(status.value());
        errors.setErrors(errorMessages);

        return new ResponseEntity<>(errors, headers, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

}
