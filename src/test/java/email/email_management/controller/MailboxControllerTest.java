package email.email_management.controller;

import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import email.email_management.controller.v1.MailboxController;
import email.email_management.models.request.*;
import email.email_management.service.FolderService;
import email.email_management.service.MailboxService;
import email.email_management.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MailboxControllerTest {

    public static final String BASE_URL = "/api/v1/mailboxes";

    public static final String MAILBOX = "teste@domain.com";
    public static final int FOLDER_IDT = 1;
    public static final int MESSAGE_IDT = 1;
    @InjectMocks
    MailboxController controller;

    @Mock
    MessageService messageService;
    @Mock
    MailboxService service;
    @Mock
    FolderService folderService;

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();


        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void createSuccessfully() throws Exception {

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(getMailboxRequest()))
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void createFolderSuccessfully() throws Exception {
        FolderRequest folderRequest = getFolderRequest();

        mockMvc.perform(post(BASE_URL + "/" + MAILBOX + "/folders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(folderRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    void createMessageSuccessfully() throws Exception {

        MessageRequest messageRequest = getMessageRequest();

        mockMvc.perform(post(BASE_URL + "/" + MAILBOX + "/send-message")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(messageRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(messageService, times(1)).create((MAILBOX), (messageRequest));
    }

    @Test
    void receiveMessageSuccessfully() throws Exception {

        ReceiveMessageRequest receiveMessageRequest = getReceiveMessageRequest();

        mockMvc.perform(post(BASE_URL + "/" + MAILBOX + "/receive-message")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(receiveMessageRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    void updateMessageSuccessfully() throws Exception {
        ReadRequest readRequest = getReadRequest();

        mockMvc.perform(put(BASE_URL + "/" + MAILBOX + "/folders/" + FOLDER_IDT + "/messages/" + MESSAGE_IDT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(readRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    void findAllSuccessfully() throws Exception {

        mockMvc.perform(get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void findAllFoldersSuccessfully() throws Exception {

        mockMvc.perform(get(BASE_URL + "/" + MAILBOX + "/folders")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findAllMessagesForFolderSuccessfully() throws Exception {

        mockMvc.perform(get(BASE_URL + "/" + MAILBOX + "/folders/" + FOLDER_IDT + "/messages")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void findMessagesByMailboxNameFolderIdAndMessageIdSuccessfully() throws Exception {

        mockMvc.perform(get(BASE_URL + "/" + MAILBOX + "/folders/" + FOLDER_IDT + "/messages/" + MESSAGE_IDT)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


    private ReadRequest getReadRequest() {
        return new ReadRequest(true);
    }


    private ReceiveMessageRequest getReceiveMessageRequest() {
        return new ReceiveMessageRequest("teste1@dominio.com"
                , "Assunto de teste", "Corpo do e-mail de teste@domain.com"
                , "JUNK");
    }


    private MessageRequest getMessageRequest() {
        return new MessageRequest("teste1@dominio.com", "Assunto de teste", "Corpo do e-mail de teste@domain.com");
    }

    private FolderRequest getFolderRequest() {
        return new FolderRequest("arquivos-de-testes");
    }


    private MailboxRequest getMailboxRequest() {
        return new MailboxRequest("teste@domain.com");
    }


}
