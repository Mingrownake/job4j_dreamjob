package ru.job4j.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.model.City;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MemoryCityRepository implements CityRepository {

    private final Map<Integer, City> cities = new HashMap<>();

    public MemoryCityRepository() {
        cities.put(1, new City(1, "Москва"));
        cities.put(2, new City(2, "Санкт-Петербург"));
        cities.put(3, new City(3, "Киров"));
    }

    @Override
    public Collection<City> findAll() {
        return cities.values();
    }
}
