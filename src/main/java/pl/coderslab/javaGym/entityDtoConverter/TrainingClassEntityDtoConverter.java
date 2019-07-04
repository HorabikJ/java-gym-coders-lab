package pl.coderslab.javaGym.entityDtoConverter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.javaGym.dataTransferObject.TrainingClassDto;
import pl.coderslab.javaGym.entity.data.TrainingClass;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrainingClassEntityDtoConverter {

    private ModelMapper modelMapper;

    @Autowired
    public TrainingClassEntityDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TrainingClassDto convertTrainingClassToDto(TrainingClass trainingClass) {
        TrainingClassDto trainingClassDto = modelMapper
                .map(trainingClass, TrainingClassDto.class);
        trainingClassDto.setReservedPlaces(trainingClass.getReservations().size());
        return trainingClassDto;
    }

    public List<TrainingClassDto> convertTrainingClassToDtoList(List<TrainingClass> trainingClasses) {
        return trainingClasses.stream()
                .map(trainingClass -> convertTrainingClassToDto(trainingClass))
                .collect(Collectors.toList());
    }
}
