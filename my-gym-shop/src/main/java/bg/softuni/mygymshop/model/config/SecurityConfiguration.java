package bg.softuni.mygymshop.model.config;

import bg.softuni.mygymshop.model.enums.RoleType;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;


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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           SecurityContextRepository securityContextRepository) throws Exception {
        http.
                // defines which pages will be authorized
                        authorizeHttpRequests().
                // allow access to all static files (images, CSS, js)
                        requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll().
                // the URL-s below are available for all users - logged in and anonymous
                        requestMatchers("/", "/users/login", "/users/register").permitAll().
                // only for admins
                        requestMatchers("/pages/admins").hasRole(RoleType.ADMIN.name()).
                anyRequest().authenticated().
                and().
                // configure login with HTML form
                        formLogin().
                loginPage("/users/login").permitAll().
                // the names of the user name, password input fields in the custom login form
                        usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY).
                passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY).
                // where do we go after login
                        defaultSuccessUrl("/index").//use true argument if you always want to go there, otherwise go to previous page
                and().logout().//configure logout
                logoutUrl("/users/logout").
                logoutSuccessUrl("/").//go to homepage after logout
                invalidateHttpSession(true).and().securityContext().securityContextRepository(securityContextRepository);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }
}
