package com.yener.fistikhotel.service.impl;

import com.yener.fistikhotel.exception.UserAlreadyExistsException;
import com.yener.fistikhotel.exception.UserException;
import com.yener.fistikhotel.model.Role;
import com.yener.fistikhotel.model.User;
import com.yener.fistikhotel.repository.RoleRepository;
import com.yener.fistikhotel.repository.UserRepository;
import com.yener.fistikhotel.service.RoleService;
import com.yener.fistikhotel.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setPassword("password");
        user.setEmail("test@example.com");
    }

    @Test
    void registerUser_shouldSaveUserWithEncodedPasswordAndRole() {

        when(userRepository.save(any(User.class))).thenReturn(user);
        User registeredUser = userService.registerUser(user);

        assertEquals(user.getEmail(), registeredUser.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_shouldThrowUserAlreadyExistsException_whenEmailExists() {

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(user));
        verify(userRepository).existsByEmail("test@example.com");
    }

    @Test
    void getUsers_shouldReturnListOfUsers() {

        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> retrievedUsers = userService.getUsers();

        assertEquals(users, retrievedUsers);
        verify(userRepository).findAll();
    }

    @Test
    void deleteUser_shouldDeleteUserByEmail() {

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        userService.deleteUser(user.getEmail());

        verify(userRepository).deleteByEmail(user.getEmail());
    }

    @Test
    void getUser_shouldReturnUserByEmail() {

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        User retrievedUser = userService.getUser(user.getEmail());

        assertEquals(user, retrievedUser);
        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    void getUser_shouldThrowUsernameNotFoundException_whenUserNotFound() {

        String nonExistentEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUser(nonExistentEmail));
        verify(userRepository).findByEmail(nonExistentEmail);
    }

    @Test
    void findById_shouldReturnUserById() {


        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User retrievedUser = userService.findById(1L);

        assertEquals(user, retrievedUser);
        verify(userRepository).findById(1L);
    }

    @Test
    void findById_shouldThrowUserException_whenUserNotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userService.findById(1L));
        verify(userRepository).findById(1L);
    }
}