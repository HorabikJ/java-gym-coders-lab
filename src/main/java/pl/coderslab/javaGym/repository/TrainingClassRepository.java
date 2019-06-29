package pl.coderslab.javaGym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.javaGym.entity.data.TrainingClass;

import java.util.List;

@Repository
public interface TrainingClassRepository extends JpaRepository<TrainingClass, Long> {

    List<TrainingClass> findAllByUniqueClassId(String uniqueClassId);

    List<TrainingClass> findAllByInstructorId(Long instructorId);

}
