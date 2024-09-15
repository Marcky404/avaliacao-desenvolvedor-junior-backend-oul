package email.email_management.models.request;

import email.email_management.models.Mailbox;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailboxRequest {

    @NotNull(message = "E-mail não pode ser nulo")
    @NotBlank(message = "E-mail deve ser preenchido")
    @Pattern(regexp = "^[\\w\\-]+(\\.[\\w\\-]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$", message = "Nome da caixa é invalido")
    private String name;

    public static Mailbox toEntity(MailboxRequest mailboxRequest) {
        Mailbox mailbox = new Mailbox();
        mailbox.setName(mailboxRequest.getName());
        return mailbox;
    }

}
