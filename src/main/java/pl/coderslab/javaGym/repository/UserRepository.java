package pl.coderslab.javaGym.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.javaGym.entity.user.Role;
import pl.coderslab.javaGym.entity.user.User;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    Boolean existsByEmailIgnoreCase(String email);

    List<User> findAllByRolesIsNotContaining(Set<Role> roles);

    List<User> findAllByRolesIsContaining(Set<Role> roles);

    List<User> findAllByRolesIsNotContainingAndEmailIsContainingIgnoreCase(Set<Role> roles, String email);

    List<User> findAllByRolesIsContainingAndEmailIsContainingIgnoreCase(Set<Role> roles, String email);

    List<User> findAllByNewsletterIsTrue();

    List<User> findAllByRolesIsNotContainingAndFirstNameIsContainingAndLastNameIsContainingAllIgnoreCase
            (Set<Role> roles, String firstName, String lastName);

    List<User> findAllByRolesIsContainingAndFirstNameIsContainingAndLastNameIsContainingAllIgnoreCase
            (Set<Role> roles, String firstName, String lastName);

}
