package pl.coderslab.javaGym.entityDtoConverter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.javaGym.dataTransferObject.UserDto;
import pl.coderslab.javaGym.entity.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserEntityDtoConverter {

    private ModelMapper modelMapper;

    @Autowired
    public UserEntityDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDto convertUserToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setPassword(null);
        return userDto;
    }

    public User convertUserToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    public List<UserDto> convertUserToDtoList(List<User> users) {
        return users.stream()
                .map(user -> convertUserToDto(user))
                .collect(Collectors.toList());
    }
}
