package pl.coderslab.javaGym.service.dataService;

import org.springframework.stereotype.Service;
import pl.coderslab.javaGym.dataTransferObject.TrainingClassDto;
import pl.coderslab.javaGym.entity.data.Instructor;
import pl.coderslab.javaGym.entity.data.TrainingClass;
import pl.coderslab.javaGym.entity.data.TrainingType;
import pl.coderslab.javaGym.error.customException.ActionNotAllowedException;
import pl.coderslab.javaGym.error.customException.ResourceNotFoundException;
import pl.coderslab.javaGym.repository.InstructorRepository;
import pl.coderslab.javaGym.repository.TrainingClassRepository;
import pl.coderslab.javaGym.repository.TrainingTypeRepository;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TrainingClassService implements AbstractDataService<TrainingClass> {

    private TrainingClassRepository trainingClassRepository;
    private InstructorRepository instructorRepository;
    private TrainingTypeRepository trainingTypeRepository;

    public TrainingClassService(TrainingClassRepository trainingClassRepository,
                                InstructorRepository instructorRepository,
                                TrainingTypeRepository trainingTypeRepository) {
        this.trainingClassRepository = trainingClassRepository;
        this.instructorRepository = instructorRepository;
        this.trainingTypeRepository = trainingTypeRepository;
    }

    public List<TrainingClass> saveTrainingClass
            (TrainingClassDto trainingClassDto, Integer occurrence, Integer repeat) {
        List<TrainingClass> trainingClasses = new ArrayList<>();
        String uniqueClassId = UUID.randomUUID().toString();
        LocalDateTime startDate = trainingClassDto.getStartDate();
        for (int i = 0; i < repeat; i++) {
            LocalDateTime eachStartTime = startDate.plus(Period.ofDays(occurrence * i));
            String timeReservedErrorMessage = getErorMesageIfNewClassTimeIsReserved
                    (eachStartTime, trainingClassDto.getDurationInMinutes());
            if (timeReservedErrorMessage != null) {
                saveTrainingClass(trainingClassDto, trainingClasses, uniqueClassId, eachStartTime);
            } else {
                throw new ActionNotAllowedException("*This time is already reserved. Please see details: "
                        + timeReservedErrorMessage);
            }
        }
        return trainingClasses;
    }

    private void saveTrainingClass(TrainingClassDto trainingClassDto, List<TrainingClass> trainingClasses, String uniqueClassId, LocalDateTime eachStartTime) {
        TrainingClass trainingClass = new TrainingClass();
        trainingClass.setUniqueClassId(uniqueClassId);
        trainingClass.setStartDate(eachStartTime);
        trainingClass.setDurationInMinutes(trainingClassDto.getDurationInMinutes());
        trainingClass.setMaxCapacity(trainingClassDto.getMaxCapacity());
        trainingClassRepository.save(trainingClass);
        trainingClasses.add(trainingClass);
    }

    private String getErorMesageIfNewClassTimeIsReserved(LocalDateTime startDate, Integer durationInMinutes) {
        List<TrainingClass> trainingClasses = trainingClassRepository.findAll();
        LocalDateTime newTrainingStartDate = startDate;
        LocalDateTime newTrainingEndDate = startDate.plusMinutes(durationInMinutes);
        StringBuffer sb = new StringBuffer();
        for (TrainingClass trainingClass : trainingClasses) {
            LocalDateTime anyTrainingStartDate = trainingClass.getStartDate();
            LocalDateTime anyTrainingEndDate = trainingClass.getStartDate()
                    .plusMinutes(trainingClass.getDurationInMinutes());
            if ((newTrainingStartDate.isAfter(anyTrainingStartDate) &&
                newTrainingStartDate.isBefore(anyTrainingEndDate)) ||
                (newTrainingEndDate.isAfter(anyTrainingStartDate) &&
                newTrainingEndDate.isBefore(anyTrainingEndDate))) {
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


    private Instructor getInstructorById(Long id) {
        Instructor instructor = instructorRepository.findById(id).orElse(null);
        if (instructor != null) {
            return instructor;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private TrainingType getTrainingTypeById(Long id) {
        TrainingType trainingTypeEntity = trainingTypeRepository.findById(id).orElse(null);
        if (trainingTypeEntity != null) {
            return trainingTypeEntity;
        } else {
            throw new ResourceNotFoundException();
        }
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
        return null;
    }

    @Override
    public Boolean deleteById(Long id) {
        return null;
    }

    @Override
    public TrainingClass edit(TrainingClass trainingClass, Long id) {
        return null;
    }


}
