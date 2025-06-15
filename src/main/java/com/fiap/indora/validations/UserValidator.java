package com.fiap.indora.validations;

import com.fiap.indora.dtos.UserDto;
import com.fiap.indora.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final Validator validator;
    private final UserService userService;

    Logger logger = LogManager.getLogger(UserValidator.class);

    public UserValidator(@Qualifier("defaultValidator") Validator validator, UserService userService) {
        this.validator = validator;
        this.userService = userService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto userDto = (UserDto) target;
        validator.validate(userDto, errors);

        if (!errors.hasErrors()) {
            validateUserName(userDto, errors);
            validateEmail(userDto, errors);
        }
    }

    public void validateUserName(UserDto userDto,Errors errors) {
        if (userService.existsByUsername(userDto.username())) {
            logger.error("Username already exists {}", userDto.username());
            errors.rejectValue("username", "error.user", "Username already exists");
        }
    }

    public void validateEmail(UserDto userDto, Errors errors) {
        if (userService.existsByEmail(userDto.email())) {
            logger.error("Email already exists {}", userDto.email());
            errors.rejectValue("email", "error.user", "Email already exists");
        }
    }

}
