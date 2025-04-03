package com.project.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import com.project.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

@Getter
@UtilityClass
public class SecurityContext {

    public static String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
