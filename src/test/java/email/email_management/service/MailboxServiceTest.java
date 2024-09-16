package email.email_management.service;

import email.email_management.exception.BusinessException;
import email.email_management.models.Folder;
import email.email_management.models.Mailbox;
import email.email_management.models.request.MailboxRequest;
import email.email_management.models.response.MailboxListResponse;
import email.email_management.repository.MailboxRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MailboxServiceTest {
    public static final int MAILBOX_IDT = 1;
    public static final String NAME = "teste@domain.com";
    @InjectMocks
    MailboxService service;
    @Mock
    MailboxRepository repository;


    @Test
    void createSuccessfully() {
        MailboxRequest mailboxRequest = getMailboxRequest();
        Mailbox mailbox = getMailbox();

        when(repository.save(any())).thenReturn(mailbox);
        service.create(mailboxRequest);

        verify(repository, times(1)).save(any());

    }


    @Test
    void create_WhenMailboxAlreadyExists_ReturnsConflict() {

        MailboxRequest mailboxRequest = getMailboxRequest();

        when(repository.existsByName(anyString())).thenReturn(true);

        var exception = assertThrows(BusinessException.class,
                () -> service.create(mailboxRequest));

        assertThat(exception.getMessage()).isEqualTo("Caixa já existe");
    }

    @Test
    void findByNameSuccessfully() {

        Optional<Mailbox> mailboxOptional = Optional.of(getMailbox());

        when(repository.findByName(anyString())).thenReturn(mailboxOptional);
        Mailbox mailbox = service.findByName(NAME);

        assertThat(mailbox.getIdt()).isEqualTo(MAILBOX_IDT);
        assertThat(mailbox.getName()).isEqualTo(NAME);
        assertThat(mailbox.getFolders()).isEqualTo(createFolderBase());
    }

    @Test
    void findByName_WhenMailboxAlreadyExists_ReturnsNotFound() {

        when(repository.findByName(anyString())).thenReturn(Optional.empty());

        var exception = assertThrows(BusinessException.class,
                () -> service.findByName(NAME));

        assertThat(exception.getMessage()).isEqualTo("Mailbox não existe");
    }

    @Test
    void findAllSuccessfully() {
        List<Mailbox> mailboxes = getMailboxeList();

        when(repository.findAll()).thenReturn(mailboxes);
        List<MailboxListResponse> response = service.findAll();

        assertThat(response).isNotNull();
        assertThat(response.get(0).getName()).isEqualTo(NAME);

    }

    @Test
    void findAllPagedSuccessfully() {

        List<Mailbox> mailboxes = getMailboxeList();
        PageRequest pageRequest = getPageRequest();
        Page<Mailbox> mailboxPage = new PageImpl<>(mailboxes);

        when(repository.findAll(pageRequest)).thenReturn(mailboxPage);
        Page<MailboxListResponse> response = service.findAll(pageRequest);

        assertThat(response).isNotNull();
        assertThat(response.getContent().get(0).getName()).isEqualTo(NAME);

    }


    private MailboxRequest getMailboxRequest() {
        return new MailboxRequest("teste@domain.com");
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

    private Mailbox getMailbox() {
        return new Mailbox(MAILBOX_IDT, "teste@domain.com", createFolderBase());
    }

    private List<Mailbox> getMailboxeList() {

        Mailbox mailbox = getMailbox();

        return Arrays.asList(mailbox);
    }

    private PageRequest getPageRequest() {
        return PageRequest.of(0, 10);
    }


}
