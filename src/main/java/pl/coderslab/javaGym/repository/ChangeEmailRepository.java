package pl.coderslab.javaGym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.javaGym.entity.email.ChangeEmailDetails;

@Repository
public interface ChangeEmailRepository
        extends JpaRepository<ChangeEmailDetails, Long> {

}
