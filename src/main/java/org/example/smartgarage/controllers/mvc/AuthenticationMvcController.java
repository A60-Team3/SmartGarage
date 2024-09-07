package org.example.smartgarage.controllers.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.smartgarage.dtos.request.PasswordResetDto;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.VerificationToken;
import org.example.smartgarage.services.contracts.AuthenticationService;
import org.example.smartgarage.services.contracts.UserService;
import org.example.smartgarage.services.contracts.VerificationTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/garage")
public class AuthenticationMvcController {
    private final UserService userService;
    private final VerificationTokenService tokenService;
    private final AuthenticationService authenticationService;

    public AuthenticationMvcController(UserService userService, VerificationTokenService tokenService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.authenticationService = authenticationService;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @PostMapping("/password")
    public String resetPasswordEmail(@RequestParam("phoneNumber") String phoneNumber,
                                     HttpServletRequest request) {
        UserEntity existingUser = userService.findByPhoneNumber(phoneNumber);
        if (existingUser == null) {
            return "redirect:/garage/login?user=false";
        }

        authenticationService.sendResetEmail(existingUser, request);
        return "redirect:/garage/login?email=true";
    }

    @GetMapping("/password/{userId}")
    public String getResetPasswordPage(@RequestParam("token") String token,
                                       @PathVariable long userId,
                                       Model model) {

        VerificationToken dbToken = tokenService.findByToken(token);

        if (dbToken == null || dbToken.isExpired()) {
            model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.getReasonPhrase());
            model.addAttribute("error", "Password reset token expired. Try again.");
            return "error-page";
        }

        UserEntity user = userService.findByUsername(dbToken.getUser().getUsername()).orElse(null);

        if (user == null) {
            model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.getReasonPhrase());
            model.addAttribute("error", "User doesnt exist");
            return "error-page";
        }

        if (userId != user.getId()) {
            model.addAttribute("statusCode", HttpStatus.FORBIDDEN.getReasonPhrase());
            model.addAttribute("error", "Cannot change other people passwords");
            return "error-page";
        }

        tokenService.removeToken(dbToken);
        model.addAttribute("passwordDto", new PasswordResetDto());

        return "password-reset";
    }

    @PostMapping("/password/{userId}")
    public String resetPassword(@PathVariable long userId,
                                @Valid @ModelAttribute("passwordDto") PasswordResetDto dto,
                                BindingResult bindingResult,
                                Model model,
                                HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "password-reset";
        }

        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "password_error", "Password confirmation should match password.");
            return "password-reset";
        }

        authenticationService.resetPassword(dto.getPassword(), userId, request);

        return "redirect:/garage/login?reset=true";
    }
}
