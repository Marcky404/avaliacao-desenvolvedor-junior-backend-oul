package email.email_management.service;

import email.email_management.exception.BusinessException;
import email.email_management.models.Folder;
import email.email_management.models.Mailbox;
import email.email_management.models.Message;
import email.email_management.models.request.FolderRequest;
import email.email_management.models.response.FolderResponse;
import email.email_management.repository.FolderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FolderServiceTest {

    public static final int FOLDER_IDT = 1;
    public static final String MAILBOX_NAME = "joaopereira@cliente.com";

    @InjectMocks
    FolderService service;

    @Mock
    FolderRepository repository;

    @Mock
    MailboxService mailboxService;


    @Test
    void createSuccessfully() {
        FolderRequest folderRequest = getFolderRequest();
        folderRequest.setName("teste");
        Folder folder = getFolder();

        when(mailboxService.findByName(anyString())).thenReturn(getMailbox());
        when(repository.save(any())).thenReturn(folder);
        service.create(MAILBOX_NAME, folderRequest);

        verify(repository, times(1)).save(any());

    }

    @Test
    void create_WhenMailboxAlreadyExists_ReturnsNotFound() {
        FolderRequest folderRequest = getFolderRequest();

        when(mailboxService.findByName(anyString())).thenReturn(getMailbox());

        var exception = assertThrows(BusinessException.class,
                () -> service.create(MAILBOX_NAME, folderRequest));

        assertThat(exception.getMessage()).isEqualTo("Pasta já existe");
    }

    @Test
    void findyByIdtSuccessfully() {
        Folder folder = getFolder();

        when(repository.findById(any())).thenReturn(Optional.of(folder));
        Folder response = service.findByIdt(FOLDER_IDT);

        assertThat(response).isNotNull();
        assertThat(response.getIdt()).isEqualTo(FOLDER_IDT);
        assertThat(response.getName()).isEqualTo(folder.getName());
    }

    @Test
    void findByIdt_WhenFolderAlreadyExists_ReturnsNotFound() {

        when(repository.findById(any())).thenReturn(Optional.empty());

        var exception = assertThrows(BusinessException.class,
                () -> service.findByIdt(FOLDER_IDT));

        assertThat(exception.getMessage()).isEqualTo("Pasta não existe");
    }

    @Test
    void findAllSuccessfully() {

        Mailbox mailbox = getMailbox();

        when(mailboxService.findByName(anyString())).thenReturn(mailbox);
        List<FolderResponse> response = service.findAll(MAILBOX_NAME);

        assertThat(response).isNotNull();
        assertThat(response.get(0).getName()).isEqualTo("INBOX");
        assertThat(response.get(1).getName()).isEqualTo("JUNK");
        assertThat(response.get(2).getName()).isEqualTo("SENT");
    }

    @Test
    void findAllPagedSuccessfully() {

        Mailbox mailbox = getMailbox();
        Page<Folder> folder = new PageImpl<>(getFolderList());
        PageRequest pageRequest = getPageRequest();

        when(mailboxService.findByName(anyString())).thenReturn(mailbox);
        when(repository.findByMailboxName(anyString(), any())).thenReturn(folder);
        Page<FolderResponse> response = service.findAll(MAILBOX_NAME, pageRequest);

        assertThat(response).isNotNull();
        assertThat(response.getContent().get(0).getName()).isEqualTo("JUNK");
    }


    private Folder getFolder() {

        return new Folder(FOLDER_IDT, "JUNK", getMailbox(), getMessageList());
    }


    private FolderResponse getFolderResponse() {
        return new FolderResponse(FOLDER_IDT, "JUNK");
    }

    private FolderRequest getFolderRequest() {
        return new FolderRequest("JUNK");
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

    private List<FolderResponse> getFolderResponseList() {
        return Arrays.asList(getFolderResponse());
    }

    private List<Folder> getFolderList() {
        return Arrays.asList(getFolder());
    }

    private PageRequest getPageRequest() {
        return PageRequest.of(0, 10);
    }


}
