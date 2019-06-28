package pl.coderslab.javaGym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.javaGym.entity.data.Instructor;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    Boolean existsByEmail(String email);

}
