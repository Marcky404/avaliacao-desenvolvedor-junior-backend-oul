package email.email_management.controller.v2;

import email.email_management.models.response.*;
import email.email_management.service.FolderService;
import email.email_management.service.MailboxService;
import email.email_management.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v2/mailboxes")
@RequiredArgsConstructor
public class MailboxControllerV2 {

    private final MailboxService service;
    private final FolderService folderService;
    private final MessageService messageService;



    @GetMapping
    public ResponseEntity<Page<MailboxListResponse>> getAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                            @RequestParam(value = "size", defaultValue = "10") Integer size){

        PageRequest pageRequest = PageRequest.of(page, size);

        return ResponseEntity.ok(service.findAll(pageRequest));
    }

    @GetMapping("{mailbox}/folders")
    public ResponseEntity<Page<FolderResponse>> findAllFolders(@PathVariable("mailbox") String mailbox,
                                                               @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                               @RequestParam(value = "size", defaultValue = "10") Integer size) {

        PageRequest pageRequest = PageRequest.of(page, size);

        return ResponseEntity.ok(folderService.findAll(mailbox, pageRequest));
    }

    @GetMapping("{mailbox}/folders/{folderIdt}/messages")
    public ResponseEntity<Page<FolderMessagesResponse>> findAllMessageForFolder(@PathVariable("mailbox") String mailbox,
                                                                                @PathVariable("folderIdt") Integer folderIdt,
                                                                                @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                                @RequestParam(value = "size", defaultValue = "10") Integer size) {

        PageRequest pageRequest = PageRequest.of(page, size);

        return ResponseEntity.ok(messageService.findMessagesByMailboxNameAndFolderId(mailbox, folderIdt, pageRequest));
    }

}
