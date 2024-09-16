package email.email_management.models.response;

import email.email_management.models.Folder;
import email.email_management.models.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {


    private Integer idt;
    private String sender;
    private String recipient;
    private String subject;
    private String body;
    private Boolean read;
    private Folder folder;

    public static MessageResponse toResponse(Message message) {
        MessageResponse messageResponse = new MessageResponse();

        messageResponse.setIdt(message.getIdt());
        messageResponse.setSender(message.getSender());
        messageResponse.setRecipient(message.getRecipient());
        messageResponse.setSubject(message.getSubject());
        messageResponse.setBody(message.getBody());
        messageResponse.setRead(message.getRead());
        messageResponse.setFolder(message.getFolder());

        return messageResponse;
    }
}
