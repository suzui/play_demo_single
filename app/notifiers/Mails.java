package notifiers;

import models.person.Person;
import play.Play;
import play.mvc.Mailer;

public class Mails extends Mailer {
    
    private static final String SENDER_ADDRESS = Play.configuration.getProperty("mail.sender.address");
    private static final String BASE_URL = Play.configuration.getProperty("application.baseUrl");
    
    public static void mail(String mail) {
        setSubject("title");
        addRecipient(mail);
        setFrom(SENDER_ADDRESS);
        // 添加附件
        //EmailAttachment attachment = new EmailAttachment();
        //attachment.setDescription("A pdf document");
        //attachment.setPath(Play.getFile("rules.pdf").getPath());
        //addAttachment(attachment);
        send();
    }
    
    public static void captcha(String email, String code) {
        if (!Person.isEmailLegal(email)) {
            return;
        }
        setSubject("验证码");
        addRecipient(email);
        setFrom(SENDER_ADDRESS);
        send(code);
    }
}
