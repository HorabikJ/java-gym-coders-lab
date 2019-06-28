package pl.coderslab.javaGym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.javaGym.entity.data.Instructor;

import java.util.List;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    Boolean existsByEmailIgnoreCase(String email);

    List<Instructor> findAllByEmailIsContainingIgnoreCase(String email);

    List<Instructor> findAllByFirstNameIsContainingAndLastNameIsContainingAllIgnoreCase(String firstName, String lastName);

}
