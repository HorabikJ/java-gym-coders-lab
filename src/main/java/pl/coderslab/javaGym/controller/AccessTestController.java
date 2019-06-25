package pl.coderslab.javaGym.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.coderslab.javaGym.email.EmailSender;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.repository.UserRepository;

@Controller
public class AccessTestController {

    private EmailSender emailSender;
    private UserRepository userRepository;

    public AccessTestController(EmailSender emailSender,
                                UserRepository userRepository) {
        this.emailSender = emailSender;
        this.userRepository = userRepository;
    }

    @GetMapping("/user/user-content")
    public String userContent() {
        return "user-content";
    }

    @GetMapping("/admin/admin-content")
    public String adminContent() {
        return "admin-content";
    }

    @GetMapping("/")
    public String homepage() {
        return "access-test";
    }

    @GetMapping("/test")
    public String test() {
        User user = userRepository.findById(4L).orElse(null);
        emailSender.sendAccountActivationEmail(user);
        return "aaaa";
    }

}
