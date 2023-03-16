package bg.softuni.mygymshop.model.config;

import bg.softuni.mygymshop.repository.UserRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


// 1. add spring security starter dependency
// 2. Define the following beans
//    a. SecurityFilterChain -> here we describe authnetication rules, how to perform login/logout etc.
//    b. PasswordEncoder -> used when saving a User in the DB to hash the password,
//                          spring security uses it to validate password when logging
//    c. UserDetailsService -> shows Spring how to fetch the user from the db, wraps the User entity into a UserDetails interface impl
// 3. add thymeleaf security to hide restricted elements
// 4. use Principal object (which we get as an argument in the controller methods)

@Configuration
public class SecurityConfiguration {
    private final UserRepository userRepository;

    public SecurityConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/", "/routes/**", "/api/**").permitAll()
                .requestMatchers("/users/login", "/users/register").anonymous()
                .requestMatchers("/users/profile").authenticated()
                .and()
                .formLogin()
                .loginPage("/users/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .failureForwardUrl("/users/login?error=true")
                .and()
                .logout()
                .logoutUrl("/users/logout")
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/");

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
