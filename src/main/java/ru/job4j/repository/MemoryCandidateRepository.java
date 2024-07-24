package ru.job4j.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.model.Candidate;

import java.util.*;

@Repository
public class MemoryCandidateRepository implements CandidateRepository {

    private int nextId = 1;

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Nikita", "Base Java Core"));
        save(new Candidate(0, "Anton", "Java Core, Spring, Spring Boot"));
        save(new Candidate(0, "Marina", "Algorithms"));
        save(new Candidate(0, "Ivan", "System administrator"));
    }

    Map<Integer, Candidate> candidates = new HashMap<>();

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public void deleteById(int id) {
        candidates.remove(id);
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(), (i, oldCandidate) ->
             new Candidate(oldCandidate.getId(), candidate.getName(), candidate.getDescription())
        ) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
