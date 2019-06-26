package pl.coderslab.javaGym.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.coderslab.javaGym.entity.user.User;
import pl.coderslab.javaGym.service.userService.UserService;

@Controller
public class AccessTestController {

    private UserService userService;

    public AccessTestController(UserService userService) {
        this.userService = userService;
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

    @GetMapping("/super/super-admin-content")
    public String superAdminContent() {
        return "super-admin-content";
    }

    @ModelAttribute("loggedUser")
    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.findUserByEmail(authentication.getName());
    }


//    @GetMapping("/test")
//    public String test() {
//        User user = userRepository.findById(4L).orElse(null);
//        emailSender.sendAccountActivationEmail(user);
//        return "aaaa";
//    }

}
