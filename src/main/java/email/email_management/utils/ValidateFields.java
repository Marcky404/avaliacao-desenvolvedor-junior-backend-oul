package email.email_management.utils;

import ch.qos.logback.core.util.StringUtil;
import email.email_management.exception.BusinessException;
import email.email_management.models.Folder;
import email.email_management.models.Mailbox;
import org.springframework.http.HttpStatus;


public class ValidateFields {

    public static String setDefaultSubjectIfNull(String subject) {
        if (StringUtil.isNullOrEmpty(subject)) {
            return "(sem assunto)";
        }
        return subject;
    }

}

