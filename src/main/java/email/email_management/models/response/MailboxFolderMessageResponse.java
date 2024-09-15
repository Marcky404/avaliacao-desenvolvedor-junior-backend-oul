package email.email_management.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailboxFolderMessageResponse {

    private Integer idt;
    private String sender;
    private String recipient;
    private String subject;
    private String body;
    private Boolean read;
    private LocalDateTime send_at;

}
