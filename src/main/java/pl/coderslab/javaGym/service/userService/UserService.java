package pl.coderslab.javaGym.service.userService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.javaGym.dataTransferObject.EmailDto;
import pl.coderslab.javaGym.emailSender.EmailSender;
import pl.coderslab.javaGym.entity.data.Reservation;
import pl.coderslab.javaGym.entity.data.TrainingClass;
import pl.coderslab.javaGym.entity.user.Role;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.enums.RoleEnum;
import pl.coderslab.javaGym.error.customException.*;
import pl.coderslab.javaGym.repository.ReservationRepository;
import pl.coderslab.javaGym.repository.RoleRepository;
import pl.coderslab.javaGym.repository.TrainingClassRepository;
import pl.coderslab.javaGym.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Value("${application.globalvalues.classes-show-period-days}")
    private Integer CLASSES_SHOW_PERIOD;

    private EmailSender emailSender;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private TrainingClassRepository trainingClassRepository;
    private ReservationRepository reservationRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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

    public User findUserById(Long userId) {
        return getUserIfNotNull(userRepository.findById(userId).orElse(null));
    }

    @Transactional
    public Boolean deleteUserById(Long userId) {
        User user = findUserById(userId);
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
        roles.add(getUserRole());
        if (asAdmin) {
            roles.add(getAdminRole());
        }
        user.setRoles(roles);
    }

    @Transactional
    public Boolean deleteAnyUserById(Long userId) {
        User user = findUserById(userId);
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
        User user = findUserById(userId);
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
        User user = getUserIfNotNull(userRepository.findByEmail(userEmail));
        emailSender.sendResetPasswordEmail(user);
        return true;
    }

    public List<User> showAllUsersWithUserRoleOnly() {
        return getUserListIfNotEmpty(userRepository
                .findAllByRolesIsNotContaining(getSetWithAdminRoleOnly()));
    }

    public List<User> showAllAdmins() {
        return getUserListIfNotEmpty(userRepository
                .findAllByRolesIsContaining(getSetWithAdminRoleOnly()));
    }

    public List<User> searchForUsersByEmail(String email) {
        return getUserListIfNotEmpty(userRepository.findAllByRolesIsNotContainingAndEmailIsContainingIgnoreCase
                (getSetWithAdminRoleOnly(), email));
    }

    public List<User> searchForAdminsByEmail(String email) {
        return getUserListIfNotEmpty(userRepository.findAllByRolesIsContainingAndEmailIsContainingIgnoreCase
                (getSetWithAdminRoleOnly(), email));
    }

    public List<User> findAllUsersByNames(String firstName, String lastName) {
        return getUserListIfNotEmpty(userRepository
                .findAllByRolesIsNotContainingAndFirstNameIsContainingAndLastNameIsContainingAllIgnoreCase
                        (getSetWithAdminRoleOnly(), firstName, lastName));
    }

    public List<User> findAllAdminsByNames(String firstName, String lastName) {
        return getUserListIfNotEmpty(userRepository
                .findAllByRolesIsContainingAndFirstNameIsContainingAndLastNameIsContainingAllIgnoreCase
                        (getSetWithAdminRoleOnly(), firstName, lastName));
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

    private Role getUserRole() {
        Role role = roleRepository.findByRole(RoleEnum.ROLE_USER.toString());
        if (role != null) {
            return role;
        } else {
            throw new ResourceNotFoundException("Critical Exception! User Role not found!");
        }
    }

    @Transactional
    public User changeUserActiveAccount(Long userId, Boolean active) {
        User user = findUserById(userId);
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
    public Boolean sendEmailToUser(Long userId, EmailDto email) {
        User user = findUserById(userId);
        emailSender.sendEmailToPerson(user, email);
        return true;
    }

    @Transactional
    public Boolean sendActivationEmail(Long userId) {
        User user = findUserById(userId);
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
        User user = findUserById(userId);
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
            boolean onTrainingList = false;
            if (currentCapacity < trainingClass.getMaxCapacity()) {
                onTrainingList = true;
            }
            Reservation reservation = new Reservation
                    (user, trainingClass, LocalDateTime.now(), onTrainingList);
            emailSender.sendClassReservationEmail(reservation, onTrainingList);
            return reservationRepository.save(reservation);
        } else {
            throw new ReservationException("*User already has a reservation for this training class.");
        }
    }

    private TrainingClass findTrainingClassAvailableForUser(Long classId) {
        return getTrainingClassIfNotNull(trainingClassRepository.findTrainingClassByIdAvailableForUser
                (LocalDateTime.now(), LocalDateTime.now().plusDays
                        (CLASSES_SHOW_PERIOD), classId));
    }

    private Boolean isTrainingClassAlreadyReservedByUser(Long userId, Long classId) {
        return reservationRepository.existsByUserIdAndTrainingClassId(userId, classId);
    }

    @Transactional
    public Boolean cancelClassById(Long classId, Long userId) {
        User user = getAuthenticatedUserById(userId);
        TrainingClass trainingClass = findTrainingClassById(classId);
        if (isTrainingClassAlreadyReservedByUser(user.getId(), trainingClass.getId())) {
            Reservation reservation =
                    findReservationByUserIdAndTrainingClassId(user.getId(), trainingClass.getId());
            if (trainingClass.getStartDate().isAfter(LocalDateTime.now())) {
                if (reservation.getOnTrainingList()) {
                    deleteUserFromTrainingListAndMoveFirstUserFromWaitingToTrainingList(classId, reservation);
                } else {
                    deleteUserFromWaitingList(reservation);
                }
                return true;
            } else {
                throw new ReservationException("*Can not editTrainingType reservation for past training class.");
            }
        } else {
            throw new ReservationException("*User do not have a reservation for this training class.");
        }
    }

    private void deleteUserFromWaitingList(Reservation reservation) {
        reservationRepository.delete(reservation);
        emailSender.sendClassCancellationEmail(reservation);
    }

    private void deleteUserFromTrainingListAndMoveFirstUserFromWaitingToTrainingList
            (Long classId, Reservation reservation) {
        reservationRepository.delete(reservation);
        emailSender.sendClassCancellationEmail(reservation);
        Reservation awaitingReservation
                = findFirstReservationOnAwaitingListByClassId(classId);
        if (awaitingReservation != null) {
            awaitingReservation.setOnTrainingList(true);
            reservationRepository.save(awaitingReservation);
            emailSender.sendJumpToTrainingEmail(awaitingReservation);
        }
    }

    private TrainingClass findTrainingClassById(Long classId) {
        return getTrainingClassIfNotNull(trainingClassRepository.findById(classId).orElse(null));
    }

    private Reservation findReservationByUserIdAndTrainingClassId(Long userId, Long classId) {
        return getReservationIfNotNull(reservationRepository.findByUserIdAndTrainingClassId(userId, classId));
    }

    private Reservation findFirstReservationOnAwaitingListByClassId(Long classId) {
        return reservationRepository
                .findFirstByTrainingClassIdAndOnTrainingListIsFalseOrderByReservationTimeAsc(classId);
    }

    public List<Reservation> showFutureReservationsByUserId(Long userId) {
        User user = getAuthenticatedUserById(userId);
        return getReservationListIfNotEmpty(reservationRepository
                .findAllFutureClassReservationsByUserId(user.getId(), LocalDateTime.now()));
    }

    public List<Reservation> showPastReservationsByUserId(Long userId) {
        User user = getAuthenticatedUserById(userId);
        return getReservationListIfNotEmpty(reservationRepository
                .findAllPastClassReservationsByUserId(user.getId(), LocalDateTime.now()));
    }

    private List<User> getUserListIfNotEmpty(List<User> users) {
        if (users.size() > 0) {
            return users;
        } else {
            throw new ResourceNotFoundException("*Users not found!");
        }
    }

    private User getUserIfNotNull(User user) {
        if (user != null) {
            return user;
        } else {
            throw new ResourceNotFoundException("*User not found!");
        }
    }

    private List<Reservation> getReservationListIfNotEmpty(List<Reservation> reservations) {
        if (reservations.size() > 0) {
            return reservations;
        } else {
            throw new ResourceNotFoundException("*Reservations not found!");
        }
    }

    private Reservation getReservationIfNotNull(Reservation reservation) {
        if (reservation != null) {
            return reservation;
        } else {
            throw new ResourceNotFoundException("*Reservation not found!");
        }
    }

    private TrainingClass getTrainingClassIfNotNull(TrainingClass trainingClass) {
        if (trainingClass != null) {
            return trainingClass;
        } else {
            throw new ResourceNotFoundException("*Training class not found!");
        }
    }
}
