package pl.coderslab.javaGym.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.coderslab.javaGym.entity.user.Role;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.enumClass.RoleEnum;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query("SELECT u.email FROM User u")
    List<String> getAllUsersEmails();

    Boolean existsByEmail(String email);

    List<User> findAllByRolesIsNotContaining(Set<Role> roles);

    List<User> findAllByRolesIsContaining(Set<Role> roles);

    List<User> findAllByRolesIsNotContainingAndEmailIsContaining(Set<Role> roles, String email);

    List<User> findAllByRolesIsContainingAndEmailIsContaining(Set<Role> roles, String email);

    List<User> findAllByNewsletterIsTrue();

}
