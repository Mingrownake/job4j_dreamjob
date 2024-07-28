package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.model.City;

import java.util.Collection;

@Service
public interface CityService {
    Collection<City> findAll();
}
