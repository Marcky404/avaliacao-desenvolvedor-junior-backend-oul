package email.email_management.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestControllerAdvice
public class ControlExceptionHandler {

    public static final String CONSTRAINT_VALIDATION_FAILED = "Constraint validation failed";

    @ExceptionHandler(value = {BusinessException.class})
    protected ResponseEntity<Object> handleConflict(BusinessException ex, WebRequest request) {

        return ResponseEntity.status(ex.getHttpStatusCode()).body(ex.toBody());

    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException exMethod, WebRequest request) {


        BusinessException ex = BusinessException.builder()
                .httpStatusCode(HttpStatus.CONFLICT)
                .message(CONSTRAINT_VALIDATION_FAILED)
                .description(exMethod.getCause().getLocalizedMessage())
                .build();

        return ResponseEntity.status(ex.getHttpStatusCode()).body(ex.toBody());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validationError(MethodArgumentNotValidException exMethod, WebRequest request) {

        BindingResult bindingResult = exMethod.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        var message = fieldErrors.stream().findFirst().get().getDefaultMessage();

        List<String> fieldErrorDtos = fieldErrors.stream()
                .map(f -> "{'".concat(f.getField()).concat("':'").concat(f.getDefaultMessage()).concat("'}")).map(String::new)
                .toList();

        BusinessException ex = BusinessException.builder()
                .httpStatusCode(HttpStatus.BAD_REQUEST)
                .description(fieldErrorDtos.toString())
                .message(CONSTRAINT_VALIDATION_FAILED)
                .build();


        return ResponseEntity.status(ex.getHttpStatusCode()).body(ex.toBody());
    }

}
