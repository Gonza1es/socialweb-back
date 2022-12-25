package com.example.socialwebback.controller;


import com.example.socialwebback.dto.LoginRequest;
import com.example.socialwebback.dto.Response;
import com.example.socialwebback.dto.ResponseMessage;
import com.example.socialwebback.error.ErrorDescription;
import com.example.socialwebback.model.User;
import com.example.socialwebback.repository.UserRepository;
import com.example.socialwebback.security.jwt.JwtTokenProvider;
import com.example.socialwebback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth")
@CrossOrigin
public class AuthController {


    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;

    private UserRepository userRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          UserService userService,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest loginRequest) {
        try {
            String username = loginRequest.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword()));
            User user = userService.getUserByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("Пользователь не найден");
            }
            String token = jwtTokenProvider.createToken(username, user.getRoles());

            return new ResponseMessage(token, user.getIsActive());
        } catch (AuthenticationException e) {
            return ErrorDescription.WRONG_LOGIN_OR_PASSWORD.createException();
        }
    }

    @PostMapping("/registration")
    public Response registration(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            return ErrorDescription.LOGIN_ALREADY_EXISTS.createException();

        if (userRepository.existsByEmail(user.getEmail()))
            return ErrorDescription.EMAIL_ALREADY_EXISTS.createException();

        userService.register(user);
        return null;
    }

    @PostMapping("/adminReq")
    public void registerAdmin(@RequestBody User user) {
        userService.registAdmin(user);
    }

}
