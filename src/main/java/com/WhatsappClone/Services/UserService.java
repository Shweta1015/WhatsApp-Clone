package com.WhatsappClone.Services;

import com.WhatsappClone.Exception.UserException;
import com.WhatsappClone.Modal.User;
import com.WhatsappClone.Request.UpdateUserRequest;


import java.util.List;

public interface UserService {

    public User findUserById(Integer id) throws UserException;

    public User findUserProfile(String jwt) throws UserException;

    public User updatUser(Integer userId, UpdateUserRequest req) throws UserException;

    public List<User> searchUser(String query);
}
