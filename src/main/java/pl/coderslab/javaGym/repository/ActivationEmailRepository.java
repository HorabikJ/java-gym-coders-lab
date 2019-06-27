package pl.coderslab.javaGym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.javaGym.entity.confirmationEmail.ActivationEmailDetails;

@Repository
public interface ActivationEmailRepository
        extends JpaRepository<ActivationEmailDetails, Long> {

    ActivationEmailDetails findByUserId(Long id);

    ActivationEmailDetails findByParam(String param);



}
