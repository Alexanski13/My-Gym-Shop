package bg.softuni.mygymshop.web;

import bg.softuni.mygymshop.model.dtos.UserRegistrationDTO;
import bg.softuni.mygymshop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final SecurityContextRepository securityContextRepository;

    @Autowired
    public UserController(UserService userService, SecurityContextRepository securityContextRepository) {
        this.userService = userService;
        this.securityContextRepository = securityContextRepository;
    }

    @ModelAttribute("userRegistrationDTO")
    public UserRegistrationDTO initForm() {
        return new UserRegistrationDTO();
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(UserRegistrationDTO userRegistrationDTO,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        System.out.println(userRegistrationDTO.toString());

        userService.registerUser(userRegistrationDTO, successfulAuth -> {

            SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();

            SecurityContext context = strategy.createEmptyContext();
            context.setAuthentication(successfulAuth);

            strategy.setContext(context);

            securityContextRepository.saveContext(context, request, response);
        });

        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

//    @GetMapping("/profile") NOT YET IMPLEMENTED!
//    public String profile(Principal principal, Model model) {
//        String username = principal.getName();
//        UserEntity user = userService.getUser(username);
//
//        UserProfileView userProfileView = new UserProfileView(
//                username,
//                user.getEmail(),
//                user.getFirstName(),
//                user.getLastName(),
//                user.getAge()
//        );
//
//        model.addAttribute("user", userProfileView);
//
//        return "profile";
//    }
}
