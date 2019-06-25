package pl.coderslab.javaGym.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.coderslab.javaGym.email.EmailSender;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.repository.UserRepository;

@RestController
public class AccessTestController {

    private EmailSender emailSender;
    private UserRepository userRepository;

    public AccessTestController(EmailSender emailSender,
                                UserRepository userRepository) {
        this.emailSender = emailSender;
        this.userRepository = userRepository;
    }

    @GetMapping("/user/user-content")
    @ResponseBody
    public String userContent() {
        String userContent = "<h2>User content</h2>";
        String homepage = "<p><a href='/'>homepage</a></p>";
        return userContent + homepage;
    }

    @GetMapping("/admin/admin-content")
    @ResponseBody
    public String adminContent() {
        String adminContent = "<h2>Admin content</h2>";
        String homepage = "<p><a href='/'>homepage</a></p>";
        return adminContent + homepage;
    }

    @GetMapping(value = "/", produces = "text/html")
    @ResponseBody
    public String homepage() {
        String homepage = "<h2>homepage</h2>";
        String userContent = "<p><a href='/user/user-content'>user content</a></p>";
        String adminContent = "<p><a href='/admin/admin-content'>admin content</a></p>";
        String login = "<p><a href='/login'>login</a></p>";
        String logout = "<form action='/logout' method='post'><input type='submit' value='logout'/></form>";
        return homepage + userContent + adminContent + login + logout;
    }

    @GetMapping("/test")
    public String test() {
        User user = userRepository.findById(4L).orElse(null);
        emailSender.sendAccountActivationEmail(user);
        return "aaaa";
    }

}
