package com.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.controllerServlets.validator.ValidatorKey;
import com.project.controllerServlets.validator.ValidatorProvider;
import com.project.exceptions.ValidationException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.util.Map;

@WebFilter("/*")
public class ValidationFilter implements Filter {

    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String httpMethod = httpRequest.getMethod();

        Class<?> servletClass = Class.forName(httpRequest.getHttpServletMapping().getServletName());
        System.out.println("Полученный servletClass: " + servletClass);

        ValidatorKey key = new ValidatorKey(servletClass, httpMethod);
        var validator = ValidatorProvider.get(key);

        if (validator != null) {
            try {
                validator.validate(httpRequest);
            } catch (ValidationException exception) {
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write(objectMapper.writeValueAsString(Map.of("message", exception.getMessage())));
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }
}
