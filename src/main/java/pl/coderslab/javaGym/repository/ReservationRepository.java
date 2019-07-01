package pl.coderslab.javaGym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.javaGym.entity.data.Reservation;

import java.util.LinkedList;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    LinkedList<Reservation> findAllByTrainingClassIdOrderByReservationTimeAsc(Long id);

    Boolean existsByUserIdAndTrainingClassId(Long userId, Long trainingClassId);

    Reservation findByUserIdAndTrainingClassId(Long userId, Long trainingClassId);

    Reservation findFirstByTrainingClassIdAndOnTrainingListIsFalseOrderByReservationTimeAsc
            (Long trainingClassId);

}
