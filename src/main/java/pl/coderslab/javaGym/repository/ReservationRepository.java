package pl.coderslab.javaGym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.coderslab.javaGym.entity.data.Reservation;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByTrainingClassIdOrderByReservationTimeAsc(Long id);

    Boolean existsByUserIdAndTrainingClassId(Long userId, Long trainingClassId);

    Reservation findByUserIdAndTrainingClassId(Long userId, Long trainingClassId);

    Reservation findFirstByTrainingClassIdAndOnTrainingListIsFalseOrderByReservationTimeAsc
            (Long trainingClassId);

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.trainingClass.startDate > :nowTime")
    List<Reservation> findAllFutureClassReservationsByUserId
            (@Param("userId") Long userId, @Param("nowTime")LocalDateTime nowTime);

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.trainingClass.startDate < :nowTime")
    List<Reservation> findAllPastClassReservationsByUserId
            (@Param("userId")Long id, @Param("nowTime") LocalDateTime nowTime);
}
