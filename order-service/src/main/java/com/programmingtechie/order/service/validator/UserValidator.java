package com.programmingtechie.order.service.validator;

import com.programmingtechie.order.service.model.User;
import com.programmingtechie.order.service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        Optional<User> optionalUser = userService.findOptionalUserByUserName(user.getUsername());
        if (optionalUser.isPresent()){
            errors.rejectValue("username","","Человек с таким именем существует");
        }
    }
}
