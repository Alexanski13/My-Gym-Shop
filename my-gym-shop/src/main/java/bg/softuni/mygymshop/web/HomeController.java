package bg.softuni.mygymshop.web;

import bg.softuni.mygymshop.model.AppUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/index")
    public String home(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model) {

        if (appUserDetails != null) {
            model.addAttribute("fullName", appUserDetails.getFullName());
        }

        return "index";
    }

}
