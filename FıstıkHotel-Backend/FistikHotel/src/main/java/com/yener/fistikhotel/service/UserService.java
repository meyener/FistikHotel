package com.yener.fistikhotel.service;

import com.yener.fistikhotel.model.User;

import java.util.List;

public interface UserService {

    User registerUser(User user);
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);
}
