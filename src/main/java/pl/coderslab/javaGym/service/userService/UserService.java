package pl.coderslab.javaGym.service.userService;

import com.sun.mail.util.MailConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.javaGym.email.EmailSender;
import pl.coderslab.javaGym.entity.user.Role;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.enumClass.RoleEnum;
import pl.coderslab.javaGym.error.customException.*;
import pl.coderslab.javaGym.repository.RoleRepository;
import pl.coderslab.javaGym.repository.UserRepository;

import javax.mail.MessagingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements AbstractUserService<User> {

    private EmailSender emailSender;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       EmailSender emailSender) {
        this.emailSender = emailSender;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    //    method for admins
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public User save(User user, Boolean asAdmin) {
        if (user.getId() == null) {
            try {
                setUserProperties(user, asAdmin);
                emailSender.sendAccountActivationEmail(user);
                return userRepository.save(user);
            } catch (MailException e) {
                throw new EmailSendingException();
            } catch (DataIntegrityViolationException e) {
                throw new DomainObjectException();
            }
        } else {
            throw new NotAuthenticatedException();
        }
    }

    private void setUserProperties(User user, Boolean asAdmin) {
        Set<Role> roles = new HashSet<>();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(0);
        Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER.toString());
        roles.add(userRole);
        if (asAdmin) {
            Role adminRole = roleRepository.findByRole(RoleEnum.ROLE_ADMIN.toString());
            roles.add(adminRole);
        }
        user.setRoles(roles);
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<String> getAllUsersEmails() {
        return userRepository.getAllUsersEmails();
    }

    @Transactional
    public Boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getAuthenticatedUser(userId);
        if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        } else {
            throw new PasswordDoNotMatchException();
        }
    }

    private User getAuthenticatedUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            if (user.getEmail().equals(email)) {
                return user;
            } else {
                throw new NotAuthenticatedException();
            }
        } else {
            throw new ResourceNotFoundException();
        }
    }

    //    method for users
    public User getAuthenticatedUserById(Long userId) {
        User user = getAuthenticatedUser(userId);
        user.setPassword(null);
        return user;
    }

    @Transactional
    public User changeNewsletterConsent(Long userId, Boolean newsletter) {
        User user = getAuthenticatedUser(userId);
        user.setNewsletter(newsletter);
        return userRepository.save(user);
    }

    @Transactional
    public User changeFirstAndLastName(Long userId, String firstName, String lastName) {
        User user = getAuthenticatedUser(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return userRepository.save(user);
    }

    @Transactional
    public Boolean sendUserEmailChangeMessage(Long userId, String newEmail) {
        User user = getAuthenticatedUser(userId);
        if (user != null) {
            Boolean newEmailExistInDB = userRepository.existsByEmail(newEmail);
            if (!newEmailExistInDB) {
                try {
                    emailSender.sendChangeEmailMessage(user, newEmail);
                    return true;
                } catch (MailException e) {
                    throw new EmailSendingException();
                }
            } else {
                throw new DomainObjectException();
            }
        } else {
            throw new NotAuthenticatedException();
        }
    }
}