package bg.softuni.mygymshop.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Controller
public class LocaleController {

    @PostMapping("/locale")
    public String changeLocale(@RequestParam("lang") String lang, HttpServletRequest request) {
        request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, new Locale(lang));
        return "redirect:/";
    }
}
