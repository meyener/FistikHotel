package com.yener.fistikhotel.service.impl;

import com.yener.fistikhotel.exception.RoleAlreadyExistException;
import com.yener.fistikhotel.exception.UserAlreadyExistsException;
import com.yener.fistikhotel.exception.UserException;
import com.yener.fistikhotel.model.Role;
import com.yener.fistikhotel.model.User;
import com.yener.fistikhotel.repository.RoleRepository;
import com.yener.fistikhotel.repository.UserRepository;
import com.yener.fistikhotel.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role createRole(Role theRole) {
        String roleName = "ROLE_" + theRole.getName().toUpperCase();
        Role role = new Role(roleName);
        if (roleRepository.existsByName(roleName)) {
            throw new RoleAlreadyExistException(theRole.getName() + " role already exists");
        }
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long roleId) {
        this.removeAllUsersFromRole(roleId);
        roleRepository.deleteById(roleId);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new RuntimeException("Error"));
    }

    @Override
    public User removeUserFromRole(Long userId, Long roleId) {
        User user = getUser(userId);
        Role role = findRoleById(roleId);
        if (role.getUsers().contains(user)) {
            role.removeUserFromRole(user);
            roleRepository.save(role);
            return user;
        }
        throw new UsernameNotFoundException("User not found");
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException("User is not exist"));
    }

    @Override
    public User assignRoleToUser(Long userId, Long roleId) {
        User user = getUser(userId);
        Role role = findRoleById(roleId);
        if (user.getRoles().contains(role)) {
            throw new UserAlreadyExistsException(
                    user.getFirstName() + " is already assigned to the" + role.getName() + " role");
        }
        role.assignRoleToUser(user);
        roleRepository.save(role);

        return user;
    }

    @Override
    public Role removeAllUsersFromRole(Long roleId) {
        Role role = findRoleById(roleId);
        role.removeAllUsersFromRole();
        return roleRepository.save(role);
    }

    public Role findRoleById(Long userId) {
        return roleRepository.findById(userId).orElseThrow(() -> new RuntimeException(" Role not exist"));
    }

}
