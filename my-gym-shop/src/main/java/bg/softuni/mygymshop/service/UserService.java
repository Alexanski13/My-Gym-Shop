package bg.softuni.mygymshop.service;

import bg.softuni.mygymshop.model.dtos.UserDTO;
import bg.softuni.mygymshop.model.dtos.UserRegistrationDTO;
import bg.softuni.mygymshop.model.entities.RoleEntity;
import bg.softuni.mygymshop.model.entities.UserEntity;
import bg.softuni.mygymshop.model.enums.RoleType;
import bg.softuni.mygymshop.repository.UserRepository;
import bg.softuni.mygymshop.repository.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       UserDetailsService userDetailsService,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
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
            var moderatorRole = new RoleEntity().setRole(RoleType.USER);
            var adminRole = new RoleEntity().setRole(RoleType.ADMIN);

            userRoleRepository.save(moderatorRole);
            userRoleRepository.save(adminRole);
        }
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            initAdmin();
//            initModerator();
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
                setRoles(new HashSet<>(userRoleRepository.findAll()));

        userRepository.save(adminUser);
    }

    private void initModerator() {

        var moderatorRole = userRoleRepository.
                findUserRoleEntityByRole(RoleType.USER).orElseThrow();

        var moderatorUser = new UserEntity().
                setUsername("Moderator").
                setPassword(passwordEncoder.encode("topsecret")).
                setFirstName("Moderator").
                setLastName("Moderatorov").
                setEmail("moderator@example.com").
                setAge(30).
                setRoles((Set<RoleEntity>) moderatorRole);

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

    // IMPLEMENTATION FOR ROLE MANAGEMENT

    public List<UserDTO> getUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(user -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return modelMapper.map(user, UserDTO.class);
    }

//    public UserDTO saveUser(UserDTO userDTO) {
//        UserEntity user = modelMapper.map(userDTO, User.class);
//        user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
//        User savedUser = userRepository.save(user);
//        return modelMapper.map(savedUser, UserDTO.class);
//    }

    public UserDTO updateUser(UserDTO userDTO) {
        UserEntity user = userRepository.findById(userDTO.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRoles(userDTO.getRoles().stream().map(roleDTO -> new RoleEntity()
                .setId(roleDTO.getId())
                .setRole(roleDTO.getName())).collect(Collectors.toSet()));
        UserEntity updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserDTO assignRoleToUser(Long userId, RoleType role) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.getRoles().add(new RoleEntity()
                .setRole(role));
        UserEntity updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDTO.class);
    }
}
