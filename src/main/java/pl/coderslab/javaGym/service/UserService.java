package pl.coderslab.javaGym.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.coderslab.javaGym.entity.Role;
import pl.coderslab.javaGym.entity.User;
import pl.coderslab.javaGym.enumClass.RoleEnum;
import pl.coderslab.javaGym.error.customException.DomainObjectException;
import pl.coderslab.javaGym.repository.RoleRepository;
import pl.coderslab.javaGym.repository.UserRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<String> getAllUsersEmails() {
        return userRepository.getAllUsersEmails();
    }

    public User saveAsUser(User user) {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setActive(1);
            Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER.toString());
            user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DomainObjectException();
        }
    }

    public User saveAsAdmin(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER.toString());
        Role adminRole = roleRepository.findByRole(RoleEnum.ROLE_ADMIN.toString());
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole, adminRole)));
        return userRepository.save(user);
    }

}
