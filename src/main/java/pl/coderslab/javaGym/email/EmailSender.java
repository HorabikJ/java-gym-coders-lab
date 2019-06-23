package pl.coderslab.javaGym.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pl.coderslab.javaGym.entity.Person;

@Component
public class EmailSender {

    private final String changeEmailSubject = "JavaSpringGym change of email.";
    private final String changeEmailText = "Please click below link to confirm change of your email:\n";

    private final String confirmAccountSubject = "JavaGymSpring account activation email.";
    private final String confirmAccountText = "Please click below link to activate your account:\n" +
                                              "***DO NOT MANIPULATE IT!\n";

    private final String welcomeEmailSubject = " welcome in JavaSpringGym application!";
    private final String welcomeEmailText = " we are very pleased that you joined us!";

    private JavaMailSender javaMailSender;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public EmailSender(JavaMailSender javaMailSender, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.javaMailSender = javaMailSender;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void sendUserWelcomeEmail(Person person) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(person.getEmail());
        message.setSubject(person.getFirstName() + welcomeEmailSubject);
        message.setText(person.getFirstName() + welcomeEmailText);
        javaMailSender.send(message);
    }

    public void sendAccountActivationEmail(Person person) {
        SimpleMailMessage message = new SimpleMailMessage();
        StringBuffer messageText = new StringBuffer();
        String hashedEmail = bCryptPasswordEncoder.encode(person.getEmail());

        messageText.append(confirmAccountText)
                .append("http://localhost:8080/confirm-account?param=")
                .append(hashedEmail);

        message.setTo(person.getEmail());
        message.setSubject(confirmAccountSubject);
        message.setText(messageText.toString());
        javaMailSender.send(message);
    }

    public void sendChangeEmailMessage(Person person, String newEmail) {
        StringBuffer messageText = new StringBuffer();
        SimpleMailMessage message = new SimpleMailMessage();
        String hashedCurrentEmail = bCryptPasswordEncoder.encode(person.getEmail());

        messageText.append(changeEmailText)
                .append("http://localhost:8080/change-email?param=")
                .append(hashedCurrentEmail)
                .append("&newEmail=")
                .append(newEmail);

        message.setTo(newEmail);
        message.setSubject(changeEmailSubject);
        message.setText(messageText.toString());
        javaMailSender.send(message);
    }
}
