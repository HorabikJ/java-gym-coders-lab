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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class ActivationEmailService implements
        AbstractConfirmationEmailService<ActivationEmailDetails> {

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
        LocalDateTime sendTime = emailDetails.getSendTime();
        LocalDateTime nowTime = LocalDateTime.now();
        return nowTime.isBefore(sendTime.plusMinutes(linkExpirationTimeInMinutes));
    }

    private void activateUserAccount(ActivationEmailDetails emailDetails) {
        User user = emailDetails.getUser();
        user.setActive(true);
        userRepository.save(user);
        emailDetails.setParam(UUID.randomUUID().toString());
        activationEmailRepository.save(emailDetails);
    }

}
