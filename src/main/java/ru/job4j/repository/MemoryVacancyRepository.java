package ru.job4j.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Vacancy;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryVacancyRepository implements VacancyRepository {

    private AtomicInteger nextId = new AtomicInteger(0);

    private final Map<Integer, Vacancy> vacancies = new HashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "Base Java", true, 1, 0));
        save(new Vacancy(0, "Junior Java Developer", "Base Java, OOP", true, 1, 0));
        save(new Vacancy(0, "Junior+ Java Developer", "Base Java, OOP, Collection", true, 1, 0));
        save(new Vacancy(0, "Middle Java Developer", "Base Java, OOP, Collection, Lambda", true, 2, 0));
        save(new Vacancy(0, "Middle+ Java Developer",
                "Base Java, OOP, Collection, Lambda, Stream API", true, 3, 0));
        save(new Vacancy(0, "Senior Java Developer",
                "Base Java, OOP, Collection, Lambda, Stream API, IO", true, 3, 0));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        int id = nextId.getAndIncrement();
        vacancy.setId(id);
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
            new Vacancy(oldVacancy.getId(), vacancy.getTitle(), vacancy.getDescription(),
                    vacancy.getVisible(), vacancy.getCityId(), vacancy.getFileId())) != null;
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
