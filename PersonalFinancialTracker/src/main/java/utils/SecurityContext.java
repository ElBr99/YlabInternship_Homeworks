package utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import model.User;

@Getter
@UtilityClass

public class SecurityContext {

    private static ThreadLocal<User> currentUser;

    public static User getCurrentUserInfo() {
        return currentUser.get();
    }

    public static boolean isEmptyContext() {
        return currentUser == null;
    }

    public static void setCurrentUser(User currentUser) {
        SecurityContext.currentUser = ThreadLocal.withInitial(() -> currentUser);
    }

    public static String getCurrentUserEmail() {
        return currentUser.get().getEmail();
    }

    public static void clearContext() {
        currentUser = null;

    }

}
