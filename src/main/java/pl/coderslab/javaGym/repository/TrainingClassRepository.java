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

    List<TrainingClass> findAllByStartDateIsAfterOrderByStartDateAsc(LocalDateTime startDate);

    List<TrainingClass> findAllByStartDateIsBeforeOrderByStartDateAsc(LocalDateTime startDate);

    List<TrainingClass> findAllByClassGroupIdOrderByStartDateAsc(String classGroupId);

    List<TrainingClass> findAllByClassGroupIdAndStartDateIsAfterOrderByStartDateAsc(String classGroupId, LocalDateTime startDate);

    List<TrainingClass> findAllByClassGroupIdIsNotAndStartDateIsAfterOrderByStartDateAsc(String classGroupId, LocalDateTime startDate);

    TrainingClass findByIdAndStartDateIsAfter(Long id, LocalDateTime startDate);

    List<TrainingClass> findByIdIsNotAndStartDateIsAfterOrderByStartDateAsc(Long id, LocalDateTime startDate);

    @Query("SELECT t FROM TrainingClass t WHERE t.startDate > :startDate AND " +
            "(t.instructor IS NULL OR t.trainingType IS NULL) ORDER BY t.startDate ASC")
    List<TrainingClass> findAllTrainingClassesInFutureWhereAnyRelationIsNull(@Param("startDate")LocalDateTime startDate);

    List<TrainingClass> findAllByInstructorIdAndStartDateIsAfterOrderByStartDateAsc(Long id, LocalDateTime startDate);

    List<TrainingClass> findAllByTrainingTypeIdAndStartDateIsAfterOrderByStartDateAsc(Long id, LocalDateTime startDate);

    @Query("SELECT t FROM TrainingClass t WHERE t.instructor IS NOT NULL AND t.trainingType IS NOT NULL " +
            "AND t.startDate BETWEEN :showStartDate AND :showEndDate ORDER BY t.startDate ASC")
    List<TrainingClass> findAllTrainingClassesAvailableForUsers(@Param("showStartDate") LocalDateTime showStartDate,
                                                       @Param("showEndDate") LocalDateTime showEndDate);

    @Query("SELECT t FROM TrainingClass t WHERE t.instructor IS NOT NULL AND t.trainingType IS NOT NULL " +
            "AND t.startDate BETWEEN :showStartDate AND :showEndDate AND t.id = :classId")
    TrainingClass findTrainingClassByIdAvailableForUser(@Param("showStartDate") LocalDateTime showStartDate,
                                                        @Param("showEndDate") LocalDateTime showEndDate,
                                                        @Param("classId") Long classId);

}
