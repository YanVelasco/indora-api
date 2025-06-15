package com.fiap.indora.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.fiap.indora.validations.PasswordConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDto(

        @NotBlank(message = "Username is required", groups = {UserView.RegistrationPost.class})
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters", groups = {UserView.RegistrationPost.class})
        @JsonView(UserView.RegistrationPost.class)
        String username,

        @Email(message = "Email is invalid", groups = {UserView.RegistrationPost.class})
        @NotBlank(message = "Email is required", groups = {UserView.RegistrationPost.class})
        @Size(max = 50, message = "Email must be less than 50 characters", groups = {UserView.RegistrationPost.class})
        @JsonView(UserView.RegistrationPost.class)
        String email,

        @NotBlank(message = "Password is required", groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
        @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters", groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
        @PasswordConstraint(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
        @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
        String password,

        @NotBlank(message = "Old password is required", groups = {UserView.PasswordPut.class})
        @Size(min = 6, max = 20, message = "Old password must be between 6 and 20 characters", groups = {UserView.PasswordPut.class})
        @PasswordConstraint(groups = {UserView.PasswordPut.class})
        @JsonView(UserView.PasswordPut.class)
        String oldPassword,

        @NotBlank(message = "Full name is required", groups = {UserView.RegistrationPost.class, UserView.UserPut.class})
        @Size(max = 150, message = "Full name must be less than 150 characters", groups = {UserView.RegistrationPost.class, UserView.UserPut.class})
        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String fullName,

        @Size(max = 20, message = "User phone number must be less than 20 characters", groups = {UserView.RegistrationPost.class, UserView.UserPut.class})
        @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
        String phoneNumber,

        @NotBlank(message = "Image URL is required", groups = {UserView.ImagePut.class})
        @JsonView(UserView.ImagePut.class)
        String imageUrl

) {

    public interface UserView {
        interface RegistrationPost {}
        interface UserPut {}
        interface PasswordPut {}
        interface ImagePut {}
    }

}
