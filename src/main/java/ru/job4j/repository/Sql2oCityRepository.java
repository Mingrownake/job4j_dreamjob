package ru.job4j.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.model.City;
import java.util.Collection;

@Repository
public class Sql2oCityRepository implements CityRepository {
    private final Sql2o sql2o;

    public Sql2oCityRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Collection<City> findAll() {
        try (Connection con = sql2o.open()) {
            var query = con.createQuery("select * from cities");
            return query.executeAndFetch(City.class);
        }
    }
}
