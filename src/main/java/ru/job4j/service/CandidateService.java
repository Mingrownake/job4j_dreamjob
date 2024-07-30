package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.dto.FileDto;
import ru.job4j.model.Candidate;

import java.util.Collection;
import java.util.Optional;

@Service
public interface CandidateService {
    Candidate save(Candidate candidate, FileDto fileDto);

    void deleteById(int id);

    boolean update(Candidate candidate, FileDto fileDto);

    Optional<Candidate> findById(int id);

    Collection<Candidate> findAll();
}
