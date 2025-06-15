package com.fiap.indora.validations;

import com.fiap.indora.validations.impl.PasswordConstraintImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordConstraintImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {

    String message() default "Invalid Password! Password must contain at last one digit [0-9], at one lowercase latin character [a-z], at one uppercase latin character [A-Z], at one special character like [!@#*()], a length between 6 and 20 characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
