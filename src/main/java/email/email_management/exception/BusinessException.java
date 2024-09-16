package email.email_management.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Slf4j
public class BusinessException extends RuntimeException {

    private HttpStatus httpStatusCode;
    private String message;
    private String description;

    public BusinessException(HttpStatus httpStatus, String message) {
        this.httpStatusCode = httpStatus;
        this.message = message;
    }

    public BusinessExceptionBody toBody() {
        return BusinessExceptionBody.builder()
                .message(this.message)
                .description(this.description)
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BusinessExceptionBody {
        private String message;
        private String description;
    }
}