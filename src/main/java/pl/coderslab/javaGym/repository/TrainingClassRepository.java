package pl.coderslab.javaGym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.coderslab.javaGym.entity.data.TrainingClass;
import pl.coderslab.javaGym.entity.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainingClassRepository extends JpaRepository<TrainingClass, Long> {

    List<TrainingClass> findAllByStartDateIsAfter(LocalDateTime startDate);

    List<TrainingClass> findAllByStartDateIsBefore(LocalDateTime startDate);

    List<TrainingClass> findAllByClassGroupId(String classGroupId);

    List<TrainingClass> findAllByClassGroupIdAndStartDateIsAfter(String classGroupId, LocalDateTime startDate);

    List<TrainingClass> findAllByClassGroupIdIsNotAndStartDateIsAfter(String classGroupId, LocalDateTime startDate);

    TrainingClass findByIdAndStartDateIsAfter(Long id, LocalDateTime startDate);

    List<TrainingClass> findByIdIsNotAndStartDateIsAfter(Long id, LocalDateTime startDate);

    @Query("SELECT t FROM TrainingClass t WHERE t.startDate > :startDate AND (t.instructor = null OR t.trainingType = null )")
    List<TrainingClass> findAllTrainingClassesInFutureWhereAnyRelationIsNull(@Param("startDate")LocalDateTime startDate);

    @Query("SELECT t.customers FROM TrainingClass t WHERE t.id = :classId")
    List<User> findAllUsersOnClassReservationListForByClassId(@Param("classId") Long classId);

    @Query("SELECT t.awaitingCustomers FROM TrainingClass t WHERE t.id = :classId")
    List<User> findAllUsersOnClassAwaitingListForByClassId(@Param("classId") Long classId);

    List<TrainingClass> findAllByInstructorIdAndStartDateIsAfter(Long id, LocalDateTime startDate);

    List<TrainingClass> findAllByTrainingTypeIdAndStartDateIsAfter(Long id, LocalDateTime startDate);

}
