package ru.job4j.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.job4j.model.User;

import java.util.Optional;
import java.util.logging.Logger;

@Repository
public class Sql2oUserRepository implements UserRepository {
    private final Sql2o sql2o;
    private Logger log = Logger.getLogger(this.getClass().getName());

    public Sql2oUserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<User> save(User user) {
        try (Connection con = sql2o.open()) {
            var sql = "insert into users(name, email, password) values(:name, :email, :password)";
            var query = con.createQuery(sql, true)
                    .addParameter("name", user.getName())
                    .addParameter("email", user.getEmail())
                    .addParameter("password", user.getPassword());
            int generateKey = query.executeUpdate().getKey(Integer.class);
            user.setId(generateKey);
            return Optional.of(user);
        } catch (Sql2oException ex) {
            log.info("Пользователь с данным email: " + user.getEmail() + " уже существует");
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (Connection con = sql2o.open()) {
            var sql = "select * from users where email = :email and password = :password";
            var query = con.createQuery(sql)
                    .addParameter("email", email)
                    .addParameter("password", password);
            var user = query.executeAndFetchFirst(User.class);
            return Optional.ofNullable(user);
        }
    }

    public void deleteAllUser() {
        try (Connection con = sql2o.open()) {
            var sql = "delete from users";
            con.createQuery(sql).executeUpdate();
        }
    }
}
