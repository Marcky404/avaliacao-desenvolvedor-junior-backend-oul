package email.email_management.controller.v1;

import email.email_management.models.request.*;
import email.email_management.models.response.*;
import email.email_management.service.FolderService;
import email.email_management.service.MailboxService;
import email.email_management.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/mailboxes")
@RequiredArgsConstructor
public class MailboxController {

    private final MailboxService service;
    private final FolderService folderService;
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MailboxResponse> create(@Valid @RequestBody MailboxRequest mailboxRequest) {
        MailboxResponse mailboxResponse = service.create(mailboxRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(mailboxResponse.getIdt())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{mailbox}/folders")
    public ResponseEntity<MailboxResponse> createFolder(@PathVariable("mailbox") String mailbox, @RequestBody @Valid FolderRequest folderRequest) {
        FolderResponse folderResponse = folderService.create(mailbox, folderRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(folderResponse.getIdt())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{mailbox}/send-messagefolders")
    public ResponseEntity<MessageResponse> createMessage(@PathVariable("mailbox") String mailbox, @RequestBody @Valid MessageRequest messageRequest) {
        MessageResponse messageResponse = messageService.create(mailbox, messageRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(messageResponse.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{mailbox}/receive-message")
    public ResponseEntity<MessageResponse> receiveMessage(@PathVariable("mailbox") String mailbox, @RequestBody @Valid ReceiveMessageRequest receiveMessageRequest) {
        MessageResponse messageResponse = messageService.receiveMessage(mailbox, receiveMessageRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(messageResponse.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("{mailbox}/folders/{folderIdt}/messages/{messageIdt}")
    public ResponseEntity<MessageResponse> update(@PathVariable("mailbox") String mailbox, @PathVariable("folderIdt") Integer folderIdt, @PathVariable("messageIdt") Integer messageIdt, @RequestBody ReadRequest readRequest) {
        MessageResponse messageResponse = messageService.update(mailbox, folderIdt, messageIdt, readRequest);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MailboxListResponse>> getAll() {

        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("{mailbox}/folders")
    public ResponseEntity<List<FolderResponse>> findAllFolders(@PathVariable("mailbox") String mailbox) {
        return ResponseEntity.ok(folderService.findAll(mailbox));
    }

    @GetMapping("{mailbox}/folders/{folderIdt}/messages")
    public ResponseEntity<List<FolderMessagesResponse>> findAllMessageForFolder(@PathVariable("mailbox") String mailbox, @PathVariable("folderIdt") Integer folderIdt) {
        return ResponseEntity.ok(messageService.findMessagesByMailboxNameAndFolderId(mailbox, folderIdt));
    }

    @GetMapping("{mailbox}/folders/{folderIdt}/messages/{messageIdt}")
    public ResponseEntity<List<MailboxFolderMessageResponse>> findMessagesByMailboxNameFolderIdAndMessageId(@PathVariable("mailbox") String mailbox, @PathVariable("folderIdt") Integer folderIdt, @PathVariable("messageIdt") Integer messageIdt) {
        return ResponseEntity.ok(messageService.findMessagesByMailboxNameAndFolderIdAndMessageId(mailbox, folderIdt, messageIdt));
    }


}
