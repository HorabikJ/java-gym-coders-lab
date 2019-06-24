package pl.coderslab.javaGym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetPasswordEmailDetailsRepository
        extends JpaRepository<ResetPasswordEmailDetailsRepository, Long> {
}
