package pl.coderslab.javaGym.service.dataService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.javaGym.entity.data.TrainingType;
import pl.coderslab.javaGym.error.customException.ActionNotAllowedException;
import pl.coderslab.javaGym.error.customException.ResourceNotFoundException;
import pl.coderslab.javaGym.error.customException.UniqueDBFieldException;
import pl.coderslab.javaGym.repository.TrainingTypeRepository;

import java.util.List;

@Service
public class TrainingTypeService {

    private TrainingTypeRepository trainingTypeRepository;

    public TrainingTypeService(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Transactional
    public TrainingType saveTrainingType(TrainingType trainingType) {
        if (trainingType.getId() == null) {
            if (!isTrainingTypeNameAlreadyInDB(trainingType)) {
                return trainingTypeRepository.save(trainingType);
            } else {
                throw new UniqueDBFieldException();
            }
        } else {
            throw new ActionNotAllowedException();
        }
    }

    public List<TrainingType> findAllTrainingTypes() {
        return getTrainingTypeListIfNotEmpty(trainingTypeRepository.findAll());
    }

    @Transactional
    public Boolean deleteTrainingTypeById(Long id) {
        TrainingType trainingType = findTrainingTypeBy(id);
        trainingTypeRepository.delete(trainingType);
        return true;
    }

    public TrainingType editTrainingType(TrainingType newTrainingType, Long id) {
        TrainingType trainingTypeFromDB = findTrainingTypeBy(id);
        if (newTrainingType.getName().equals(trainingTypeFromDB.getName())) {
            newTrainingType.setId(trainingTypeFromDB.getId());
            return trainingTypeRepository.save(newTrainingType);
        } else {
            if (!isTrainingTypeNameAlreadyInDB(newTrainingType)) {
                newTrainingType.setId(trainingTypeFromDB.getId());
                return trainingTypeRepository.save(newTrainingType);
            } else {
                throw new UniqueDBFieldException();
            }
        }
    }

    public TrainingType findTrainingTypeBy(Long id) {
        return getTrainingTypeIfNotNull
                (trainingTypeRepository.findById(id).orElse(null));
    }

    private Boolean isTrainingTypeNameAlreadyInDB(TrainingType trainingType) {
        return trainingTypeRepository.existsByNameIgnoreCase(trainingType.getName());
    }

    private TrainingType getTrainingTypeIfNotNull(TrainingType trainingType) {
        if (trainingType != null) {
            return trainingType;
        } else {
            throw new ResourceNotFoundException("*TrainingType not found!");
        }
    }

    private List<TrainingType> getTrainingTypeListIfNotEmpty(List<TrainingType> trainingTypes) {
        if (trainingTypes.size() > 0) {
            return trainingTypes;
        } else {
            throw new ResourceNotFoundException("*TrainingTypes not found!");
        }
    }
}
