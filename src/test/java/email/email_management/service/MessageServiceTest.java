package email.email_management.service;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    public static final int MESSAGE_IDT = 1;
    public static final String MAILBOX_NAME = "joaopereira@cliente.com";

    @InjectMocks
    MessageService service;

    @Mock
    MessageRepository repository;
    @Mock
    MailboxService mailboxService;
    @Mock
    FolderService folderService;


    @Test
    void creteSuccessfully() {

        Message message = getMessage();

        when(mailboxService.findByName(anyString())).thenReturn(getMailbox());
        when(repository.save(any())).thenReturn(message);
        service.create(MAILBOX_NAME, getMessageRequest());

        verify(repository, times(1)).save(any());
    }

    @Test
    void findByIdtSuccessfully() {

        Message message = getMessage();

        when(repository.findById(any())).thenReturn(Optional.of(getMessage()));
        Message response = service.findByIdt(MESSAGE_IDT);

        assertThat(response).isNotNull();
        assertThat(response.getIdt()).isEqualTo(message.getIdt());
        assertThat(response.getSender()).isEqualTo(message.getSender());
        assertThat(response.getRecipient()).isEqualTo(message.getRecipient());
        assertThat(response.getSubject()).isEqualTo(message.getSubject());
        assertThat(response.getBody()).isEqualTo(message.getBody());
        assertThat(response.getRead()).isEqualTo(message.getRead());

    }

    @Test
    void findByIdt_WhenMessageAlreadyExists_ReturnsNotFound() {

        when(repository.findById(any())).thenReturn(Optional.empty());

        var exception = assertThrows(BusinessException.class,
                () -> service.findByIdt(MESSAGE_IDT));

        assertThat(exception.getMessage()).isEqualTo("Messagem não existe");
    }

    @Test
    void receiveMessageSuccessfully() {

        Message message = getMessage();

        when(mailboxService.findByName(MAILBOX_NAME)).thenReturn(getMailbox());
        when(repository.save(any())).thenReturn(getMessage());
        service.receiveMessage(MAILBOX_NAME, getReceiveMessageRequest());

        verify(repository, times(1)).save(any());

    }

    @Test
    void receiveMessageValidateDefaultFolder_whenFolderNameIsNull_shouldReturnInboxFolder() {

        Message message = getMessage();
        ReceiveMessageRequest receiveRequestFolderNameIsNull = getReceiveMessageRequest();
        receiveRequestFolderNameIsNull.setFolder(null);

        when(mailboxService.findByName(MAILBOX_NAME)).thenReturn(getMailbox());
        when(repository.save(any())).thenReturn(getMessage());
        service.receiveMessage(MAILBOX_NAME, receiveRequestFolderNameIsNull);

        verify(repository, times(1)).save(any());
    }

    @Test
    void receiveMessageValidateDefaultFolder_whenFolderNameIsEmpty_shouldReturnInboxFolder() {

        Message message = getMessage();
        ReceiveMessageRequest receiveRequestFolderNameIsEmpty = getReceiveMessageRequest();
        receiveRequestFolderNameIsEmpty.setFolder("");

        when(mailboxService.findByName(MAILBOX_NAME)).thenReturn(getMailbox());
        when(repository.save(any())).thenReturn(getMessage());
         service.receiveMessage(MAILBOX_NAME, receiveRequestFolderNameIsEmpty);

        verify(repository, times(1)).save(any());
    }

    @Test
    void receiveMessage_WhenFolderAlreadyExists_BadRequest() {

        ReceiveMessageRequest receiveRequestFolderNameIsEmpty = getReceiveMessageRequest();
        receiveRequestFolderNameIsEmpty.setFolder("TEST");

        when(mailboxService.findByName(MAILBOX_NAME)).thenReturn(getMailbox());

        var exception = assertThrows(BusinessException.class,
                () -> service.receiveMessage(MAILBOX_NAME, receiveRequestFolderNameIsEmpty));

        assertThat(exception.getMessage()).isEqualTo("Pasta Não encontrada");
    }


    @Test
    void updateSuccessfully() {
        Mailbox mailbox = getMailbox();
        Message message = getMessage();
        Folder folder = getFolder();
        ReadRequest readRequest = new ReadRequest(true);

        when(mailboxService.findByName(anyString())).thenReturn(mailbox);
        when(repository.findById(anyInt())).thenReturn(Optional.of(message));
        when(folderService.findByIdt(anyInt())).thenReturn(folder);
        when(repository.save(any(Message.class))).thenReturn(message);

        MessageResponse response = service.update(MAILBOX_NAME, folder.getIdt(), message.getIdt(), readRequest);

        assertThat(response).isNotNull();
        assertThat(response.getIdt()).isEqualTo(message.getIdt());
        assertThat(response.getRead()).isTrue();
    }

    @Test
    void findMessagesByMailboxNameAndFolderIdSuccessfully() {
        Mailbox mailbox = getMailbox();
        Folder folder = getFolder();
        List<FolderMessagesResponse> messages = getFolderMessageResponse();

        when(mailboxService.findByName(anyString())).thenReturn(mailbox);
        when(folderService.findByIdt(anyInt())).thenReturn(folder);
        when(repository.findMessagesByMailboxNameAndFolderId(anyInt(), anyInt())).thenReturn(messages);

        List<FolderMessagesResponse> response = service.findMessagesByMailboxNameAndFolderId(MAILBOX_NAME, folder.getIdt());

        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(1);
    }

    @Test
    void findMessagesByMailboxNameAndFolderIdWithPaginationSuccessfully() {
        Mailbox mailbox = getMailbox();
        Folder folder = getFolder();
        PageRequest pageRequest = getPageRequest();
        Page<FolderMessagesResponse> messagesPage = new PageImpl<>(getFolderMessageResponse());

        when(mailboxService.findByName(anyString())).thenReturn(mailbox);
        when(folderService.findByIdt(anyInt())).thenReturn(folder);
        when(repository.findMessagesByMailboxNameAndFolderId(anyInt(), anyInt(), any(PageRequest.class))).thenReturn(messagesPage);

        Page<FolderMessagesResponse> response = service.findMessagesByMailboxNameAndFolderId(MAILBOX_NAME, folder.getIdt(), pageRequest);

        assertThat(response).isNotNull();
        assertThat(response.getContent().size()).isEqualTo(1);
    }

    @Test
    void findMessagesByMailboxNameAndFolderIdAndMessageIdSuccessfully() {
        Mailbox mailbox = getMailbox();
        Folder folder = getFolder();
        Message message = getMessage();
        List<MailboxFolderMessageResponse> messages = getMailboxFolderMessageResponse();

        when(mailboxService.findByName(anyString())).thenReturn(mailbox);
        when(folderService.findByIdt(anyInt())).thenReturn(folder);
        when(repository.findById(anyInt())).thenReturn(Optional.of(message));
        when(repository.findMessagesByMailboxNameAndFolderIdAndMessageId(anyInt(), anyInt(), anyInt())).thenReturn(messages);

        List<MailboxFolderMessageResponse> response = service.findMessagesByMailboxNameAndFolderIdAndMessageId(MAILBOX_NAME, folder.getIdt(), message.getIdt());

        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(1);
    }


    private MessageRequest getMessageRequest() {
        return new MessageRequest(MAILBOX_NAME,
                "Encontro em familia", "Olá Primo");
    }

    private Message getMessage() {

        Message message = new Message();

        message.setIdt(MESSAGE_IDT);
        message.setSender("anasilva@empresa.com");
        message.setRecipient(MAILBOX_NAME);
        message.setSubject("Encontro em familia");
        message.setBody("Olá Primo");
        message.setRead(false);
        message.setSend_at(LocalDateTime.now());

        return message;
    }

    private MessageResponse getMessageResponse() {

        MessageResponse messageResponse = new MessageResponse();

        messageResponse.setIdt(MESSAGE_IDT);
        messageResponse.setSender("anasilva@empresa.com");
        messageResponse.setRecipient(MAILBOX_NAME);
        messageResponse.setSubject("Encontro em familia");
        messageResponse.setBody("Olá Primo");
        messageResponse.setRead(false);

        return messageResponse;
    }

    private ReceiveMessageRequest getReceiveMessageRequest() {

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest();

        receiveMessageRequest.setSender("anasilva@empresa.com");
        receiveMessageRequest.setSubject("Encontro em familia");
        receiveMessageRequest.setBody("Olá Primo");
        receiveMessageRequest.setFolder("JUNK");

        return receiveMessageRequest;
    }

    private Mailbox getMailbox() {
        return new Mailbox(1, MAILBOX_NAME, createFolderBase());
    }

    private List<Folder> createFolderBase() {

        Folder inbox = new Folder();
        inbox.setName("INBOX");

        Folder junk = new Folder();
        junk.setName("JUNK");

        Folder sent = new Folder();
        sent.setName("SENT");

        return Arrays.asList(inbox, junk, sent);
    }

    private Folder getFolder() {

        return new Folder(1, "JUNK", getMailbox(), getMessageList());
    }

    private List<Message> getMessageList() {

        Message message = new Message();

        message.setIdt(1);
        message.setSender("anasilva@empresa.com");
        message.setRecipient(MAILBOX_NAME);
        message.setSubject("Reunião de Projeto - Atualização");
        message.setBody("Olá João");
        message.setRead(false);

        return Arrays.asList(message);
    }

    private List<FolderMessagesResponse> getFolderMessageResponse() {

        FolderMessagesResponse folderMessagesResponse = new FolderMessagesResponse();

        folderMessagesResponse.setIdt(1);
        folderMessagesResponse.setSender("anasilva@empresa.com");
        folderMessagesResponse.setSubject("Reunião de Projeto - Atualização");
        folderMessagesResponse.setSand_at(LocalDateTime.now());
        folderMessagesResponse.setRead(false);

        return Arrays.asList(folderMessagesResponse);
    }


    private PageRequest getPageRequest() {
        return PageRequest.of(0, 10);
    }

    private List<MailboxFolderMessageResponse> getMailboxFolderMessageResponse() {

        MailboxFolderMessageResponse mailboxFolderMessageResponse = new MailboxFolderMessageResponse();

        mailboxFolderMessageResponse.setIdt(1);
        mailboxFolderMessageResponse.setSender("anasilva@empresa.com");
        mailboxFolderMessageResponse.setSubject("Reunião de Projeto - Atualização");
        mailboxFolderMessageResponse.setSend_at(LocalDateTime.now());
        mailboxFolderMessageResponse.setRead(false);
        mailboxFolderMessageResponse.setBody("Olá Primo");
        mailboxFolderMessageResponse.setRead(false);

        return Arrays.asList(mailboxFolderMessageResponse);

    }


}
