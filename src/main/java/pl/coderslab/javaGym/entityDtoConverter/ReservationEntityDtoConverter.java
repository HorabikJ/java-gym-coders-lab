package pl.coderslab.javaGym.entityDtoConverter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.javaGym.dataTransferObject.ReservationDto;
import pl.coderslab.javaGym.dataTransferObject.TrainingClassDto;
import pl.coderslab.javaGym.dataTransferObject.UserDto;
import pl.coderslab.javaGym.entity.data.Reservation;
import pl.coderslab.javaGym.entity.data.TrainingClass;
import pl.coderslab.javaGym.entity.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationEntityDtoConverter {

    private ModelMapper modelMapper;

    @Autowired
    public ReservationEntityDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ReservationDto convertReservationToDtoUserView(Reservation reservation) {
        ReservationDto reservationDto = modelMapper.map(reservation, ReservationDto.class);
        reservationDto.setUserDto(null);
        reservationDto.setTrainingClassDto(convertTrainingClassToDto(reservation.getTrainingClass()));
        return reservationDto;
    }

    private TrainingClassDto convertTrainingClassToDto(TrainingClass trainingClass) {
        TrainingClassDto trainingClassDto = modelMapper
                .map(trainingClass, TrainingClassDto.class);
        trainingClassDto.setReservedPlaces(trainingClass.getReservations().size());
        return trainingClassDto;
    }

    public ReservationDto convertReservationToDtoAdminView(Reservation reservation) {
        ReservationDto reservationDto = modelMapper.map(reservation, ReservationDto.class);
        reservationDto.setUserDto(convertUserToDto(reservation.getUser()));
        reservationDto.setTrainingClassDto(null);
        return reservationDto;
    }

    private UserDto convertUserToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setPassword(null);
        return userDto;
    }

    public List<ReservationDto> convertReservationsToDtoListAdminView(List<Reservation> reservations) {
        return reservations.stream()
                .map(reservation -> convertReservationToDtoAdminView(reservation))
                .collect(Collectors.toList());
    }

    public List<ReservationDto> convertReservationsToDtoListUserView(List<Reservation> reservations) {
        return reservations.stream()
                .map(reservation -> convertReservationToDtoUserView(reservation))
                .collect(Collectors.toList());
    }

}
