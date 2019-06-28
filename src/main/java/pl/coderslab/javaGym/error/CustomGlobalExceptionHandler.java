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
import pl.coderslab.javaGym.error.customException.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({DomainObjectException.class,
            UserUnauthorizedException.class,
            ResourceNotFoundException.class,
            PasswordDoNotMatchException.class,
            LinkExpiredException.class,
            EmailSendingException.class,
            ActionNotAllowedException.class})
    public ResponseEntity<CustomErrorResponse> handleCustomException
            (Exception exception, WebRequest request) {

        String fieldName = getErrorMessageFieldName(exception);
        HttpStatus httpStatus = getHttpStatus(exception);
        CustomErrorResponse errors = new CustomErrorResponse();
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put(fieldName, exception.getMessage());

        errors.setTimestamp(LocalDateTime.now());
        errors.setHttpStatus(httpStatus.toString());
        errors.setErrors(errorMessage);

        return new ResponseEntity<>(errors, httpStatus);
    }

    private String getErrorMessageFieldName(Exception exception) {
        String fieldName = "error";
        Class exceptionClass = exception.getClass();
        if (exceptionClass.equals(DomainObjectException.class)) {
            fieldName = "email";
        } else if (exceptionClass.equals(UserUnauthorizedException.class)) {
            fieldName = "user";
        } else if (exceptionClass.equals(PasswordDoNotMatchException.class)) {
            fieldName = "password";
        }
        return fieldName;
    }

    private HttpStatus getHttpStatus(Exception exception) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        Class exceptionClass = exception.getClass();
        if (exceptionClass.equals(UserUnauthorizedException.class)) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        } else if (exceptionClass.equals(ResourceNotFoundException.class)) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (exceptionClass.equals(LinkExpiredException.class)) {
            httpStatus = HttpStatus.REQUEST_TIMEOUT;
        }
        return httpStatus;
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
        errors.setHttpStatus(status.toString());
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
        errors.setHttpStatus(HttpStatus.BAD_REQUEST.toString());
        errors.setErrors(errorMessages);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
