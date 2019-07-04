package pl.coderslab.javaGym.entityDtoConverter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.javaGym.dataTransferObject.InstructorDto;
import pl.coderslab.javaGym.entity.data.Instructor;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InstructorEntityDtoConverter {

    private ModelMapper modelMapper;

    @Autowired
    public InstructorEntityDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Instructor convertInstructorToEntity(InstructorDto instructorDto) {
        return modelMapper.map(instructorDto, Instructor.class);
    }

    public InstructorDto convertInstructorToDto(Instructor instructor) {
        return modelMapper.map(instructor, InstructorDto.class);
    }

    public List<InstructorDto> convertInstructorEntityToDtoList(List<Instructor> instructors) {
        return instructors.stream()
                .map(instructor -> convertInstructorToDto(instructor))
                .collect(Collectors.toList());
    }
}

