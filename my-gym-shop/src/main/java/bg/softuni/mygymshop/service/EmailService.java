package bg.softuni.mygymshop.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.UUID;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender javaMailSender,
                        TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendRegistrationEmail(String userEmail, String userName) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setFrom("gymbro@example.com");
            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setSubject("Greetings and salutations from GymNation!");
            mimeMessageHelper.setText(generateEmailText(userName), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateEmailText(String userName) {
        Context ctx = new Context();
        ctx.setLocale(Locale.getDefault());
        ctx.setVariable("userName", userName);

        String activationLink = "http://localhost:8080/activate?code=" + generateActivationCode();

        ctx.setVariable("activationLink", activationLink);

        return templateEngine.process("email/registration", ctx);
    }

    public String generateActivationCode() {
        return UUID.randomUUID().toString();
    }
}