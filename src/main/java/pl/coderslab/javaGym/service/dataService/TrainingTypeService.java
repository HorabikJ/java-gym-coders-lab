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
public class TrainingTypeService implements AbstractDataService<TrainingType> {

    private TrainingTypeRepository trainingTypeRepository;

    public TrainingTypeService(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Transactional
    @Override
    public TrainingType save(TrainingType trainingType) {
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

    @Override
    public List<TrainingType> findAll() {
        return null;
    }

    @Override
    public TrainingType findById(Long id) {
        return null;
    }

    @Transactional
    @Override
    public Boolean deleteById(Long id) {
        TrainingType trainingType = getTrainingTypeByIdFromDB(id);
        trainingTypeRepository.delete(trainingType);
        return true;
    }

    @Override
    public TrainingType edit(TrainingType newTrainingType, Long id) {
        TrainingType trainingTypeFromDB = getTrainingTypeByIdFromDB(id);
        if (newTrainingType.getName().equals(trainingTypeFromDB.getName())) {
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

    private TrainingType getTrainingTypeByIdFromDB(Long id) {
        TrainingType trainingType = trainingTypeRepository.findById(id).orElse(null);
        if (trainingType != null) {
            return trainingType;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private Boolean isTrainingTypeNameAlreadyInDB(TrainingType trainingType) {
        return trainingTypeRepository.existsByNameIgnoreCase(trainingType.getName());
    }
}
