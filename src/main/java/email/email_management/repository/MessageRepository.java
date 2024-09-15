package email.email_management.repository;

import email.email_management.models.Message;
import email.email_management.models.response.FolderMessagesResponse;
import email.email_management.models.response.MailboxFolderMessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {


    @Query("SELECT new email.email_management.models.response." +
            "FolderMessagesResponse(m.idt, m.sender, m.subject, m.send_at, m.read) " +
            "FROM Mailbox mb INNER JOIN Folder f ON mb.idt = f.mailbox.idt " +
            "INNER JOIN Message m ON m.folder.idt = f.idt " +
            "WHERE mb.idt = :mailboxId AND f.idt = :folderId")
    List<FolderMessagesResponse> findMessagesByMailboxNameAndFolderId(@Param("mailboxId") Integer mailboxId,
                                                                      @Param("folderId") Integer folderId);

    @Query("SELECT new email.email_management.models.response." +
            "FolderMessagesResponse(m.idt, m.sender, m.subject, m.send_at, m.read) " +
            "FROM Mailbox mb INNER JOIN Folder f ON mb.idt = f.mailbox.idt " +
            "INNER JOIN Message m ON m.folder.idt = f.idt " +
            "WHERE mb.idt = :mailboxId AND f.idt = :folderId")
    Page<FolderMessagesResponse> findMessagesByMailboxNameAndFolderId(@Param("mailboxId") Integer mailboxId,
                                                                      @Param("folderId") Integer folderId,
                                                                      PageRequest pageRequest);

    @Query("SELECT new email.email_management.models.response." +
            "MailboxFolderMessageResponse(m.idt, m.sender, m.recipient, m.subject,m.body,m.read, m.send_at) " +
            "FROM Mailbox mb INNER JOIN Folder f ON mb.idt = f.mailbox.idt " +
            "INNER JOIN Message m ON m.folder.idt = f.idt " +
            "WHERE mb.idt = :mailboxId AND f.idt = :folderId AND m.idt = :messageId")
    List<MailboxFolderMessageResponse> findMessagesByMailboxNameAndFolderIdAndMessageId(@Param("mailboxId") Integer mailboxId,
                                                                                        @Param("folderId") Integer folderId,
                                                                                        @Param("messageId") Integer messageId);


}
