package email.email_management.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FolderMessagesResponse {

    private Integer idt;
    private String sender;
    private String subject;
    private LocalDateTime sand_at;
    private Boolean read;

}
