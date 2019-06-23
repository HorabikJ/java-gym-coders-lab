package pl.coderslab.javaGym.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pl.coderslab.javaGym.entity.Person;
import pl.coderslab.javaGym.entity.User;
import pl.coderslab.javaGym.enumClass.EmailTypeEnum;

//TODO Optimize email sending mechanizm
@Component
public class EmailSender {

    private JavaMailSender javaMailSender;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public EmailSender(JavaMailSender javaMailSender, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.javaMailSender = javaMailSender;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void sendEmail(Person person, EmailTypeEnum emailType) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(person.getEmail());
        setEmailTitleAndContent(message, person, emailType);
        javaMailSender.send(message);
    }

    private void setEmailTitleAndContent(SimpleMailMessage message, Person person,
                 EmailTypeEnum emailTypeEnum) {
        if (emailTypeEnum.equals(EmailTypeEnum.ACCOUNT_ACTIVATION_EMAIL))
            message.setSubject("JavaSpringGym account activation.");
            message.setText(this.activationEmailText +
                    "http://localhost:8080/confirm-account?param="
                    + bCryptPasswordEncoder.encode(person.getEmail()));
        if (emailTypeEnum.equals(EmailTypeEnum.WELCOME_EMAIL)) {
            message.setSubject(person.getFirstName() + " welcome in JavaSpringGym application!");
            message.setText(person.getFirstName() + ", we are very pleased that you joined us!");
        } else if (emailTypeEnum.equals(EmailTypeEnum.CLASS_CANCELED_EMAIL)) {
//            something
        } else if (emailTypeEnum.equals((EmailTypeEnum.CLASS_RESERVED_EMAIL))) {
//               something
        }
    }

    private String activationEmailText = "click in below link to activate your account\n";


}
