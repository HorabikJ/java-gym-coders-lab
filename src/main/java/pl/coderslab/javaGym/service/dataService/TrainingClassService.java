package pl.coderslab.javaGym.service.dataService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.javaGym.dataTransferObject.EmailDto;
import pl.coderslab.javaGym.dataTransferObject.TrainingClassDto;
import pl.coderslab.javaGym.emailSender.EmailSender;
import pl.coderslab.javaGym.entity.data.Instructor;
import pl.coderslab.javaGym.entity.data.TrainingClass;
import pl.coderslab.javaGym.entity.data.TrainingType;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.error.customException.ActionNotAllowedException;
import pl.coderslab.javaGym.error.customException.ResourceNotFoundException;
import pl.coderslab.javaGym.repository.InstructorRepository;
import pl.coderslab.javaGym.repository.TrainingClassRepository;
import pl.coderslab.javaGym.repository.TrainingTypeRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TrainingClassService implements AbstractDataService<TrainingClass> {

    private TrainingClassRepository trainingClassRepository;
    private InstructorRepository instructorRepository;
    private TrainingTypeRepository trainingTypeRepository;
    private EmailSender emailSender;

    public TrainingClassService(TrainingClassRepository trainingClassRepository,
                                InstructorRepository instructorRepository,
                                TrainingTypeRepository trainingTypeRepository,
                                EmailSender emailSender) {
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
                saveTrainingClass(trainingClassDto, savedTrainingClasses, groupClassId, eachStartTime);
            } else {
                throw new ActionNotAllowedException("*This time is already reserved." +
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
            if (isNewTrainingTimeNotColliedWithAnyOtherTime
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

    private boolean isNewTrainingTimeNotColliedWithAnyOtherTime
            (LocalDateTime eachStartDate, LocalDateTime eachEndDate, LocalDateTime anyTrainingStartDate,
             LocalDateTime anyTrainingEndDate) {
        return (eachStartDate.isAfter(anyTrainingStartDate) &&
            eachStartDate.isBefore(anyTrainingEndDate)) ||
            (eachEndDate.isAfter(anyTrainingStartDate) &&
            eachEndDate.isBefore(anyTrainingEndDate)) ||
            (eachStartDate.isBefore(anyTrainingStartDate) &&
            eachEndDate.isAfter(anyTrainingEndDate));
    }

    private void saveTrainingClass(TrainingClassDto trainingClassDto, List<TrainingClass> trainingClasses,
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
        Instructor instructor = getInstructorById(instructorId);
        for (TrainingClass trainingClass : classes) {
            trainingClass.setInstructor(instructor);
            trainingClassRepository.save(trainingClass);
        }
        return classes;
    }

    public List<TrainingClass> findAllByClassGroupId(String classGroupId) {
        List<TrainingClass> classes = trainingClassRepository.findAllByClassGroupId(classGroupId);
        if (classes.size() > 0) {
            return classes;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private Instructor getInstructorById(Long id) {
        Instructor instructor = instructorRepository.findById(id).orElse(null);
        if (instructor != null) {
            return instructor;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Transactional
    public List<TrainingClass> setTrainingTypeByClassGroupId(String classGroupId, Long trainingTypeId) {
        List<TrainingClass> classes = findAllByClassGroupIdAndStartDateIsAfter
                (classGroupId, LocalDateTime.now());
        TrainingType trainingType = getTrainingTypeById(trainingTypeId);
        for (TrainingClass trainingClass : classes) {
            trainingClass.setTrainingType(trainingType);
            trainingClassRepository.save(trainingClass);
        }
        return classes;
    }

    private TrainingType getTrainingTypeById(Long id) {
        TrainingType trainingTypeEntity = trainingTypeRepository.findById(id).orElse(null);
        if (trainingTypeEntity != null) {
            return trainingTypeEntity;
        } else {
            throw new ResourceNotFoundException();
        }
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
                throw new ActionNotAllowedException("*This time is already reserved." +
                        " Please see reserved time details: "
                        + errorMessage);
            }
        }
        return classesToChange;
    }

    private List<TrainingClass> findAllByClassGroupIdAndStartDateIsAfter(String classGroupId, LocalDateTime startDate) {
        List<TrainingClass> classes = trainingClassRepository
                .findAllByClassGroupIdAndStartDateIsAfter(classGroupId, startDate);
        if (classes.size() > 0) {
            return classes;
        } else {
            throw new ResourceNotFoundException();
        }
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
                throw new ActionNotAllowedException("*This time is already reserved." +
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
        List<TrainingClass> classes = findAllByClassGroupIdAndStartDateIsAfter
                (classGroupId, LocalDateTime.now());
        trainingClassRepository.deleteInBatch(classes);
        return true;
    }

    @Transactional
    public TrainingClass setInstructorByClassId(Long classId, Long instructorId) {
        TrainingClass trainingClass = findByIdAndStartDateIsAfter(classId, LocalDateTime.now());
        Instructor instructor = getInstructorById(instructorId);
        trainingClass.setInstructor(instructor);
        return trainingClassRepository.save(trainingClass);
    }

    private TrainingClass findByIdAndStartDateIsAfter(Long id, LocalDateTime startDate) {
        TrainingClass trainingClass = trainingClassRepository.findByIdAndStartDateIsAfter(id, startDate);
        if (trainingClass != null) {
            return trainingClass;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Transactional
    public TrainingClass setTrainingTypeByClassId(Long classId, Long trainingTypeId) {
        TrainingClass trainingClass = findByIdAndStartDateIsAfter(classId, LocalDateTime.now());
        TrainingType trainingType = getTrainingTypeById(trainingTypeId);
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
//            TODO change this exception to a new one?
            throw new ActionNotAllowedException("*This time is already reserved." +
                    " Please see reserved time details: "
                    + errorMessage);
        }
    }

    private List<TrainingClass> findByIdIsNotAndStartDateIsAfter(Long id, LocalDateTime startDate) {
        return trainingClassRepository.findByIdIsNotAndStartDateIsAfter(id, LocalDateTime.now());
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
            throw new ActionNotAllowedException("*This time is already reserved." +
                    " Please see reserved time details: "
                    + errorMessage);
        }
    }

    @Transactional
    public Boolean deleteClassByClassId(Long classId) {
        TrainingClass trainingClass = findByIdAndStartDateIsAfter(classId, LocalDateTime.now());
        if (trainingClass != null) {
            trainingClassRepository.delete(trainingClass);
            return true;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<TrainingClass> findAllInFutureWhereAnyRelationIsNull() {
        List<TrainingClass> trainingClasses = trainingClassRepository
                .findAllTrainingClassesInFutureWhereAnyRelationIsNull(LocalDateTime.now());
        if (trainingClasses.size() > 0) {
            return trainingClasses;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<User> findAllUsersOnClassReservationListByClassId(Long classId) {
        List<User> users = trainingClassRepository.findAllUsersOnClassReservationListForByClassId(classId);
        if (users.size() > 0) {
            return users;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<User> findAllUsersOnClassAwaitingListByClassId(Long classId) {
        List<User> users = trainingClassRepository.findAllUsersOnClassAwaitingListForByClassId(classId);
        if (users.size() > 0) {
            return users;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<TrainingClass> findAllByStartDateIsInFuture() {
        List<TrainingClass> trainingClasses = trainingClassRepository.findAllByStartDateIsAfter(LocalDateTime.now());
        if (trainingClasses != null) {
            return trainingClasses;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<TrainingClass> findAllByStartDateIsInPast() {
        List<TrainingClass> trainingClasses = trainingClassRepository.findAllByStartDateIsBefore(LocalDateTime.now());
        if (trainingClasses != null) {
            return trainingClasses;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public Boolean sendEmailToAllClassByIdCustomers(Long classId, EmailDto email) {
        TrainingClass trainingClass = findById(classId);
        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(trainingClass.getCustomers());
        allUsers.addAll(trainingClass.getAwaitingCustomers());
        emailSender.sendEmailToUsers(email, allUsers);
        return true;
    }

    @Override
    public TrainingClass save(TrainingClass trainingClass) {
        return null;
    }

    @Override
    public List<TrainingClass> findAll() {
        return null;
    }

    @Override
    public TrainingClass findById(Long id) {
        TrainingClass trainingClass = trainingClassRepository.findById(id).orElse(null);
        if (trainingClass != null) {
            return  trainingClass;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Override
    public Boolean deleteById(Long id) {
        return null;
    }

    @Override
    public TrainingClass edit(TrainingClass trainingClass, Long id) {
        return null;
    }

    public List<TrainingClass> findAllFutureClassesForInstructor(Long instructorId) {
        Instructor instructor = getInstructorById(instructorId);
        List<TrainingClass> classes = trainingClassRepository
                .findAllByInstructorIdAndStartDateIsAfter(instructor.getId(), LocalDateTime.now());
        if (classes.size() > 0) {
            return classes;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public List<TrainingClass> findAllByTrainingTypeId(Long trainingTypeId) {
        TrainingType trainingType = getTrainingTypeById(trainingTypeId);
        List<TrainingClass> classes = trainingClassRepository
                .findAllByTrainingTypeIdAndStartDateIsAfter(trainingType.getId(), LocalDateTime.now());
        if (classes.size() > 0) {
            return classes;
        } else {
            throw new ResourceNotFoundException();
        }
    }
}
