package pl.coderslab.javaGym.service.dataService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.javaGym.dataTransferObject.EmailDto;
import pl.coderslab.javaGym.dataTransferObject.TrainingClassDto;
import pl.coderslab.javaGym.emailSender.EmailSender;
import pl.coderslab.javaGym.entity.data.Instructor;
import pl.coderslab.javaGym.entity.data.Reservation;
import pl.coderslab.javaGym.entity.data.TrainingClass;
import pl.coderslab.javaGym.entity.data.TrainingType;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.error.customException.ClassTimeReservedException;
import pl.coderslab.javaGym.error.customException.ResourceNotFoundException;
import pl.coderslab.javaGym.globalValue.GlobalValue;
import pl.coderslab.javaGym.repository.InstructorRepository;
import pl.coderslab.javaGym.repository.ReservationRepository;
import pl.coderslab.javaGym.repository.TrainingClassRepository;
import pl.coderslab.javaGym.repository.TrainingTypeRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TrainingClassService {

    private ReservationRepository reservationRepository;
    private TrainingClassRepository trainingClassRepository;
    private InstructorRepository instructorRepository;
    private TrainingTypeRepository trainingTypeRepository;
    private EmailSender emailSender;

    public TrainingClassService(TrainingClassRepository trainingClassRepository,
                                InstructorRepository instructorRepository,
                                TrainingTypeRepository trainingTypeRepository,
                                ReservationRepository reservationRepository,
                                EmailSender emailSender) {
        this.reservationRepository = reservationRepository;
        this.trainingClassRepository = trainingClassRepository;
        this.instructorRepository = instructorRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.emailSender = emailSender;
    }

    @Transactional
    public List<TrainingClass> saveTrainingClass
            (TrainingClassDto trainingClassDto, Integer occurrence, Integer repeat) {
        List<TrainingClass> savedTrainingClasses = new ArrayList<>();
        List<TrainingClass> allFutureClasses = trainingClassRepository
                .findAllByStartDateIsAfter(LocalDateTime.now());
        String groupClassId = UUID.randomUUID().toString();
        LocalDateTime startTime = trainingClassDto.getStartDate();
        for (int i = 0; i < repeat; i++) {
            LocalDateTime eachStartTime = startTime.plus(Period.ofDays(occurrence * i));
            String errorMessage = getErrorMessageIfNewClassTimeIsReserved
                    (eachStartTime, trainingClassDto.getDurationInMinutes(), allFutureClasses);
            if (errorMessage == null) {
                insertTrainingClass(trainingClassDto, savedTrainingClasses, groupClassId, eachStartTime);
            } else {
                throw new ClassTimeReservedException("*This time is already reserved." +
                        " Please see reserved time details: "
                        + errorMessage);
            }
        }
        return savedTrainingClasses;
    }

    private String getErrorMessageIfNewClassTimeIsReserved
            (LocalDateTime eachStartDate, Integer durationInMinutes,
             List<TrainingClass> futureClassesToCheckTimeWith) {
        LocalDateTime eachEndDate = eachStartDate.plusMinutes(durationInMinutes);
        StringBuffer sb = new StringBuffer();
        for (TrainingClass trainingClass : futureClassesToCheckTimeWith) {
            LocalDateTime anyTrainingStartDate = trainingClass.getStartDate();
            LocalDateTime anyTrainingEndDate = trainingClass.getStartDate()
                    .plusMinutes(trainingClass.getDurationInMinutes());
            if (isNewTrainingTimeNotColliedWithAnyOtherTrainingTime
                    (eachStartDate, eachEndDate, anyTrainingStartDate, anyTrainingEndDate)) {
                return sb.append("Start time: ")
                        .append(anyTrainingStartDate)
                        .append(", end date: ")
                        .append(anyTrainingEndDate)
                        .append(".")
                        .toString();
            }
        }
        return null;
    }

    private boolean isNewTrainingTimeNotColliedWithAnyOtherTrainingTime
            (LocalDateTime eachStartDate, LocalDateTime eachEndDate, LocalDateTime anyTrainingStartDate,
             LocalDateTime anyTrainingEndDate) {
        return (eachStartDate.isAfter(anyTrainingStartDate) &&
                eachStartDate.isBefore(anyTrainingEndDate)) ||
                (eachEndDate.isAfter(anyTrainingStartDate) &&
                        eachEndDate.isBefore(anyTrainingEndDate)) ||
                (eachStartDate.isBefore(anyTrainingStartDate) &&
                        eachEndDate.isAfter(anyTrainingEndDate));
    }

    private void insertTrainingClass(TrainingClassDto trainingClassDto, List<TrainingClass> trainingClasses,
                                   String classGroupId, LocalDateTime eachStartTime) {
        TrainingClass trainingClass = new TrainingClass();
        trainingClass.setClassGroupId(classGroupId);
        trainingClass.setStartDate(eachStartTime);
        trainingClass.setDurationInMinutes(trainingClassDto.getDurationInMinutes());
        trainingClass.setMaxCapacity(trainingClassDto.getMaxCapacity());
        trainingClassRepository.save(trainingClass);
        trainingClasses.add(trainingClass);
    }

    @Transactional
    public List<TrainingClass> setInstructorByClassGroupId(String classGroupId, Long instructorId) {
        List<TrainingClass> classes = findAllByClassGroupIdAndStartDateIsAfter
                (classGroupId, LocalDateTime.now());
        Instructor instructor = findInstructorById(instructorId);
        for (TrainingClass trainingClass : classes) {
            trainingClass.setInstructor(instructor);
            trainingClassRepository.save(trainingClass);
        }
        return classes;
    }

    public List<TrainingClass> findAllByClassGroupId(String classGroupId) {
        return getTrainingClassListIfNotEmpty(trainingClassRepository.findAllByClassGroupId(classGroupId));
    }

    private Instructor findInstructorById(Long id) {
        return getInstructorIfNotNull(instructorRepository.findById(id).orElse(null));
    }

    @Transactional
    public List<TrainingClass> setTrainingTypeByClassGroupId(String classGroupId, Long trainingTypeId) {
        List<TrainingClass> classes = findAllByClassGroupIdAndStartDateIsAfter
                (classGroupId, LocalDateTime.now());
        TrainingType trainingType = findTrainingTypeById(trainingTypeId);
        for (TrainingClass trainingClass : classes) {
            trainingClass.setTrainingType(trainingType);
            trainingClassRepository.save(trainingClass);
        }
        return classes;
    }

    private TrainingType findTrainingTypeById(Long id) {
        return getTrainingTypeIfNotNull(trainingTypeRepository.findById(id).orElse(null));
    }

    @Transactional
    public List<TrainingClass> changeMaxCapacityByClassGroupId(String classGroupId, Integer maxCapacity) {
        List<TrainingClass> classes = findAllByClassGroupIdAndStartDateIsAfter
                (classGroupId, LocalDateTime.now());
        for (TrainingClass trainingClass : classes) {
            trainingClass.setMaxCapacity(maxCapacity);
            trainingClassRepository.save(trainingClass);
        }
        return classes;
    }

    @Transactional
    public List<TrainingClass> changeDurationByClassGroupId(String classGroupId, Integer newDuration) {
        List<TrainingClass> classesToChange =
                findAllByClassGroupIdAndStartDateIsAfter(classGroupId, LocalDateTime.now());
        List<TrainingClass> classesToCheckTimeWith =
                findAllByClassGroupIdIsNotAndStartDateIsAfter(classGroupId, LocalDateTime.now());
        for (TrainingClass trainingClass : classesToChange) {
            String errorMessage = getErrorMessageIfNewClassTimeIsReserved
                    (trainingClass.getStartDate(), newDuration, classesToCheckTimeWith);
            if (errorMessage == null) {
                trainingClass.setDurationInMinutes(newDuration);
                trainingClassRepository.save(trainingClass);
            } else {
                throw new ClassTimeReservedException("*This time is already reserved." +
                        " Please see reserved time details: "
                        + errorMessage);
            }
        }
        return classesToChange;
    }

    private List<TrainingClass> findAllByClassGroupIdAndStartDateIsAfter
            (String classGroupId, LocalDateTime startDate) {
        return getTrainingClassListIfNotEmpty(trainingClassRepository
                .findAllByClassGroupIdAndStartDateIsAfter(classGroupId, startDate));
    }

    private List<TrainingClass> findAllByClassGroupIdIsNotAndStartDateIsAfter
            (String classGroupId, LocalDateTime startTime) {
        return trainingClassRepository.findAllByClassGroupIdIsNotAndStartDateIsAfter(classGroupId, startTime);
    }

    @Transactional
    public List<TrainingClass> changeClassStartHourByClassGroupId
            (String classGroupId, Integer hour, Integer minute) {
        List<TrainingClass> classesToChange =
                findAllByClassGroupIdAndStartDateIsAfter(classGroupId, LocalDateTime.now());
        List<TrainingClass> classesToCheckTimeWith =
                findAllByClassGroupIdIsNotAndStartDateIsAfter(classGroupId, LocalDateTime.now());
        for (TrainingClass trainingClass : classesToChange) {
            trainingClass.setStartDate(getNewStartDateForClass(trainingClass.getStartDate(), hour, minute));
            String errorMessage = getErrorMessageIfNewClassTimeIsReserved
                    (trainingClass.getStartDate(), trainingClass.getDurationInMinutes(), classesToCheckTimeWith);
            if (errorMessage == null) {
                trainingClassRepository.save(trainingClass);
            } else {
                throw new ClassTimeReservedException("*This time is already reserved." +
                        " Please see reserved time details: "
                        + errorMessage);
            }
        }
        return classesToChange;
    }

    private LocalDateTime getNewStartDateForClass(LocalDateTime oldStartDate, Integer newHour, Integer newMinute) {
        return LocalDateTime.of(oldStartDate.toLocalDate(), LocalTime.of(newHour, newMinute));
    }

    @Transactional
    public Boolean deleteClassByClassGroupId(String classGroupId) {
        trainingClassRepository.deleteInBatch(getTrainingClassListIfNotEmpty
                (findAllByClassGroupIdAndStartDateIsAfter(classGroupId, LocalDateTime.now())));
        return true;
    }

    @Transactional
    public TrainingClass setInstructorByClassId(Long classId, Long instructorId) {
        TrainingClass trainingClass = findByIdAndStartDateIsAfter(classId, LocalDateTime.now());
        Instructor instructor = findInstructorById(instructorId);
        trainingClass.setInstructor(instructor);
        return trainingClassRepository.save(trainingClass);
    }

    private TrainingClass findByIdAndStartDateIsAfter(Long id, LocalDateTime startDate) {
        return getTrainingClassIfNotNull(trainingClassRepository
                .findByIdAndStartDateIsAfter(id, startDate));
    }

    @Transactional
    public TrainingClass setTrainingTypeByClassId(Long classId, Long trainingTypeId) {
        TrainingClass trainingClass = findByIdAndStartDateIsAfter(classId, LocalDateTime.now());
        TrainingType trainingType = findTrainingTypeById(trainingTypeId);
        trainingClass.setTrainingType(trainingType);
        return trainingClassRepository.save(trainingClass);
    }

    @Transactional
    public TrainingClass changeMaxCapacityByClassId(Long classId, Integer maxCapacity) {
        TrainingClass trainingClass = findByIdAndStartDateIsAfter(classId, LocalDateTime.now());
        trainingClass.setMaxCapacity(maxCapacity);
        return trainingClassRepository.save(trainingClass);
    }

    @Transactional
    public TrainingClass changeDurationForClassId(Long classId, Integer newDuration) {
        TrainingClass trainingClass =
                findByIdAndStartDateIsAfter(classId, LocalDateTime.now());
        List<TrainingClass> classesToCheckTimeWith =
                findByIdIsNotAndStartDateIsAfter(classId, LocalDateTime.now());
        String errorMessage = getErrorMessageIfNewClassTimeIsReserved
                (trainingClass.getStartDate(), newDuration, classesToCheckTimeWith);
        if (errorMessage == null) {
            trainingClass.setDurationInMinutes(newDuration);
            return trainingClassRepository.save(trainingClass);
        } else {
            throw new ClassTimeReservedException("*This time is already reserved." +
                    " Please see reserved time details: "
                    + errorMessage);
        }
    }

    private List<TrainingClass> findByIdIsNotAndStartDateIsAfter(Long id, LocalDateTime startDate) {
        return trainingClassRepository.findByIdIsNotAndStartDateIsAfter(id, startDate);
    }

    @Transactional
    public TrainingClass changeClassStartHourByClassId(Long classId, Integer hour, Integer minute) {
        TrainingClass trainingClass =
                findByIdAndStartDateIsAfter(classId, LocalDateTime.now());
        List<TrainingClass> classesToCheckTimeWith =
                findByIdIsNotAndStartDateIsAfter(classId, LocalDateTime.now());
        trainingClass.setStartDate(getNewStartDateForClass(trainingClass.getStartDate(), hour, minute));
        String errorMessage = getErrorMessageIfNewClassTimeIsReserved
                (trainingClass.getStartDate(), trainingClass.getDurationInMinutes(), classesToCheckTimeWith);
        if (errorMessage == null) {
            return trainingClassRepository.save(trainingClass);
        } else {
            throw new ClassTimeReservedException("*This time is already reserved." +
                    " Please see reserved time details: "
                    + errorMessage);
        }
    }

    @Transactional
    public Boolean deleteClassByClassId(Long classId) {
        TrainingClass trainingClass = getTrainingClassIfNotNull
                (findByIdAndStartDateIsAfter(classId, LocalDateTime.now()));
        trainingClassRepository.delete(trainingClass);
        return true;
    }

    public List<TrainingClass> findAllInFutureWhereAnyRelationIsNull() {
        return getTrainingClassListIfNotEmpty(trainingClassRepository
                .findAllTrainingClassesInFutureWhereAnyRelationIsNull(LocalDateTime.now()));
    }

    public List<Reservation> findAllReservationsByClassId(Long classId) {
        TrainingClass trainingClass = findTrainingClassById(classId);
        return getReservationListIfNotEmpty(reservationRepository
                .findAllByTrainingClassIdOrderByReservationTimeAsc(trainingClass.getId()));
    }

    public List<TrainingClass> findAllByStartDateIsInFuture() {
        return getTrainingClassListIfNotEmpty(trainingClassRepository
                .findAllByStartDateIsAfter(LocalDateTime.now()));
    }

    public List<TrainingClass> findAllByStartDateIsInPast() {
        return getTrainingClassListIfNotEmpty(trainingClassRepository
                .findAllByStartDateIsBefore(LocalDateTime.now()));
    }

    public Boolean sendEmailToAllCustomersByTrainingClass(Long classId, EmailDto email) {
        List<User> users = findAllReservationsByClassId(classId)
                .stream()
                .map(Reservation::getUser)
                .collect(Collectors.toList());
        emailSender.sendEmailToUsers(email, users);
        return true;
    }

    public TrainingClass findTrainingClassById(Long id) {
        return getTrainingClassIfNotNull(trainingClassRepository.findById(id).orElse(null));
    }

    public List<TrainingClass> findAllFutureClassesByInstructor(Long instructorId) {
        Instructor instructor = findInstructorById(instructorId);
        return getTrainingClassListIfNotEmpty(trainingClassRepository
                .findAllByInstructorIdAndStartDateIsAfter(instructor.getId(), LocalDateTime.now()));
    }

    public List<TrainingClass> findAllFutureClassesByTrainingType(Long trainingTypeId) {
        TrainingType trainingType = findTrainingTypeById(trainingTypeId);
        return getTrainingClassListIfNotEmpty(trainingClassRepository
                .findAllByTrainingTypeIdAndStartDateIsAfter(trainingType.getId(), LocalDateTime.now()));
    }

    public List<TrainingClass> findAllClassesAvailableForUsers() {
        return getTrainingClassListIfNotEmpty(trainingClassRepository.findAllTrainingClassesAvailableForUsers
                (LocalDateTime.now(), LocalDateTime.now().plusDays(GlobalValue.CLASSES_SHOW_PERIOD_IN_DAYS)));
    }

    public List<TrainingClass> findAllTrainingClassesForUsersByInstructorId(Long instructorId) {
        Instructor instructor = findInstructorById(instructorId);
        return getTrainingClassListIfNotEmpty(findAllClassesAvailableForUsers().stream()
                .filter(trainingClass -> trainingClass.getInstructor().equals(instructor))
                .collect(Collectors.toList()));
    }

    public List<TrainingClass> findAllTrainingClassesForUsersByTrainingTypeId(Long trainingTypeId) {
        TrainingType trainingType = findTrainingTypeById(trainingTypeId);
        return getTrainingClassListIfNotEmpty(findAllClassesAvailableForUsers().stream()
                .filter(trainingClass -> trainingClass.getTrainingType().equals(trainingType))
                .collect(Collectors.toList()));
    }

    private Instructor getInstructorIfNotNull(Instructor instructor) {
        if (instructor != null) {
            return instructor;
        } else {
            throw new ResourceNotFoundException("*Instructor not found!");
        }
    }

    private TrainingType getTrainingTypeIfNotNull(TrainingType trainingType) {
        if (trainingType != null) {
            return trainingType;
        } else {
            throw new ResourceNotFoundException("*TrainingType not found!");
        }
    }

    private List<TrainingClass> getTrainingClassListIfNotEmpty(List<TrainingClass> trainingClasses) {
        if (trainingClasses.size() > 0) {
            return trainingClasses;
        } else {
            throw new ResourceNotFoundException("*TrainingClasses not found!");
        }
    }

    private TrainingClass getTrainingClassIfNotNull(TrainingClass trainingClass) {
        if (trainingClass != null) {
            return trainingClass;
        } else {
            throw new ResourceNotFoundException("*TrainingClass not found!");
        }
    }

    private List<Reservation> getReservationListIfNotEmpty(List<Reservation> reservations) {
        if (reservations.size() > 0) {
            return reservations;
        } else {
            throw new ResourceNotFoundException("*Reservation not found!");
        }
    }


}
