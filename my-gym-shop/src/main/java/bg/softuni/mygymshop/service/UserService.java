package bg.softuni.mygymshop.service;

import bg.softuni.mygymshop.model.dtos.user.UserDTO;
import bg.softuni.mygymshop.model.dtos.user.UserRegistrationDTO;
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

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final UserRoleRepository roleRepository;

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       UserRoleRepository roleRepository, UserDetailsService userDetailsService,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
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

    @PostConstruct
    public void init() {
        initRoles();
        initUsers();
    }

    private void initRoles() {
        if (userRoleRepository.count() == 0) {
            var userRole = new RoleEntity().setRole(RoleType.USER);
            var adminRole = new RoleEntity().setRole(RoleType.ADMIN);

            userRoleRepository.save(userRole);
            userRoleRepository.save(adminRole);
        }
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            initAdmin();
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

    public UserDTO getUserById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO updateUser(UserDTO userDTO) {
        UserEntity user = userRepository.findById(userDTO.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRoles(userDTO.getRoles().stream().map(roleDTO -> new RoleEntity()
                .setId(roleDTO.getId())
                .setRole(roleDTO.getRole())).collect(Collectors.toSet()));
        UserEntity updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserDTO assignRoleToUser(Long userId, RoleType role, boolean addRole) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Optional<RoleEntity> existingRole = roleRepository.findByRole(role);

        if (existingRole.isPresent()) {
            if (addRole) {
                if (user.getRoles().stream().noneMatch(r -> r.getRole() == role)) {
                    user.getRoles().add(existingRole.get());
                }
            } else {
                user.getRoles().removeIf(r -> r.getRole() == role);
            }
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Role does not exist!");
        }

        return modelMapper.map(user, UserDTO.class);
    }

    public UserEntity getUserByActivationCode(String activationCode) {
        return userRepository.findByActivationCode(activationCode);
    }

    public void saveUser(UserEntity user) {
        userRepository.saveAndFlush(user);
    }
}