package com.project.controllerServlets.validator;

import com.project.exceptions.ValidationException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class ValidationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String httpMethod = httpRequest.getMethod();
        Class<?> servletClass = (Class<?>) httpRequest.getAttribute("servletClass");

        if (servletClass != null) {

            ValidatorKey key = new ValidatorKey(servletClass, httpMethod);
            var validator = ValidatorProvider.get(key);

            if (validator != null) {
                try {
                    validator.validate(httpRequest);
                } catch (ValidationException exception) {
                    httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    httpResponse.getWriter().write(exception.getMessage());
                    return;
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }
}
