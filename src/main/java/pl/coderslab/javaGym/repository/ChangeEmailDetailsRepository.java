package pl.coderslab.javaGym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.javaGym.entity.confirmationEmail.ChangeEmailDetails;

@Repository
public interface ChangeEmailDetailsRepository
        extends JpaRepository<ChangeEmailDetails, Long> {

    ChangeEmailDetails findByUserId(Long id);

    ChangeEmailDetails findByParam(String param);

}
