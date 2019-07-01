package pl.coderslab.javaGym.service.userService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.javaGym.emailSender.EmailSender;
import pl.coderslab.javaGym.entity.data.Reservation;
import pl.coderslab.javaGym.entity.data.TrainingClass;
import pl.coderslab.javaGym.entity.user.Role;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.enumClass.RoleEnum;
import pl.coderslab.javaGym.error.customException.*;
import pl.coderslab.javaGym.dataTransferObject.EmailDto;
import pl.coderslab.javaGym.repository.ReservationRepository;
import pl.coderslab.javaGym.repository.RoleRepository;
import pl.coderslab.javaGym.repository.TrainingClassRepository;
import pl.coderslab.javaGym.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    private static final Integer SHOW_CLASSES_DAYS_NUMBER = 14;

    private EmailSender emailSender;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private TrainingClassRepository trainingClassRepository;
    private ReservationRepository reservationRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       TrainingClassRepository trainingClassRepository,
                       ReservationRepository reservationRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       EmailSender emailSender) {
        this.emailSender = emailSender;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.reservationRepository = reservationRepository;
        this.trainingClassRepository = trainingClassRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findById(Long userId) {
        return getUserById(userId);
    }

    private User getUserById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return user;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Transactional
    public Boolean deleteUserById(Long userId) {
        User user = getUserById(userId);
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
            if (!userRepository.existsByEmailIgnoreCase(user.getEmail())) {
                setUserProperties(user, asAdmin);
                emailSender.sendAccountActivationEmail(user);
                return userRepository.save(user);
            } else {
                throw new UniqueDBFieldException();
            }
        } else {
            throw new ActionNotAllowedException();
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

    @Transactional
    public Boolean deleteAnyUserById(Long userId) {
        User user = getUserById(userId);
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

    //TODO to check if needed
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public Boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getAuthenticatedUserById(userId);
        if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        } else {
            throw new PasswordDoNotMatchException();
        }
    }

    public User getAuthenticatedUserById(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = getUserById(userId);
        if (user.getEmail().equals(email)) {
            return user;
        } else {
            throw new UserUnauthorizedException();
        }
    }

    @Transactional
    public User changeNewsletterConsent(Long userId, Boolean newsletter) {
        User user = getAuthenticatedUserById(userId);
        user.setNewsletter(newsletter);
        return userRepository.save(user);
    }

    @Transactional
    public User changeFirstAndLastName(Long userId, String firstName, String lastName) {
        User user = getAuthenticatedUserById(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return userRepository.save(user);
    }

    @Transactional
    public Boolean sendUserEmailChangeMessage(Long userId, String newEmail) {
        User user = getAuthenticatedUserById(userId);
        Boolean newEmailExistInDB = userRepository.existsByEmailIgnoreCase(newEmail);
        if (!newEmailExistInDB) {
            emailSender.sendChangeEmailMessage(user, newEmail);
            return true;
        } else {
            throw new UniqueDBFieldException();
        }
    }

    @Transactional
    public Boolean resetUserPassword(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user != null) {
            emailSender.sendResetPasswordEmail(user);
            return true;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<User> showAllUsersWithUserRoleOnly() {
        List<User> users = userRepository.findAllByRolesIsNotContaining(getSetWithAdminRoleOnly());
        if (users.size() > 0) {
            return users;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<User> showAllAdmins() {
        List<User> admins = userRepository.findAllByRolesIsContaining(getSetWithAdminRoleOnly());
        if (admins.size() > 0) {
            return admins;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<User> searchForUsersByEmail(String email) {
        List<User> users = userRepository.findAllByRolesIsNotContainingAndEmailIsContainingIgnoreCase
                (getSetWithAdminRoleOnly(), email);
        if (users.size() > 0) {
            return users;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<User> searchForAdminsByEmail(String email) {
        List<User> admins = userRepository.findAllByRolesIsContainingAndEmailIsContainingIgnoreCase
                (getSetWithAdminRoleOnly(), email);
        if (admins.size() > 0) {
            return admins;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<User> findAllUsersByNames(String firstName, String lastName) {
        List<User> users = userRepository
                .findAllByRolesIsNotContainingAndFirstNameIsContainingAndLastNameIsContainingAllIgnoreCase
                        (getSetWithAdminRoleOnly(), firstName, lastName);
        if (users.size() > 0) {
            return users;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<User> findAllAdminsByNames(String firstName, String lastName) {
        List<User> admins = userRepository
                .findAllByRolesIsContainingAndFirstNameIsContainingAndLastNameIsContainingAllIgnoreCase
                        (getSetWithAdminRoleOnly(), firstName, lastName);
        if (admins.size() > 0) {
            return admins;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private Set<Role> getSetWithAdminRoleOnly() {
        return new HashSet<>(Arrays.asList(getAdminRole()));
    }

    private Role getAdminRole() {
        Role role = roleRepository.findByRole(RoleEnum.ROLE_ADMIN.toString());
        if (role != null) {
            return role;
        } else {
            throw new ResourceNotFoundException("Critical Exception! Admin Role not found!");
        }
    }

    @Transactional
    public User changeUserActiveAccount(Long userId, Boolean active) {
        User user = getUserById(userId);
        if (!isUserAnAdmin(user)) {
            user.setActive(active);
            return userRepository.save(user);
        } else {
            throw new UserUnauthorizedException();
        }
    }

    private Boolean isUserAnAdmin(User user) {
        return user.getRoles().contains(getAdminRole());
    }

    @Transactional
    public Boolean sendEmailToUser(Long userId, EmailDto emailData) {
        User user = getUserById(userId);
        emailSender.sendEmailToPerson(user, emailData);
        return true;
    }

    @Transactional
    public Boolean sendActivationEmail(Long userId) {
        User user = getUserById(userId);
        emailSender.sendAccountActivationEmail(user);
        return true;
    }

    @Transactional
    public Boolean sendNewsletter(EmailDto newsletter) {
        List<User> newsletterUsers = userRepository.findAllByNewsletterIsTrue();
        emailSender.sendEmailToUsers(newsletter, newsletterUsers);
        return true;
    }

    @Transactional
    public User changeUserActiveAccountStatus(Long userId, Boolean active) {
        User user = getUserById(userId);
        if (!isUserASuperAdmin(user)) {
            user.setActive(active);
            return userRepository.save(user);
        } else {
            throw new UserUnauthorizedException();
        }
    }

    @Transactional
    public Reservation reserveClassById(Long userId, Long classId) {
        User user = getAuthenticatedUserById(userId);
        TrainingClass trainingClass = findTrainingClassAvailableForUser(classId);
        if (!isTrainingClassAlreadyReservedByUser(user.getId(), trainingClass.getId())) {
            Integer currentCapacity = trainingClass.getReservations().size();
            Boolean onTrainingList = false;
            if (currentCapacity < trainingClass.getMaxCapacity()) {
                onTrainingList = true;
            }
            Reservation reservation = new Reservation
                    (user, trainingClass, LocalDateTime.now(), onTrainingList);
            emailSender.sendClassReservationEmail(reservation, onTrainingList);
            return reservationRepository.save(reservation);
        } else {
            throw new ReservationException("*User already has reservation for this class.");
        }
    }

    private TrainingClass findTrainingClassAvailableForUser(Long classId) {
        TrainingClass trainingClass = trainingClassRepository.findTrainingClassByIdAvailableForUser
                (LocalDateTime.now(), LocalDateTime.now().plusDays(SHOW_CLASSES_DAYS_NUMBER), classId);
        if (trainingClass != null) {
            return trainingClass;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private Boolean isTrainingClassAlreadyReservedByUser(Long userId, Long classId) {
        return reservationRepository.existsByUserIdAndTrainingClassId(userId, classId);
    }

    @Transactional
    public Boolean cancelClass(Long classId, Long userId) {
        User user = getAuthenticatedUserById(userId);
        TrainingClass trainingClass = findTrainingClassById(classId);
        if (isTrainingClassAlreadyReservedByUser(user.getId(), trainingClass.getId())) {
            Reservation reservation =
                    findReservationByUserIdAndTrainingClassId(user.getId(), trainingClass.getId());
            if (reservation.getOnTrainingList()) {
                deleteUserFromTrainingListAndMoveFirstUserFromWaitingListToTraining(classId, reservation);
            } else {
                deleteUserFromWaitingList(reservation);
            }
            return true;
        } else {
            throw new ReservationException("*User do not have reservation for this class.");
        }
    }

    private void deleteUserFromWaitingList(Reservation reservation) {
        reservationRepository.delete(reservation);
        emailSender.sendClassCancellationEmail(reservation);
    }

    private void deleteUserFromTrainingListAndMoveFirstUserFromWaitingListToTraining
            (Long classId, Reservation reservation) {
        reservationRepository.delete(reservation);
        emailSender.sendClassCancellationEmail(reservation);
        Reservation awaitingReservation
                = findFirstReservationOnAwaitingListByClassId(classId);
        if (awaitingReservation != null) {
            awaitingReservation.setOnTrainingList(true);
            reservationRepository.save(awaitingReservation);
            emailSender.sendClassReservationEmail(awaitingReservation, true);
        }
    }

    private TrainingClass findTrainingClassById(Long classId) {
        TrainingClass trainingClass = trainingClassRepository.findById(classId).orElse(null);
        if (trainingClass != null) {
            return trainingClass;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private Reservation findReservationByUserIdAndTrainingClassId(Long userId, Long classId) {
        Reservation reservation = reservationRepository.findByUserIdAndTrainingClassId(userId, classId);
        if (reservation != null) {
            return reservation;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private Reservation findFirstReservationOnAwaitingListByClassId(Long classId) {
        return reservationRepository
                .findFirstByTrainingClassIdAndOnTrainingListIsFalseOrderByReservationTimeAsc(classId);
    }

}
