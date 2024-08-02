import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.job4j.configuration.DatasourceConfiguration;
import ru.job4j.model.User;
import ru.job4j.repository.Sql2oUserRepository;

import java.util.Optional;
import java.util.Properties;
import static org.assertj.core.api.Assertions.assertThat;

public class Sql2oUserRepositoryTest {
    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepository() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearDatabase() {
        sql2oUserRepository.deleteAllUser();
    }

    @Test
    public void whenSaveThenGetSame() {
        var user = sql2oUserRepository.save(new User(0, "Nikita", "nikita@mail.ru", "123")).get();
        var findUser = sql2oUserRepository.findByEmailAndPassword("nikita@mail.ru", "123").get();
        assertThat(findUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void whenSaveTwoUser() {
        var userFirst = sql2oUserRepository.save(new User(0, "Nikita", "nikita@mail.ru", "123")).get();
        var findUserFirst = sql2oUserRepository.findByEmailAndPassword("nikita@mail.ru", "123").get();
        var userSecond = sql2oUserRepository.save(new User(0, "Oleg", "Oleg@mail.ru", "123")).get();
        var findUserSecond = sql2oUserRepository.findByEmailAndPassword("Oleg@mail.ru", "123").get();
        assertThat(findUserFirst).usingRecursiveComparison().isEqualTo(userFirst);
        assertThat(findUserSecond).usingRecursiveComparison().isEqualTo(userSecond);
    }

    @Test
    public void whenTryGetUnExistingUser() {
        var optionRsl = sql2oUserRepository.findByEmailAndPassword("oleg@mail.ru", "123");
        assertThat(optionRsl).isEqualTo(Optional.empty());
    }

    @Test
    public void whenSaveIdentityUserThenGetEmpty() {
        sql2oUserRepository.save(new User(0, "Nikita", "nikita@mail.ru", "123"));
        var userSecond = sql2oUserRepository.save(new User(0, "Nikita", "nikita@mail.ru", "123"));
        assertThat(userSecond).isEqualTo(Optional.empty());
    }
}
