package email.email_management.controller;


import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import email.email_management.controller.v2.MailboxControllerV2;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MailboxControllerV2Test {

    public static final String BASE_URL = "/api/v2/mailboxes";
    public static final String MAILBOX = "teste@domain.com";
    public static final int FOLDER_IDT = 1;
    public static final String PAGE = "0";
    public static final String SIZE = "10";

    @InjectMocks
    MailboxControllerV2 controller;

    @Mock
    MailboxService service;
    @Mock
    FolderService folderService;
    @Mock
    MessageService messageService;

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
    void getAllSuccessfully() throws Exception {

        mockMvc.perform(get(BASE_URL)
                        .queryParam("page", PAGE)
                        .queryParam("size", SIZE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void findAllFoldersSuccessfully() throws Exception {

        mockMvc.perform(get(BASE_URL + "/" + MAILBOX + "/folders")
                        .queryParam("page", PAGE)
                        .queryParam("size", SIZE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


    @Test
    void findAllMessageForFolderSuccessfully() throws Exception {

        mockMvc.perform(get(BASE_URL + "/" + MAILBOX + "/folders/" + 1 + "/messages")
                        .queryParam("page", PAGE)
                        .queryParam("size", SIZE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


}
