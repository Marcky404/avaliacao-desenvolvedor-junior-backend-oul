package email.email_management.service;

import email.email_management.exception.BusinessException;
import email.email_management.models.Folder;
import email.email_management.models.Mailbox;
import email.email_management.models.request.MailboxRequest;
import email.email_management.models.response.FolderResponse;
import email.email_management.models.response.MailboxListResponse;
import email.email_management.models.response.MailboxResponse;
import email.email_management.repository.MailboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailboxService {

    private final MailboxRepository repository;

    public MailboxResponse create(MailboxRequest mailboxRequest) {

        Mailbox mailbox = MailboxRequest.toEntity(mailboxRequest);
        mailbox.setFolders(createFolderBase(mailbox));

        if (existsByName(mailbox.getName())) {
            throw new BusinessException(HttpStatus.CONFLICT, "Caixa já existe");
        }

        repository.save(mailbox);

        return MailboxResponse.toResponse(mailbox);
    }


    public Mailbox findByName(String mailbox) {
        return repository.findByName(mailbox).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Mailbox não existe"));
    }


    public List<MailboxListResponse> findAll() {
        List<Mailbox> mailboxes = repository.findAll();
        return mailboxes.stream().map(m -> MailboxListResponse.toResponse(m.getName())).toList();
    }

    public Page<MailboxListResponse> findAll(PageRequest pageRequest) {
        Page<Mailbox> mailboxes = repository.findAll(pageRequest);
        return mailboxes.map(m -> MailboxListResponse.toResponse(m.getName()));
    }

    public Boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    private List<Folder> createFolderBase(Mailbox mailbox) {

        Folder inbox = new Folder();
        inbox.setName("INBOX");
        inbox.setMailbox(mailbox);

        Folder junk = new Folder();
        junk.setName("JUNK");
        junk.setMailbox(mailbox);

        Folder sent = new Folder();
        sent.setName("SENT");
        sent.setMailbox(mailbox);


        return Arrays.asList(inbox, junk, sent);
    }
}
