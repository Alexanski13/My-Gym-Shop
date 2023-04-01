package bg.softuni.mygymshop.service;

import bg.softuni.mygymshop.model.dtos.UserDTO;
import bg.softuni.mygymshop.model.dtos.UserRegistrationDTO;
import bg.softuni.mygymshop.model.entities.RoleEntity;
import bg.softuni.mygymshop.model.entities.UserEntity;
import bg.softuni.mygymshop.model.enums.RoleType;
import bg.softuni.mygymshop.model.views.UserProfileViewDTO;
import bg.softuni.mygymshop.repository.UserRepository;
import bg.softuni.mygymshop.repository.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       UserDetailsService userDetailsService,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public void registerUser(UserRegistrationDTO userRegistrationDTO,
                             Consumer<Authentication> successfulLoginProcessor) {
        UserEntity userEntity = new UserEntity().setFirstName(userRegistrationDTO.getFirstname())
                .setLastName(userRegistrationDTO.getLastname())
                .setEmail(userRegistrationDTO.getEmail())
                .setAge(userRegistrationDTO.getAge())
                .setUsername(userRegistrationDTO.getUsername())
                .setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));

        userRepository.save(userEntity);

        emailService.sendRegistrationEmail(userEntity.getEmail(),
                userEntity.getFirstName() + " " + userEntity.getLastName());

//        var userDetails = userDetailsService.loadUserByUsername(userRegistrationDTO.getEmail());
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(
//                userDetails,
//                userDetails.getPassword(),
//                userDetails.getAuthorities()
//        );
//
//        successfulLoginProcessor.accept(authentication);

    }

    public UserEntity getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " was not found!"));
    }

    @PostConstruct
    public void init() {
        initRoles();
        initUsers();
    }

    private void initRoles() {
        if (userRoleRepository.count() == 0) {
            var moderatorRole = new RoleEntity().setRole(RoleType.MODERATOR);
            var adminRole = new RoleEntity().setRole(RoleType.ADMIN);

            userRoleRepository.save(moderatorRole);
            userRoleRepository.save(adminRole);
        }
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            initAdmin();
            initModerator();
            initNormalUser();
        }
    }

    private void initAdmin() {
        var adminUser = new UserEntity().
                setUsername("Admin").
                setPassword(passwordEncoder.encode("topsecret")).
                setFirstName("Admin").
                setLastName("Adminov").
                setEmail("admin@example.com").
                setAge(32).
                setRoles(userRoleRepository.findAll());

        userRepository.save(adminUser);
    }

    private void initModerator() {

        var moderatorRole = userRoleRepository.
                findUserRoleEntityByRole(RoleType.MODERATOR).orElseThrow();

        var moderatorUser = new UserEntity().
                setUsername("Moderator").
                setPassword(passwordEncoder.encode("topsecret")).
                setFirstName("Moderator").
                setLastName("Moderatorov").
                setEmail("moderator@example.com").
                setAge(30).
                setRoles(List.of(moderatorRole));

        userRepository.save(moderatorUser);
    }

    private void initNormalUser() {

        var normalUser = new UserEntity().
                setUsername("User").
                setPassword(passwordEncoder.encode("topsecret")).
                setFirstName("User").
                setLastName("Userov").
                setEmail("user@example.com").
                setAge(30);

        userRepository.save(normalUser);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::map).collect(Collectors.toList());
    }

    private UserDTO map(UserEntity userEntity) {
        return new UserDTO()
                .setId(userEntity.getId())
                .setEmail(userEntity.getEmail())
                .setAge(userEntity.getAge())
                .setUsername(userEntity.getUsername())
                .setFirstName(userEntity.getFirstName())
                .setLastName(userEntity.getLastName());
    }


    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }
}
