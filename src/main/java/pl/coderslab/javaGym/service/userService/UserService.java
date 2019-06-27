package pl.coderslab.javaGym.service.userService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.javaGym.emailSender.EmailSender;
import pl.coderslab.javaGym.entity.user.Role;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.enumClass.RoleEnum;
import pl.coderslab.javaGym.error.customException.*;
import pl.coderslab.javaGym.model.Email;
import pl.coderslab.javaGym.repository.RoleRepository;
import pl.coderslab.javaGym.repository.UserRepository;

import java.util.Arrays;
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
    public User findById(Long userId) {
        return getUserByIdFromDB(userId);
    }

    private User getUserByIdFromDB(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return user;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Transactional
    public Boolean deleteUserById(Long userId) {
        User user = getUserByIdFromDB(userId);
        if (!isUserAnAdmin(user)) {
            userRepository.delete(user);
            return true;
        } else {
            throw new UserUnauthorizedException();
        }
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
            throw new UserUnauthorizedException();
        }
    }

    private void setUserProperties(User user, Boolean asAdmin) {
        Set<Role> roles = new HashSet<>();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(false);
        Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER.toString());
        roles.add(userRole);
        if (asAdmin) {
            Role adminRole = roleRepository.findByRole(RoleEnum.ROLE_ADMIN.toString());
            roles.add(adminRole);
        }
        user.setRoles(roles);
    }

    // method for super admin only
    @Transactional
    public Boolean deleteAnyUserById(Long userId) {
        User user = getUserByIdFromDB(userId);
        if (!isUserASuperAdmin(user)) {
            userRepository.delete(user);
            return true;
        } else {
            throw new UserUnauthorizedException();
        }
    }

    private Boolean isUserASuperAdmin(User user) {
        return user.getRoles().contains(roleRepository.findByRole(RoleEnum.ROLE_SUPER.toString()));
    }

    //    method to check if it will be used later
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //    TODO
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
        User user = getUserByIdFromDB(userId);
        if (user.getEmail().equals(email)) {
            return user;
        } else {
            throw new UserUnauthorizedException();
        }
    }

    //    method for users only
    public User getAuthenticatedUserById(Long userId) {
        return getAuthenticatedUser(userId);
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
            throw new UserUnauthorizedException();
        }
    }

    @Transactional
    public Boolean resetUserPassword(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user != null) {
            try {
                emailSender.sendResetPasswordEmail(user);
                return true;
            } catch (MailException e) {
                throw new EmailSendingException();
            }
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<User> showAllUsersWithUserRoleOnly() {
        return userRepository.findAllByRolesIsNotContaining(getSetWithAdminRoleOnly());
    }

    public List<User> showAllAdmins() {
        return userRepository.findAllByRolesIsContaining(getSetWithAdminRoleOnly());
    }

    public List<User> searchForUsersByEmail(String email) {
        return userRepository.findAllByRolesIsNotContainingAndEmailIsContaining
                (getSetWithAdminRoleOnly(), email);
    }

    public List<User> searchForAdminsByEmail(String email) {
        return userRepository.findAllByRolesIsContainingAndEmailIsContaining
                (getSetWithAdminRoleOnly(), email);
    }

    private Set<Role> getSetWithAdminRoleOnly() {
        Role role = roleRepository.findByRole(RoleEnum.ROLE_ADMIN.toString());
        return new HashSet<>(Arrays.asList(role));
    }

    @Transactional
    public User changeUserActiveAccount(Long userId, Boolean active) {
        User user = getUserByIdFromDB(userId);
        if (!isUserAnAdmin(user)) {
            user.setActive(active);
            return userRepository.save(user);
        } else {
            throw new UserUnauthorizedException();
        }
    }

    private Boolean isUserAnAdmin(User user) {
        return user.getRoles().contains(roleRepository.findByRole(RoleEnum.ROLE_ADMIN.toString()));
    }

    @Transactional
    public Boolean sendEmailToUser(Long userId, Email email) {
        User user = getUserByIdFromDB(userId);
        try {
            emailSender.sendEmailToUser(user, email);
            return true;
        } catch (MailException e) {
            throw new EmailSendingException();
        }
    }

    @Transactional
    public Boolean sendActivationEmail(Long userId) {
        try {
            User user = getUserByIdFromDB(userId);
            emailSender.sendAccountActivationEmail(user);
            return true;
        } catch (MailException e) {
            throw new EmailSendingException();
        }
    }

    @Transactional
    public Boolean sendNewsletter(Email newsletter) {
        try {
            List<User> newsletterUsers = userRepository.findAllByNewsletterIsTrue();
            emailSender.sendNewsletter(newsletter, newsletterUsers);
            return true;
        } catch (MailException e) {
            throw new EmailSendingException();
        }
    }

    @Transactional
    public User changeAnyUserActiveAccount(Long userId, Boolean active) {
        User user = getUserByIdFromDB(userId);
        if (!isUserASuperAdmin(user)) {
            user.setActive(active);
            return userRepository.save(user);
        } else {
            throw new UserUnauthorizedException();
        }
    }
}
