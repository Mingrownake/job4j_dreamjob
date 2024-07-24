package ru.job4j.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.model.Candidate;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CandidateRepository {
    Candidate save(Candidate candidate);

    void deleteById(int id);

    boolean update(Candidate candidate);

    Optional<Candidate> findById(int id);

    Collection<Candidate> findAll();
}
