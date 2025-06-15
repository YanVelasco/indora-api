package com.fiap.indora.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.fiap.indora.configs.security.AuthenticationCurrentUserService;
import com.fiap.indora.configs.security.UserDetailsImpl;
import com.fiap.indora.dtos.UserDto;
import com.fiap.indora.model.UserModel;
import com.fiap.indora.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {

    Logger logger = LogManager.getLogger(UserController.class);

    final UserService userService;
    final AuthenticationCurrentUserService authenticationCurrentUserService;

    public UserController(UserService userService, AuthenticationCurrentUserService authenticationCurrentUserService) {
        this.userService = userService;
        this.authenticationCurrentUserService = authenticationCurrentUserService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<UserModel>> getAllUsers(
            Pageable pageable,
            Authentication authentication
    ) {

        UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();
        logger.info("Authentication {}", userDetails.getUsername());

        Page<UserModel> userModalPage = userService.findAll(pageable);

        if (!userModalPage.isEmpty()) {
            for (UserModel userModel : userModalPage.toList()) {
                userModel.add(
                        linkTo(methodOn(UserController.class).getUserById(userModel.getUserId())).withSelfRel()
                );
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(userModalPage);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserModel> getUserById(@PathVariable UUID userId) {
        UserDetailsImpl userDetails = authenticationCurrentUserService.getCurrentUser();
        if (userDetails.getUserId().equals(userId) || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
            return ResponseEntity.status(HttpStatus.OK).body(userService.findById(userId));
        else
            throw new AccessDeniedException("Forbidden");
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID userId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails.getUserId().equals(userId) || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            logger.debug("DELETE deleteUserById userId received {}", userId);
            userService.deleteById(userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else
            throw new AccessDeniedException("Forbidden");
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{userId}")
    public ResponseEntity<UserModel> updateUser(
            @PathVariable UUID userId,
            @RequestBody @Validated(UserDto.UserView.UserPut.class) @JsonView(UserDto.UserView.UserPut.class) UserDto userDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails.getUserId().equals(userId) || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            logger.debug("PUT updateUser userId received {}", userId);
            return userService.updateUser(userId, userDto);
        } else
            throw new AccessDeniedException("Forbidden");
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{userId}/password")
    public ResponseEntity<UserModel> updateUserPassword(
            @PathVariable UUID userId,
            @RequestBody @Validated(UserDto.UserView.PasswordPut.class) @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails.getUserId().equals(userId) || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            logger.debug("PUT updateUserPassword userId received {}", userId);
            return userService.updateUserPassword(userId, userDto);
        } else
            throw new AccessDeniedException("Forbidden");
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{userId}/image")
    public ResponseEntity<UserModel> updateUserImage(
            @PathVariable UUID userId,
            @RequestBody @Validated(UserDto.UserView.ImagePut.class) @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails.getUserId().equals(userId) || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            logger.debug("PUT updateUserImage userId received {}", userId);
            return userService.updateImage(userId, userDto);
        } else
            throw new AccessDeniedException("Forbidden");
    }

}
