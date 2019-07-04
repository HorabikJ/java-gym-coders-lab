package pl.coderslab.javaGym.emailSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import pl.coderslab.javaGym.dataTransferObject.EmailDto;
import pl.coderslab.javaGym.entity.Person;
import pl.coderslab.javaGym.entity.confirmationEmail.ActivationEmailDetails;
import pl.coderslab.javaGym.entity.confirmationEmail.ChangeEmailDetails;
import pl.coderslab.javaGym.entity.confirmationEmail.ResetPasswordEmailDetails;
import pl.coderslab.javaGym.entity.data.Reservation;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.error.customException.EmailSendingException;
import pl.coderslab.javaGym.service.confirmationEmailService.ActivationEmailService;
import pl.coderslab.javaGym.service.confirmationEmailService.ChangeEmailDetailsService;
import pl.coderslab.javaGym.service.confirmationEmailService.ResetPasswordEmailService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class EmailSender {

    private JavaMailSender javaMailSender;
    private ActivationEmailService activationEmailService;
    private ChangeEmailDetailsService changeEmailDetailsService;
    private ResetPasswordEmailService resetPasswordEmailService;

    @Autowired
    public EmailSender(JavaMailSender javaMailSender,
                       ChangeEmailDetailsService changeEmailDetailsService,
                       ActivationEmailService activationEmailService,
                       ResetPasswordEmailService resetPasswordEmailService) {
        this.javaMailSender = javaMailSender;
        this.changeEmailDetailsService = changeEmailDetailsService;
        this.activationEmailService = activationEmailService;
        this.resetPasswordEmailService = resetPasswordEmailService;
    }

    @Value("${application.globalvalues.link-expiration-time-minutes}")
    private Integer LINK_EXPIRATION_TIME;

    @Value("${application.globalvalues.confirm-account-url}")
    private String CONFIRM_ACCOUNT_URL;
    @Value("${application.globalvalues.email.subject.confirm-account}")
    private String CONFIRM_ACCOUNT_SUBJECT;
    @Value("${application.globalvalues.email.text.confirm-account}")
    private String CONFIRM_ACCOUNT_TEXT;

    @Value("${application.globalvalues.change-email-ulr}")
    private String CHANGE_EMAIL_URL;
    @Value("${application.globalvalues.email.subject.change-email}")
    private String CHANGE_EMAIL_SUBJECT;
    @Value("${application.globalvalues.email.text.change-email}")
    private String CHANGE_EMAIL_TEXT;

    @Value("${application.globalvalues.reset-password-url}")
    private String RESET_PASSWORD_URL;
    @Value("${application.globalvalues.email.subject.reset-password}")
    private String RESET_PASSWORD_SUBJECT;
    @Value("${application.globalvalues.email.text.reset-password}")
    private String RESET_PASSWORD_TEXT;

    @Value("${application.globalvalues.email.subject.welcome-email}")
    private String WELCOME_EMAIL_SUBJECT;
    @Value("${application.globalvalues.email.text.welcome-email}")
    private String WELCOME_EMAIL_TEXT;

    public void sendUserWelcomeEmail(User user) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject(user.getFirstName() + WELCOME_EMAIL_SUBJECT);
            message.setText(user.getFirstName() + WELCOME_EMAIL_TEXT);
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new EmailSendingException();
        }
    }

    public void sendAccountActivationEmail(User user) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            StringBuffer messageText = new StringBuffer();

            String param = UUID.randomUUID().toString();
            messageText.append(CONFIRM_ACCOUNT_TEXT)
                    .append(CONFIRM_ACCOUNT_URL)
                    .append(param);

            message.setTo(user.getEmail());
            message.setSubject(CONFIRM_ACCOUNT_SUBJECT);
            message.setText(messageText.toString());

            ActivationEmailDetails activationEmailDetails =
                    new ActivationEmailDetails
                            (user, param, LocalDateTime.now(),
                                    LINK_EXPIRATION_TIME);

            activationEmailService.save(activationEmailDetails);
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new EmailSendingException();
        }
    }

    public void sendChangeEmailMessage(User user, String newEmail) {
        try {
            StringBuffer messageText = new StringBuffer();
            SimpleMailMessage message = new SimpleMailMessage();

            String param = UUID.randomUUID().toString();
            messageText.append(CHANGE_EMAIL_TEXT)
                    .append(CHANGE_EMAIL_URL)
                    .append(param);

            message.setTo(newEmail);
            message.setSubject(CHANGE_EMAIL_SUBJECT);
            message.setText(messageText.toString());
            ChangeEmailDetails emailDetails =
                    new ChangeEmailDetails(user, param, LocalDateTime.now(),
                            newEmail, LINK_EXPIRATION_TIME);

            changeEmailDetailsService.save(emailDetails);
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new EmailSendingException();
        }
    }

    public void sendResetPasswordEmail(User user) {
        try {
            StringBuffer messageText = new StringBuffer();
            SimpleMailMessage message = new SimpleMailMessage();

            String param = UUID.randomUUID().toString();
            messageText.append(RESET_PASSWORD_TEXT)
                    .append(RESET_PASSWORD_URL)
                    .append(param);

            message.setTo(user.getEmail());
            message.setSubject(RESET_PASSWORD_SUBJECT);
            message.setText(messageText.toString());
            ResetPasswordEmailDetails resetPasswordEmailDetails =
                    new ResetPasswordEmailDetails
                            (user, param, LocalDateTime.now(), LINK_EXPIRATION_TIME);

            resetPasswordEmailService.save(resetPasswordEmailDetails);
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new EmailSendingException();
        }
    }

    public void sendEmailToPerson(Person person, EmailDto emailData) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(person.getEmail());
            message.setSubject(emailData.getTitle());
            message.setText(emailData.getText());
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new EmailSendingException();
        }
    }

    public void sendEmailToUsers(EmailDto email, List<User> users) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            for (User user : users) {
                message.setTo(user.getEmail());
                message.setSubject(email.getTitle());
                message.setText(email.getText());
                javaMailSender.send(message);
            }
        } catch (MailException e) {
            throw new EmailSendingException();
        }
    }

    public void sendClassReservationEmail(Reservation reservation, boolean onTrainingList) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            StringBuffer messageSubject = new StringBuffer();
            messageSubject.append("JavaGymSpring - ")
                    .append(reservation.getTrainingClass().getTrainingType().getName())
                    .append(" - class reservation.");

            message.setTo(reservation.getUser().getEmail());
            message.setSubject(messageSubject.toString());
            message.setText(getReservationMessageText(reservation, onTrainingList));
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new EmailSendingException();
        }
    }

    private String getReservationMessageText(Reservation reservation, boolean onTrainingList) {
        String listType = onTrainingList ? "reservation list" : "awaiting list";
        String goodBye = onTrainingList ? "See you on the training!" :
                "We will let you know when you jump on the training list!";
        StringBuffer messageText = new StringBuffer();
        return messageText.append("Welcome ")
                .append(reservation.getUser().getFirstName()).append("!")
                .append("\nYou are on the ").append(listType).append(" for the below class:")
                .append("\nClass details:")
                .append("\nTrainer: ").append(reservation.getTrainingClass().getInstructor().getFullName())
                .append("\nTraining: ").append(reservation.getTrainingClass().getTrainingType().getName())
                .append("\nClass start date: ").append(reservation.getTrainingClass().getStartDate())
                .append(", class duration: ").append(reservation.getTrainingClass().getDurationInMinutes())
                .append(" minutes.\n")
                .append(goodBye)
                .toString();
    }

    public void sendClassCancellationEmail(Reservation reservation) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            StringBuffer messageSubject = new StringBuffer();
            messageSubject.append("JavaGymSpring - ")
                    .append(reservation.getTrainingClass().getTrainingType().getName())
                    .append(" - cancellation confirmation.");

            message.setTo(reservation.getUser().getEmail());
            message.setSubject(messageSubject.toString());
            message.setText(getCancellationMessageText(reservation));
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new EmailSendingException();
        }
    }

    private String getCancellationMessageText(Reservation reservation) {
        StringBuffer messageText = new StringBuffer();
        return messageText.append("Welcome ")
                .append(reservation.getUser().getFirstName()).append("!")
                .append("\nYou have just resigned from the below class:")
                .append("\nClass details:")
                .append("\nTrainer: ").append(reservation.getTrainingClass().getInstructor().getFullName())
                .append("\nTraining: ").append(reservation.getTrainingClass().getTrainingType().getName())
                .append("\nClass start date: ").append(reservation.getTrainingClass().getStartDate())
                .append(", class duration: ").append(reservation.getTrainingClass().getDurationInMinutes())
                .append(" minutes.\n")
                .append("We hope that you will visit us again soon!")
                .toString();
    }

    public void sendJumpToTrainingEmail(Reservation reservation) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            StringBuffer messageSubject = new StringBuffer();
            messageSubject.append("JavaGymSpring - ")
                    .append(reservation.getTrainingClass().getTrainingType().getName())
                    .append(" - you got the reservation!");

            message.setTo(reservation.getUser().getEmail());
            message.setSubject(messageSubject.toString());
            message.setText(getJumpToTrainingMessageText(reservation));
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new EmailSendingException();
        }
    }

    private String getJumpToTrainingMessageText(Reservation reservation) {
        StringBuffer messageText = new StringBuffer();
        return messageText.append("Welcome ")
                .append(reservation.getUser().getFirstName()).append("!")
                .append("\nYou have just jumped to reservation list for the below class:")
                .append("\nClass details:")
                .append("\nTrainer: ").append(reservation.getTrainingClass().getInstructor().getFullName())
                .append("\nTraining: ").append(reservation.getTrainingClass().getTrainingType().getName())
                .append("\nClass start date: ").append(reservation.getTrainingClass().getStartDate())
                .append(", class duration: ").append(reservation.getTrainingClass().getDurationInMinutes())
                .append(" minutes.\n")
                .append("See you on the training!")
                .toString();
    }
}