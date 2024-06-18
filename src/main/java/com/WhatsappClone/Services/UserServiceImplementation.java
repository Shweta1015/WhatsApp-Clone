package com.WhatsappClone.Services;

import com.WhatsappClone.Config.TokenProvider;
import com.WhatsappClone.Exception.UserException;
import com.WhatsappClone.Modal.User;
import com.WhatsappClone.Repository.UserRepository;
import com.WhatsappClone.Request.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;
    public UserServiceImplementation(UserRepository userRepository, TokenProvider tokenProvider){
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }
    @Override
    public User findUserById(Integer id) throws UserException{
       Optional<User> optional = userRepository.findById(id);

       if(optional.isPresent()){
           return optional.get();
       }
       throw new UserException("User not found with id "+id);
    }

    @Override
    public User findUserProfile(String jwt) throws UserException {
        String email = tokenProvider.getEmailFromToken(jwt);

        if (email == null) {
            throw new BadCredentialsException("received invalid token--");
        }
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserException(("User not found with email "+email));
        }
        return user;
    }

    @Override
    public User updatUser(Integer userId, UpdateUserRequest req) throws UserException {
        User user = findUserById(userId);

        if(req.getName()!= null){
            user.setName(req.getName());
        }
        if(req.getProfile_picture()!= null){
            user.setProfile_picture(req.getProfile_picture());
        }
        return userRepository.save(user);
    }

    @Override
    public List<User> searchUser(String query) {
        List<User> users = userRepository.searchUser(query);
        return users;
    }
}
