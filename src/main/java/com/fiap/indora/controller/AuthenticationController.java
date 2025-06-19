package com.fiap.indora.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.fiap.indora.configs.security.jwt.JwtProvider;
import com.fiap.indora.dtos.JwtDto;
import com.fiap.indora.dtos.LoginDto;
import com.fiap.indora.dtos.UserDto;
import com.fiap.indora.services.UserService;
import com.fiap.indora.validations.UserValidator;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthenticationController {

    Logger logger = LogManager.getLogger(AuthenticationController.class);

    final UserService userService;
    final UserValidator userValidator;
    final JwtProvider jwtProvider;
    final AuthenticationManager authenticationManager;

    public AuthenticationController(UserService userService, UserValidator userValidator,
                                    AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(
            @RequestBody @Validated(UserDto.UserView.RegistrationPost.class) @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto,
            Errors errors
    ) {
        logger.debug("POST registerUser userDto received {}", userDto);

        userValidator.validate(userDto, errors);

        if (errors.hasErrors()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> authenticateUser(@RequestBody @Valid LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwt(authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(new JwtDto(jwt));
    }

    @GetMapping("/github/login")
    public ResponseEntity<Void> githubLogin() {
        var headers = new HttpHeaders();
        var url = userService.githubLogin();
        headers.setLocation(
                URI.create(url)
        );
        return new ResponseEntity<>(
                headers,
                HttpStatus.FOUND
        );
    }

    @GetMapping("/github/authorized")
    public ResponseEntity<String> getTokenFromGithub(
            @RequestParam("code") String code
    ) {
        logger.debug("GET getTokenFromGithub code received {}", code);
        var tokenJwt = userService.getTokenFromGithub(code);
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenJwt);
    }
}
