package pl.coderslab.javaGym.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.javaGym.service.emailService.ResetPasswordEmailService;
import pl.coderslab.javaGym.service.userService.UserService;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Controller
@RequestMapping("/reset-password")
@Validated
public class ResetPasswordController {

    private UserService userService;
    private ResetPasswordEmailService resetPasswordEmailService;

    public ResetPasswordController(UserService userService,
                        ResetPasswordEmailService resetPasswordEmailService) {
        this.userService = userService;
        this.resetPasswordEmailService = resetPasswordEmailService;
    }

    @PostMapping(value = "/request", produces = "text/html")
    @ResponseBody
    public String sendResetPasswordRequest(@RequestParam
               @Email(message = "*Please provide a valid email.")
               @NotBlank(message = "*Please provide an email.")
               String userEmail) {
        Boolean result = userService.resetUserPassword(userEmail);
        return result.toString();
    }

    @GetMapping("/show-form")
    public String showResetPasswordForm(@RequestParam @NotBlank String param, Model model) {
        model.addAttribute("param", param);
        return "reset-password";
    }

    @PostMapping("/reset")
    @ResponseBody
    public Boolean resetPassword(@RequestParam @NotBlank String param,
             @Size(min = 5, message = "*Your password must have at least 5 characters.")
             @NotBlank(message = "*Please provide your password.")
             String password) {
        return resetPasswordEmailService.resetPassword(param, password);
    }
}
