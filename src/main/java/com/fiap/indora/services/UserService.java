package com.fiap.indora.services;

import com.fiap.indora.dtos.UserDto;
import com.fiap.indora.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserModel> findAll();

    UserModel findById(UUID id);

    void deleteById(UUID userId);

    UserModel registerUser(UserDto userDto);

    ResponseEntity<UserModel> updateUser(UUID userId, UserDto userDto);

    ResponseEntity<UserModel> updateUserPassword(UUID userId, UserDto userDto);

    ResponseEntity<UserModel> updateImage(UUID userId, UserDto userDto);

    Page<UserModel> findAll(Pageable pageable);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    String githubLogin();

}
