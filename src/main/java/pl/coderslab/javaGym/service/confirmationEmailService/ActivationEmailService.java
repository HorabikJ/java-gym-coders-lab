package pl.coderslab.javaGym.service.confirmationEmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.javaGym.entity.confirmationEmail.ActivationEmailDetails;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.error.customException.LinkExpiredException;
import pl.coderslab.javaGym.error.customException.ResourceNotFoundException;
import pl.coderslab.javaGym.repository.ActivationEmailRepository;
import pl.coderslab.javaGym.repository.UserRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class ActivationEmailService implements
        AbstractConfirmationEmailService<ActivationEmailDetails> {

    private final static ZoneId ZONE_POLAND = ZoneId.of("Poland");

    private UserRepository userRepository;
    private ActivationEmailRepository activationEmailRepository;

    @Autowired
    public ActivationEmailService(ActivationEmailRepository activationEmailRepository,
                                  UserRepository userRepository) {
        this.activationEmailRepository = activationEmailRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ActivationEmailDetails save(ActivationEmailDetails newEmailDetails) {
        Long userId = newEmailDetails.getUser().getId();
        ActivationEmailDetails oldEmailDetails = activationEmailRepository.findByUserId(userId);
        if (oldEmailDetails == null) {
            return activationEmailRepository.save(newEmailDetails);
        } else {
            newEmailDetails.setId(oldEmailDetails.getId());
            return activationEmailRepository.save(newEmailDetails);
        }
    }

    @Transactional
    public User activateUserAccount(String param) {
        ActivationEmailDetails emailDetails = activationEmailRepository.findByParam(param);
        if (emailDetails != null) {
            if (isLinkActive(emailDetails)) {
                activateUserAccount(emailDetails);
                return emailDetails.getUser();
            } else {
                throw new LinkExpiredException();
            }
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private Boolean isLinkActive(ActivationEmailDetails emailDetails) {
        Integer linkExpirationTimeInMinutes = emailDetails.getMinutesExpirationTime();
        Instant sendTime = emailDetails.getSendTime().toInstant();
        Instant nowTime = ZonedDateTime.now(ZONE_POLAND).toInstant();
        return nowTime.isBefore(sendTime.plusSeconds(60 * linkExpirationTimeInMinutes));
    }

    private void activateUserAccount(ActivationEmailDetails emailDetails) {
        User user = emailDetails.getUser();
        user.setActive(true);
        userRepository.save(user);
        emailDetails.setParam(UUID.randomUUID().toString());
        activationEmailRepository.save(emailDetails);
    }

}
