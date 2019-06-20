package pl.coderslab.javaGym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.javaGym.entity.TrainingType;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {

}
