package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.model.Candidate;

import java.util.Collection;
import java.util.Optional;

@Service
public interface CandidateService {
    Candidate save(Candidate candidate);

    void deleteById(int id);

    boolean update(Candidate candidate);

    Optional<Candidate> findById(int id);

    Collection<Candidate> findAll();
}
