package pl.coderslab.javaGym.entityDtoConverter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.javaGym.dataTransferObject.TrainingTypeDto;
import pl.coderslab.javaGym.entity.data.TrainingType;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrainingTypeEntityDtoConverter {

    private ModelMapper modelMapper;

    @Autowired
    public TrainingTypeEntityDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TrainingType convertTrainingTypeToEntity(TrainingTypeDto trainingTypeDto) {
        return modelMapper.map(trainingTypeDto, TrainingType.class);
    }

    public TrainingTypeDto convertTrainingTypeToDto(TrainingType trainingType) {
        return modelMapper.map(trainingType, TrainingTypeDto.class);
    }

    public List<TrainingTypeDto> convertTrainingTypesToDtoList(List<TrainingType> trainingTypes) {
        return trainingTypes.stream()
                .map(trainingType -> convertTrainingTypeToDto(trainingType))
                .collect(Collectors.toList());
    }

}
