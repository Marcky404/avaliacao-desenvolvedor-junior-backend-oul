package email.email_management.models.request;


import email.email_management.models.Message;
import email.email_management.utils.ValidateFields;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    @NotNull(message = "E-mail não pode ser nulo")
    @NotBlank(message = "E-mail deve ser preenchido")
    @Pattern(regexp = "^[\\w\\-]+(\\.[\\w\\-]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$", message = "Destinatario inválido")
    private String recipient;
    @Size(min = 1,max = 50, message = "Assunto inválido")
    private String subject;
    private String body;

    public static Message toEntity(MessageRequest messageRequest) {
        Message message = new Message();

        message.setRecipient(messageRequest.getRecipient());
        message.setSubject(ValidateFields.setDefaultSubjectIfNull(messageRequest.getSubject()));
        message.setBody(messageRequest.getBody());
        message.setRead(true);

        return message;
    }

}
