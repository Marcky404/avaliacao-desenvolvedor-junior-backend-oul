package email.email_management.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailboxListResponse {

    private String name;

    public static MailboxListResponse toResponse(String name) {
        MailboxListResponse mailboxListResponse = new MailboxListResponse();

        mailboxListResponse.setName(name);

        return mailboxListResponse;
    }
}
