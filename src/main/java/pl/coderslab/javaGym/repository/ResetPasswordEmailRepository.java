package pl.coderslab.javaGym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.javaGym.entity.email.ResetPasswordEmailDetails;

@Repository
public interface ResetPasswordEmailRepository
        extends JpaRepository<ResetPasswordEmailDetails, Long> {

    ResetPasswordEmailDetails findByUserId(Long id);

    ResetPasswordEmailDetails findByParam(String param);

}
