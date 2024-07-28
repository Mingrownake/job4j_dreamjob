package ru.job4j.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.model.City;

import java.util.Collection;

@Repository
public interface CityRepository {
    Collection<City> findAll();
}
