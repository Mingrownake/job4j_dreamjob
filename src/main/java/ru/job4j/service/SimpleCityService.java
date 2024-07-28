package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.model.City;
import ru.job4j.repository.CityRepository;

import java.util.Collection;
import java.util.List;

@Service
public class SimpleCityService implements CityService {
    private final CityRepository cityRepository;

    public SimpleCityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public Collection<City> findAll() {
        return cityRepository.findAll();
    }
}
