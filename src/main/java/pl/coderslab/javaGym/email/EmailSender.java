package pl.coderslab.javaGym.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pl.coderslab.javaGym.entity.User;
import pl.coderslab.javaGym.enumClass.EmailTypeEnum;

@Component
public class EmailSender {

    private JavaMailSender javaMailSender;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public EmailSender(JavaMailSender javaMailSender, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.javaMailSender = javaMailSender;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void sendEmail(User user, EmailTypeEnum emailType) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        setEmailTitleAndContent(message, user, emailType);
        javaMailSender.send(message);
    }

    private void setEmailTitleAndContent(SimpleMailMessage message, User user,
                 EmailTypeEnum emailTypeEnum) {
        if (emailTypeEnum.equals(EmailTypeEnum.ACCOUNT_ACTIVATION_EMAIL))
            message.setSubject("JavaSpringGym account activation.");
            message.setText(this.activationEmailText +
                    "http://localhost:8080/confirm-account?param="
                    + bCryptPasswordEncoder.encode(user.getEmail()));
        if (emailTypeEnum.equals(EmailTypeEnum.WELCOME_EMAIL)) {
            message.setSubject(user.getFirstName() + " welcome in JavaSpringGym application!");
            message.setText(user.getFirstName() + ", we are very pleased that you joined us!");
        } else if (emailTypeEnum.equals(EmailTypeEnum.CLASS_CANCELED_EMAIL)) {
//            something
        } else if (emailTypeEnum.equals((EmailTypeEnum.CLASS_RESERVED_EMAIL))) {
//               something
        }
    }

    private String activationEmailText = "click in below link to activate your account\n";


}
