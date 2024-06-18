package com.WhatsappClone.Controller;

import com.WhatsappClone.Exception.UserException;
import com.WhatsappClone.Modal.User;
import com.WhatsappClone.Request.UpdateUserRequest;
import com.WhatsappClone.Response.ApiResponse;
import com.WhatsappClone.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String token) throws UserException {  //token will be fetched from authorization token

        User user = userService.findUserProfile(token);
        return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{query}")
    public ResponseEntity<List<User>> searchUserHandler(@PathVariable("query") String q){
        List<User> users = userService.searchUser(q);
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUserHandler(@RequestBody UpdateUserRequest req, @RequestHeader("Authorization") String token) throws UserException {

        User user = userService.findUserProfile(token);
        userService.updatUser(user.getId(), req);
        ApiResponse response = new ApiResponse("user successfully updated", true);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
