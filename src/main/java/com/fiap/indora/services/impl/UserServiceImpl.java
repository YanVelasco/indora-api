package com.fiap.indora.services.impl;


import com.fiap.indora.dtos.UserDto;
import com.fiap.indora.enums.RoleName;
import com.fiap.indora.enums.UserStatus;
import com.fiap.indora.enums.UserType;
import com.fiap.indora.exceptions.AlreadyExistsException;
import com.fiap.indora.exceptions.NotFoundException;
import com.fiap.indora.model.UserModel;
import com.fiap.indora.repositories.UserRepository;
import com.fiap.indora.services.RoleService;
import com.fiap.indora.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LogManager.getLogger(UserServiceImpl.class);

    final UserRepository userRepository;
    final RoleService roleService;
    final PasswordEncoder passwordEncoder;

    @Value("${auth.github.clientId}")
    private String CLIENT_ID;

    @Value("${auth.github.redirectUri}")
    private String REDIRECT_URI;

    @Value("${auth.github.clientSecret}")
    private String clientSecret;

    private final RestClient restClient;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService,
                           PasswordEncoder passwordEncoder, RestClient.Builder restClientbuilder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.restClient = restClientbuilder.build();
    }

    @Override
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserModel findById(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found with id: " + id)
        );
    }

    @Transactional
    @Override
    public void deleteById(UUID userId) {

        logger.debug("DELETE deleteById userId received {}", userId);

        UserModel userModel = findById(userId);

        userRepository.delete(userModel);

    }

    @Transactional
    @Override
    public UserModel registerUser(UserDto userDto) {

        logger.debug("POST registerUser userDto received {}", userDto);

        var userModel = new UserModel();

        BeanUtils.copyProperties(userDto, userModel);

        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.USER);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userModel.getRoles().add(roleService.findByRoleName(RoleName.ROLE_USER));

        userRepository.save(userModel);

        return userModel;

    }

    @Transactional
    @Override
    public ResponseEntity<UserModel> updateUser(UUID userId, UserDto userDto) {

        logger.debug("PUT updateUser userId received {}", userId);

        var userModel = findById(userId);

        if (userDto.fullName() != null) {
            userModel.setFullName(userDto.fullName());
        }

        if (userDto.phoneNumber() != null) {
            userModel.setPhoneNumber(userDto.phoneNumber());
        }

        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        userRepository.save(userModel);

        return ResponseEntity.status(HttpStatus.OK).body(userModel);

    }

    @Transactional
    @Override
    public ResponseEntity<UserModel> updateUserPassword(UUID userId, UserDto userDto) {

        logger.debug("PUT updateUserPassword userId received {}", userId);
        logger.debug("PUT updateUserPassword userDto received {}", userDto);

        var userModel = findById(userId);

        if (!passwordEncoder.matches(userDto.oldPassword(), userModel.getPassword())) {
            logger.warn("Error: mismatch old password");
            throw new AlreadyExistsException("Error: mismatch old password");
        }

        if (userDto.password() != null) {
            userModel.setPassword(passwordEncoder.encode(userDto.password()));
        }

        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        userRepository.save(userModel);

        return ResponseEntity.status(HttpStatus.OK).body(userModel);

    }

    @Transactional
    @Override
    public ResponseEntity<UserModel> updateImage(UUID userId, UserDto userDto) {

        logger.debug("PUT updateImage userId received {}", userId);
        logger.debug("PUT updateImage userDto received {}", userDto);

        var userModel = findById(userId);

        if (userDto.imageUrl() != null) {
            userModel.setImageUrl(userDto.imageUrl());
        }

        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        userRepository.save(userModel);

        return ResponseEntity.status(HttpStatus.OK).body(userModel);

    }

    @Override
    public Page<UserModel> findAll(Pageable pageable) {

        logger.debug("GET findAll pageable received {}", pageable);

        return userRepository.findAll(pageable);
    }

    @Override
    public Boolean existsByUsername(String username) {

        logger.debug("GET existsByUsername username received {}", username);

        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {

        logger.debug("GET existsByEmail email received {}", email);

        return userRepository.existsByEmail(email);
    }

    @Override
    public String githubLogin() {
        try {
            String scope = URLEncoder.encode("read:user,user:email", StandardCharsets.UTF_8);
            String redirectUri = URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8);
            return "https://github.com/login/oauth/authorize"
                    + "?client_id=" + CLIENT_ID
                    + "&redirect_uri=" + redirectUri
                    + "&scope=" + scope;
        } catch (Exception e) {
            logger.error("Erro ao gerar URL de login do GitHub", e);
            throw new RuntimeException("Erro ao gerar URL de login do GitHub", e);
        }
    }

    public String getTokenFromGithub(String code) {
        var response = restClient.post()
                .uri("https://github.com/login/oauth/access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Map.of("code", code, "client_id", CLIENT_ID,
                        "client_secret", clientSecret, "redirect_uri", REDIRECT_URI))
                .retrieve()
                .body(String.class);
        return response;
    }


}
