package pl.coderslab.javaGym.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.javaGym.entity.user.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query("SELECT u.email FROM User u")
    List<String> getAllUsersEmails();

    Boolean existsByEmail(String email);

}
