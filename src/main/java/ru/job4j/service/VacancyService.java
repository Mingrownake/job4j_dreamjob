package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.dto.FileDto;
import ru.job4j.model.Vacancy;


import java.util.Collection;
import java.util.Optional;

@Service
public interface VacancyService {
    Vacancy save(Vacancy vacancy, FileDto fileDto);

    void deleteById(int id);

    boolean update(Vacancy vacancy, FileDto fileDto);

    Optional<Vacancy> findById(int id);

    Collection<Vacancy> findAll();
}

