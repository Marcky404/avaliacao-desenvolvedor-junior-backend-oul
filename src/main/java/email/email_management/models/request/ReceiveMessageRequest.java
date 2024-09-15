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
public class ReceiveMessageRequest {

    @NotNull(message = "E-mail não pode ser nulo")
    @NotBlank(message = "E-mail deve ser preenchido")
    @Pattern(regexp = "^[\\w\\-]+(\\.[\\w\\-]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$", message = "Destinatario inválido")
    private String sender;
    @Size(min = 1, max = 50, message = "Assunto inválido")
    private String subject;
    private String body;
    private String folder;

    public static Message toEntity(ReceiveMessageRequest receiveMessageRequest) {
        Message message = new Message();

        message.setSender(receiveMessageRequest.getSender());
        message.setRecipient(receiveMessageRequest.getSender());
        message.setSubject(ValidateFields.setDefaultSubjectIfNull(receiveMessageRequest.getSubject()));
        message.setBody(receiveMessageRequest.getBody());
        message.setRead(false);

        return message;
    }
}
