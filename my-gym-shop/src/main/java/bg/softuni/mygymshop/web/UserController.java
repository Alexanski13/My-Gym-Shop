package bg.softuni.mygymshop.web;

import bg.softuni.mygymshop.model.dtos.UserRegistrationDTO;
import bg.softuni.mygymshop.model.entities.UserEntity;
import bg.softuni.mygymshop.model.views.UserProfileView;
import bg.softuni.mygymshop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
    public String doRegister(@Valid UserRegistrationDTO userRegistrationDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        System.out.println(userRegistrationDTO.toString());

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegistrationDTO", userRegistrationDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationDTO", bindingResult);

            return "redirect:/register";
        }

        this.userService.register(userRegistrationDTO);

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
