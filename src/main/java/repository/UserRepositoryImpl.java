package repository;

import model.Role;
import model.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepositoryImpl implements UserRepository {

    private static final Map<String, User> usersMap = new ConcurrentHashMap<>();

    static {
        User admin = new User("Admin", "admin@mail.ru", "admin", Role.ADMIN, false);
        usersMap.put(admin.getEmail(), admin);
    }

    public void save(User user) {
        usersMap.putIfAbsent(user.getEmail(), user);
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(usersMap.get(email));
    }

    //сделать интерфейс

    public void update(User user) {
        usersMap.put(user.getEmail(), user);
    }

    public void delete(User user) {
        usersMap.remove(user.getEmail(), user);
    }


}
