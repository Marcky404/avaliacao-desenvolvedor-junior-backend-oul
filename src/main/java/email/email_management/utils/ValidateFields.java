package email.email_management.utils;

import ch.qos.logback.core.util.StringUtil;


public class ValidateFields {

    public static String setDefaultSubjectIfNull(String subject) {
        if (StringUtil.isNullOrEmpty(subject)) {
            return "(sem assunto)";
        }
        return subject;
    }

}

