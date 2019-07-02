package pl.coderslab.javaGym.service.confirmationEmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.javaGym.entity.confirmationEmail.ChangeEmailDetails;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.error.customException.LinkExpiredException;
import pl.coderslab.javaGym.error.customException.ResourceNotFoundException;
import pl.coderslab.javaGym.error.customException.UniqueDBFieldException;
import pl.coderslab.javaGym.repository.ChangeEmailDetailsRepository;
import pl.coderslab.javaGym.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class ChangeEmailDetailsService  {

    private UserRepository userRepository;
    private ChangeEmailDetailsRepository changeEmailDetailsRepository;

    @Autowired
    public ChangeEmailDetailsService(ChangeEmailDetailsRepository changeEmailDetailsRepository,
                                    UserRepository userRepository) {
        this.changeEmailDetailsRepository = changeEmailDetailsRepository;
        this.userRepository = userRepository;
    }

    public ChangeEmailDetails save(ChangeEmailDetails newChangeEmailDetails) {
        Long userId = newChangeEmailDetails.getUser().getId();
        ChangeEmailDetails oldChangeEmailDetails = changeEmailDetailsRepository.findByUserId(userId);
        if (oldChangeEmailDetails == null) {
            return changeEmailDetailsRepository.save(newChangeEmailDetails);
        } else {
            newChangeEmailDetails.setId(oldChangeEmailDetails.getId());
            return changeEmailDetailsRepository.save(newChangeEmailDetails);
        }
    }

    @Transactional
    public Boolean confirmEmailChange(String param) {
        ChangeEmailDetails emailDetails = changeEmailDetailsRepository.findByParam(param);
        if (emailDetails != null) {
            Boolean isNewEmailAlreadyInDB = userRepository.existsByEmailIgnoreCase(emailDetails.getNewEmail());
            if (!isNewEmailAlreadyInDB) {
                if (isLinkActive(emailDetails)) {
                    changeUserEmail(emailDetails);
                    return true;
                } else {
                    throw new LinkExpiredException();
                }
            } else {
                throw new UniqueDBFieldException();
            }
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private void changeUserEmail(ChangeEmailDetails emailDetails) {
        User user = emailDetails.getUser();
        user.setEmail(emailDetails.getNewEmail());
        userRepository.save(user);
        emailDetails.setParam(UUID.randomUUID().toString());
        changeEmailDetailsRepository.save(emailDetails);
    }

    private Boolean isLinkActive(ChangeEmailDetails emailDetails) {
        Integer linkExpirationTimeInMinutes = emailDetails.getMinutesExpirationTime();
        LocalDateTime sendTime = emailDetails.getSendTime();
        LocalDateTime nowTime = LocalDateTime.now();
        return nowTime.isBefore(sendTime.plusMinutes(linkExpirationTimeInMinutes));
    }

}
