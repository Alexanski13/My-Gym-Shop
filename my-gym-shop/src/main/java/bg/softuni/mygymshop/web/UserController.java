package bg.softuni.mygymshop.web;

import bg.softuni.mygymshop.model.dtos.UserDTO;
import bg.softuni.mygymshop.model.dtos.UserRegistrationDTO;
import bg.softuni.mygymshop.model.entities.UserEntity;
import bg.softuni.mygymshop.model.enums.RoleType;
import bg.softuni.mygymshop.model.views.UserProfileViewDTO;
import bg.softuni.mygymshop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

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

    @GetMapping("/activate?code={code}")
    public String accountActivation(@PathVariable String code){
        //TODO: Code switches user to Active;
        return "activation-success";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
            RedirectAttributes redirectAttributes
    ) {

        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        redirectAttributes.addFlashAttribute("bad_credentials", true);

        return "redirect:/users/login";
    }

    @GetMapping("/profile")
    public String getUserProfileDetails(Principal principal, Model model) {
        String username = principal.getName();
        UserEntity user = userService.getUser(username);

        UserProfileViewDTO userProfileView = new UserProfileViewDTO()
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setAge(user.getAge())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName());

        model.addAttribute("userProfile", userProfileView);

        return "profile";
    }

//IMPLEMENT USER MANAGEMENT

    @GetMapping("/admin/manage")
    public String getUsers(Model model) {
        List<UserDTO> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users-list";
    }

    @GetMapping("/admin/manage/{id}")
    public String getUser(@PathVariable("id") Long id, Model model) {
        UserDTO user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user-detail";
    }

    @GetMapping("/admin/{id}/edit")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        UserDTO user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user-edit";
    }

    @PostMapping("/admin/{id}/edit")
    public String editUser(@PathVariable("id") Long id, @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "user-edit";
        }
        userDTO.setId(id);
        UserDTO updatedUser = userService.updateUser(userDTO);
        return "redirect:/users/admin/manage/" + updatedUser.getId();
    }

    @GetMapping("/admin/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users/admin/manage";
    }

    @GetMapping("/admin/{id}/assign-role")
    public String assignRoleForm(@PathVariable("id") Long id, Model model) {
        UserDTO user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", RoleType.values());
        return "assign-role";
    }

    @PostMapping("/admin/{id}/assign-role")
    public String assignRole(@PathVariable("id") Long id, @RequestParam("role") RoleType role, Model model) {
        userService.assignRoleToUser(id, role);
        return "redirect:/users/admin/manage/" + id;
    }
}
