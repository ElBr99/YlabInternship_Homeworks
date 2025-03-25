package com.project;

import com.project.dtos.EnterUserDto;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class AuthorizationFilter implements Filter {

    private static final Set<String> PUBLIC_PATH = Set.of("/registration", "/entrance");
    private static final Set<String> ADMIN_PATH = Set.of("/blockUser", "/deleteUser", "/getUserTransaction");


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        var uri = ((HttpServletRequest) servletRequest).getRequestURI();

        if (isPublicPath(uri)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            if (isUserLoggedIn(servletRequest)) {
                if (isAdminPath(uri)) {
                    if (isUserAdmin(servletRequest)) {
                        filterChain.doFilter(servletRequest, servletResponse);
                    }
                    ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещён");
                } else {
                    filterChain.doFilter(servletRequest, servletResponse);
                }
            } else {
                ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Для продолжения нужно авторизоваться");
            }
        }

    }

    private boolean isAdminPath(String uri) {
        return ADMIN_PATH.stream().anyMatch(uri::startsWith);
    }

    private boolean isUserLoggedIn(ServletRequest servletRequest) {
        var user = (EnterUserDto) ((HttpServletRequest) servletRequest).getSession().getAttribute("user");
        return user != null;
    }

    private boolean isPublicPath(String uri) {
        return PUBLIC_PATH.stream().anyMatch(uri::startsWith);
    }

    private boolean isUserAdmin(ServletRequest servletRequest) {
        var user = (EnterUserDto) ((HttpServletRequest) servletRequest).getSession().getAttribute("user");
        return user != null && user.getEmail().equals("admin@mail.ru") && user.getPassword().equals("admin");
    }

}
