package email.email_management.service;

import email.email_management.exception.BusinessException;
import email.email_management.models.Folder;
import email.email_management.models.Mailbox;
import email.email_management.models.request.FolderRequest;
import email.email_management.models.response.FolderResponse;
import email.email_management.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository repository;
    private final MailboxService mailboxService;

    public FolderResponse create(String mailbox, FolderRequest folderRequest) {
        Folder folder = FolderRequest.toEntity(folderRequest);
        Mailbox mailboxEntity = mailboxService.findByName(mailbox);

        boolean hesFolder = mailboxEntity.getFolders().stream().anyMatch(f -> f.getName().equals(folder.getName()));

        if (hesFolder) {
            throw new BusinessException(HttpStatus.CONFLICT, "Pasta já existe");
        }

        folder.setMailbox(mailboxEntity);

        repository.save(folder);
        return FolderResponse.toResponse(folder);
    }

    public Folder findByIdt(Integer folderIdt) {
        return repository.findById(folderIdt).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Pasta não existe"));
    }

    public List<FolderResponse> findAll(String mailbox) {
        Mailbox mailboxEntity = mailboxService.findByName(mailbox);

        return mailboxEntity.getFolders().stream().map(FolderResponse::toResponse).toList();
    }


    public Page<FolderResponse> findAll(String mailbox, PageRequest pageRequest) {
        Page<Folder> folderPage = repository.findByMailboxName(mailbox, pageRequest);


        return folderPage.map(FolderResponse::toResponse);
    }
}
