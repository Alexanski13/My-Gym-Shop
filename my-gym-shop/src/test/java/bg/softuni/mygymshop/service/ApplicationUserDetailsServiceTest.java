package bg.softuni.mygymshop.service;

import bg.softuni.mygymshop.model.AppUserDetails;
import bg.softuni.mygymshop.model.entities.RoleEntity;
import bg.softuni.mygymshop.model.entities.UserEntity;
import bg.softuni.mygymshop.model.enums.RoleType;
import bg.softuni.mygymshop.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationUserDetailsServiceTest {

    @Mock
    private UserRepository mockUserRepo;

    private ApplicationUserDetailsService toTest;

    @BeforeEach
    void setUp() {
        toTest = new ApplicationUserDetailsService(
                mockUserRepo
        );
    }

    @Test
    void testLoadUserByUsername_UserExists() {

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("tester");
        userEntity.setPassword("password");
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRole(RoleType.ADMIN);
        userEntity.setRoles(Set.of(roleEntity));
        when(mockUserRepo.findUserEntityByUsername("tester")).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = toTest.loadUserByUsername("tester");
        assertNotNull(toTest);
        assertEquals("tester", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertEquals(Set.of(new SimpleGrantedAuthority("ROLE_ADMIN")), userDetails.getAuthorities());
        assertEquals("John Doe", ((AppUserDetails) userDetails).getFullName());
    }

    @Test
    void testLoadUserByUsername_UserDoesNotExist() {
        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> toTest.loadUserByUsername("non-existant@example.com")
        );
    }
}
