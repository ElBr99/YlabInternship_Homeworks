package com.project.controllerServlets.validator;

import com.project.exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface MyValidator<T>{
    void validate (HttpServletRequest request) throws ValidationException, IOException;

}
