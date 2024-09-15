package email.email_management.models.response;

import email.email_management.models.Folder;
import email.email_management.models.Mailbox;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailboxResponse {
    private Integer idt;
    private String name;
    private List<Folder> folderList;

    public static MailboxResponse toResponse(Mailbox mailbox) {
        MailboxResponse mailboxResponse = new MailboxResponse();

        mailboxResponse.setIdt(mailbox.getIdt());
        mailboxResponse.setName(mailbox.getName());
        mailboxResponse.setFolderList(mailbox.getFolders());
        return mailboxResponse;
    }


}
