package email.email_management.service;

import ch.qos.logback.core.util.StringUtil;
import email.email_management.exception.BusinessException;
import email.email_management.models.Folder;
import email.email_management.models.Mailbox;
import email.email_management.models.Message;
import email.email_management.models.request.MessageRequest;
import email.email_management.models.request.ReadRequest;
import email.email_management.models.request.ReceiveMessageRequest;
import email.email_management.models.response.FolderMessagesResponse;
import email.email_management.models.response.MailboxFolderMessageResponse;
import email.email_management.models.response.MessageResponse;
import email.email_management.repository.MessageRepository;
import email.email_management.utils.ValidateFields;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {


    private final MessageRepository repository;
    private final MailboxService mailboxService;
    private final FolderService folderService;

    public void create(String mailbox, MessageRequest messageRequest) {
        Message message = MessageRequest.toEntity(messageRequest);
        Mailbox mailboxEntity = mailboxService.findByName(mailbox);

        for (Folder f : mailboxEntity.getFolders()) {
            if (f.getName().equals("SENT")) {
                message.setFolder(f);
            }
        }

        repository.save(message);
    }

    public Message findByIdt(Integer idt) {
        return repository.findById(idt).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Messagem não existe"));
    }

    public void receiveMessage(String mailbox, ReceiveMessageRequest receiveMessageRequest) {
        Message message = ReceiveMessageRequest.toEntity(receiveMessageRequest);
        Mailbox mailboxEntity = mailboxService.findByName(mailbox);

        Folder folder = validateDefaultFolder(mailboxEntity, receiveMessageRequest.getFolder());

        message.setFolder(folder);

        repository.save(message);
    }

    @Transactional
    public MessageResponse update(String mailbox, Integer folderIdt, Integer messageIdt, ReadRequest readRequest) {
        Mailbox mailboxEntity = mailboxService.findByName(mailbox);
        Message message = findByIdt(messageIdt);
        Folder folder = folderService.findByIdt(folderIdt);

        folder.setMailbox(mailboxEntity);
        message.setFolder(folder);
        message.setRead(readRequest.getRead());


        return MessageResponse.toResponse(repository.save(message));
    }

    public List<FolderMessagesResponse> findMessagesByMailboxNameAndFolderId(String mailbox, Integer folderIdt) {
        Mailbox mailboxEntity = mailboxService.findByName(mailbox);
        Folder folderEntity = folderService.findByIdt(folderIdt);

        return repository.findMessagesByMailboxNameAndFolderId(mailboxEntity.getIdt(), folderEntity.getIdt());
    }

    public Page<FolderMessagesResponse> findMessagesByMailboxNameAndFolderId(String mailbox, Integer folderIdt, PageRequest pageRequest) {
        Mailbox mailboxEntity = mailboxService.findByName(mailbox);
        Folder folderEntity = folderService.findByIdt(folderIdt);

        return repository.findMessagesByMailboxNameAndFolderId(mailboxEntity.getIdt(), folderEntity.getIdt(), pageRequest);
    }

    public List<MailboxFolderMessageResponse> findMessagesByMailboxNameAndFolderIdAndMessageId(String mailbox, Integer folderIdt, Integer messageIdt) {
        Mailbox mailboxEntity = mailboxService.findByName(mailbox);
        Folder folderEntity = folderService.findByIdt(folderIdt);
        Message messageEntity = findByIdt(messageIdt);

        return repository.findMessagesByMailboxNameAndFolderIdAndMessageId(mailboxEntity.getIdt(), folderEntity.getIdt(), messageEntity.getIdt());
    }

     private Folder validateDefaultFolder(Mailbox mailbox, String folderName) {
        if (StringUtil.isNullOrEmpty(folderName)) {
            for (Folder f : mailbox.getFolders()) {
                if (f.getName().equals("INBOX")) {
                    return f;
                }
            }
        }

        return filterFolder(mailbox, folderName);
    }

    private static Folder filterFolder(Mailbox mailbox, String folderName) {
        return mailbox.getFolders().stream()
                .filter(f -> f.getName().equals(folderName))
                .findFirst().orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Pasta Não encontrada"));

    }


}
