package pl.coderslab.javaGym.service.dataService;

import org.springframework.stereotype.Service;
import pl.coderslab.javaGym.entity.data.Instructor;
import pl.coderslab.javaGym.entity.data.TrainingClass;
import pl.coderslab.javaGym.entity.data.TrainingType;
import pl.coderslab.javaGym.error.customException.ResourceNotFoundException;
import pl.coderslab.javaGym.dataTransferObject.TrainingClassDto;
import pl.coderslab.javaGym.repository.InstructorRepository;
import pl.coderslab.javaGym.repository.TrainingClassRepository;
import pl.coderslab.javaGym.repository.TrainingTypeRepository;

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

//    public List<TrainingClass> saveTrainingClass
//            (TrainingClassDto trainingClassJson, Integer frequency, Integer occurrence) {
//        List<TrainingClass> savedTrainingClasses = new ArrayList<>();
//        for (int i = 0; i < occurrence; i ++) {
//            TrainingClass save = trainingClassRepository.save(getTrainingClassEntityObject(trainingClassJson));
//            savedTrainingClasses.add(save);
//        }
//        return savedTrainingClasses;
//    }

//    private TrainingClass getTrainingClassEntityObject(TrainingClassDto trainingClassJson) {
//        TrainingClass trainingClass = new TrainingClass();
//        trainingClass.setUniqueClassId(UUID.randomUUID().toString());
//        trainingClass.setMaxCapacity(trainingClassJson.getMaxCapacity());
////        trainingClass.setStartDate(trainingClassJson.getStartDate());
//        trainingClass.setDurationInMinutes(trainingClassJson.getDurationInMinutes());
//        trainingClass.setInstructor(getInstructorById(trainingClassJson.getInstructorId()));
//        trainingClass.setTrainingTypeEntity(getTrainingTypeById(trainingClassJson.getTrainingTypeId()));
//        return trainingClass;
//    }

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
}
