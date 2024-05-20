package com.yener.fistikhotel.service.impl;

import com.yener.fistikhotel.model.Role;
import com.yener.fistikhotel.model.User;
import com.yener.fistikhotel.repository.RoleRepository;
import com.yener.fistikhotel.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {
    @Mock
    RoleRepository roleRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    RoleServiceImpl roleServiceImpl;

    Role role;
    Role secRole;

    User user;

    List<Role> roles;
    List<User> users;

    Optional<Role> optionalRole;
    Optional<Role> secOptionalRole;

    Optional<User> optionalUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        role=new Role("USER");
        secRole=new Role("ADMIN");
        secOptionalRole=Optional.of(secRole);
        user=new User();
        roles=new ArrayList<>();
        users=new ArrayList<>();
        optionalRole= Optional.of(role);
        optionalUser=Optional.of(user);
        user.setId(1L);
        user.setRoles(roles);
        role.setId(1L);
        secRole.setId(1L);
        role.setUsers(users);
        roles.add(role);
        users.add(user);

    }

    @Test
    void testGetRoles() {
        when(roleRepository.findAll()).thenReturn(roles);
        List<Role> result = roleServiceImpl.getRoles();
        assertEquals(roles, result);

    }

    @Test
    void testCreateRole() {
        when(roleRepository.existsByName("ADMIN")).thenReturn(false);
        when(roleRepository.save(role)).thenReturn(role);
        when(roleServiceImpl.createRole(role)).thenReturn(role);

        Role result = roleServiceImpl.createRole(role);
        assertEquals(role, result);
    }

    @Test
    void testDeleteRole() {

        roleRepository.findById(1L);
        when(roleRepository.findById(1L)).thenReturn(optionalRole);
        roleServiceImpl.findRoleById(1L);
        roleServiceImpl.deleteRole(1L);

        when(roleRepository.save(role)).thenReturn(role);
        verify(roleRepository).deleteById(1L);
    }

    @Test
    void testFindByName() {
        when(roleRepository.findByName(anyString())).thenReturn(optionalRole);

        Role result = roleServiceImpl.findByName("USER");
        assertEquals(role, result);
    }

    @Test
    void testRemoveUserFromRole() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(roleRepository.findById(1L)).thenReturn(optionalRole);
        when(roleRepository.save(role)).thenReturn(role);

        User result = roleServiceImpl.removeUserFromRole(1L,1L);
        assertEquals(user, result);

        assertThat(result).isEqualTo(user);
        assertThat(role.getUsers()).isEmpty();
        verify(roleRepository).save(role);
    }

    @Test
    void testAssignRoleToUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(roleRepository.findById(1L)).thenReturn(secOptionalRole);
        when(roleRepository.save(role)).thenReturn(role);

        User result = roleServiceImpl.assignRoleToUser(1L, 1L);
        assertThat(result).isEqualTo(user);
    }

    @Test
    void testRemoveAllUsersFromRole() {
        when(roleRepository.findById(1L)).thenReturn(optionalRole);
        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleServiceImpl.removeAllUsersFromRole(1L);
        assertEquals(role, result);
    }

    @Test
    void testFindRoleById() {
        when(roleRepository.findById(1L)).thenReturn(optionalRole);
        Role result = roleServiceImpl.findRoleById(Long.valueOf(1));
        assertEquals(role, result);
    }
}
