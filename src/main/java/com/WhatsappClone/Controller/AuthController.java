package com.WhatsappClone.Controller;

import com.WhatsappClone.Config.TokenProvider;
import com.WhatsappClone.Exception.UserException;
import com.WhatsappClone.Modal.User;
import com.WhatsappClone.Repository.UserRepository;
import com.WhatsappClone.Request.LoginRequest;
import com.WhatsappClone.Response.AuthResponse;
import com.WhatsappClone.Services.CustomUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private TokenProvider tokenProvider;
    private CustomUserService customUserService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, CustomUserService customUserService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserService = customUserService;
    }

    //to create new user
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
        String email = user.getEmail();
        String Name = user.getName();
        String password = user.getPassword();

        //if email is already used or not
        User isUser = userRepository.findByEmail(email);
        if(isUser != null){
            throw new UserException("Email is already registered with another account "+email);
        }

        //create new user
        User createUser = new User();
        createUser.setEmail(email);
        createUser.setName(Name);
        createUser.setPassword(passwordEncoder.encode(password));

        userRepository.save(createUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        AuthResponse response = new AuthResponse(jwt, true);
        return new ResponseEntity<AuthResponse>(response, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req){
        String email = req.getEmail();
        String password = req.getPassword();

        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        AuthResponse response = new AuthResponse(jwt, true);
        return new ResponseEntity<AuthResponse>(response, HttpStatus.ACCEPTED);

    }

    //login
    public Authentication authenticate(String Username, String password){
        UserDetails userDetails = customUserService.loadUserByUsername(Username);

        //if user is not found
        if(userDetails == null){
            throw new BadCredentialsException("invalid username");
        }

        //password authenticate
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
