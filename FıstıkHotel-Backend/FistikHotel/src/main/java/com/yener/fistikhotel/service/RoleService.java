package com.yener.fistikhotel.service;

import com.yener.fistikhotel.model.Role;
import com.yener.fistikhotel.model.User;

import java.util.List;


public interface RoleService {

    List<Role> getRoles();
    Role createRole(Role theRole);

    void deleteRole(Long id);
    Role findByName(String name);

    User removeUserFromRole(Long userId, Long roleId);
    User assignRoleToUser(Long userId, Long roleId);
    Role removeAllUsersFromRole(Long roleId);
}
