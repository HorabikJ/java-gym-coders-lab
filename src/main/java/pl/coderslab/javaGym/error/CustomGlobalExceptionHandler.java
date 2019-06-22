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
import pl.coderslab.javaGym.error.customException.NotAuthenticatedException;
import pl.coderslab.javaGym.error.customException.ResourceNotFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({DomainObjectException.class, NotAuthenticatedException.class, ResourceNotFoundException.class})
    public ResponseEntity<CustomErrorResponse> handleCustomException
            (Exception exception, WebRequest request) {

        String fieldName = getErrorMessageFieldName(exception);
        CustomErrorResponse errors = new CustomErrorResponse();
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put(fieldName, exception.getMessage());

        errors.setTimestamp(LocalDateTime.now());
        errors.setStatus(HttpStatus.BAD_REQUEST.value());
        errors.setErrors(errorMessage);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    private String getErrorMessageFieldName(Exception exception) {
        String fieldName = "error";
        Class exceptionClass = exception.getClass();
        if (exceptionClass.equals(DomainObjectException.class)) {
            fieldName = "email";
        } else if (exceptionClass.equals(NotAuthenticatedException.class)) {
            fieldName = "user";
        } else if (exceptionClass.equals(ResourceNotFoundException.class)) {
            fieldName = "error";
        }
        return fieldName;
    }

    // error handle for @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid
    (MethodArgumentNotValidException exception,
     HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, String> errorMessages = new HashMap<>();
        CustomErrorResponse errors = new CustomErrorResponse();
        BindingResult bindingResult = exception.getBindingResult();

        for (ObjectError objectError : bindingResult.getAllErrors()) {
            String fieldName = ((FieldError) objectError).getField();
            String defaultMessage = objectError.getDefaultMessage();
            errorMessages.put(fieldName, defaultMessage);
        }

        errors.setTimestamp(LocalDateTime.now());
        errors.setStatus(status.value());
        errors.setErrors(errorMessages);

        return new ResponseEntity<>(errors, headers, status);
    }

    //  @Validate For Validating Path Variables and Request Parameters
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<CustomErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        Map<String, String> errorMessages = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errorMessages.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setStatus(HttpStatus.BAD_REQUEST.value());
        errors.setErrors(errorMessages);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
