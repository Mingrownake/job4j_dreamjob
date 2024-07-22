package ru.job4j.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.model.Vacancy;

import java.util.*;

@Repository
public class MemoryVacancyRepository implements VacancyRepository {
    private final static MemoryVacancyRepository INSTANCE = new MemoryVacancyRepository();

    private int nextId = 1;

    private final Map<Integer, Vacancy> vacancies = new HashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "Base Java"));
        save(new Vacancy(0, "Junior Java Developer", "Base Java, OOP"));
        save(new Vacancy(0, "Junior+ Java Developer", "Base Java, OOP, Collection"));
        save(new Vacancy(0, "Middle Java Developer", "Base Java, OOP, Collection, Lambda"));
        save(new Vacancy(0, "Middle+ Java Developer",
                "Base Java, OOP, Collection, Lambda, Stream API"));
        save(new Vacancy(0, "Senior Java Developer",
                "Base Java, OOP, Collection, Lambda, Stream API, IO"));
    }

    public static MemoryVacancyRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId++);
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public void deleteById(int id) {
        vacancies.remove(id);
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(), (id, oldVacancy) ->
            new Vacancy(id, oldVacancy.getTitle(), oldVacancy.getDescription())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}
