package pl.coderslab.javaGym.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import pl.coderslab.javaGym.entity.User;
import pl.coderslab.javaGym.enumClass.EmailTypeEnum;

@Component
public class EmailSender {

    private JavaMailSender javaMailSender;

    @Autowired
    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(User user, EmailTypeEnum emailType) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
    }

    private void setEmailTitleAndContent(User user,
                 EmailTypeEnum emailTypeEnum, SimpleMailMessage message) {
        if (emailTypeEnum.equals(EmailTypeEnum.WELCOME_EMAIL)) {
            message.setSubject(user.getFirstName() + " welcome in JavaSpringGym application!");
            message.setText(user.getFirstName() + ", we are very pleased that you joined us!");
        } else if (emailTypeEnum.equals(EmailTypeEnum.CLASS_CANCELED_EMAIL)) {
//            something
        } else if (emailTypeEnum.equals((EmailTypeEnum.CLASS_CANCELED_EMAIL))) {
//               something
        }
    }

}
