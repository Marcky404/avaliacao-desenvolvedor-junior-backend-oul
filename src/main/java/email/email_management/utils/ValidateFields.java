package email.email_management.utils;

import ch.qos.logback.core.util.StringUtil;
import email.email_management.exception.BusinessException;
import email.email_management.models.Folder;
import email.email_management.models.Mailbox;
import org.springframework.http.HttpStatus;


public class ValidateFields {

    public static final String MESSAGE = "Pasta Não encontrada";
    public static final String SENT = "SENT";

    public static String setDefaultSubjectIfNull(String subject) {
        if (StringUtil.isNullOrEmpty(subject)) {
            return "(sem assunto)";
        }
        return subject;
    }

    public static Folder findOrDefaultFolder(Mailbox mailbox, String name) {
        if (name.equals(SENT) || StringUtil.isNullOrEmpty(name)) {

            return mailbox.getFolders().stream()
                    .filter(f -> f.getName().equals(SENT))
                    .findFirst().orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, MESSAGE));

        }

        return mailbox.getFolders().stream()
                .filter(f -> f.getName().equals(name))
                .findFirst().orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, MESSAGE));

    }

}

