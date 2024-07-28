package ru.job4j.repository;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Candidate;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryCandidateRepository implements CandidateRepository {

    private AtomicInteger nextId = new AtomicInteger(0);

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Nikita", "Base Java Core", false, 3));
        save(new Candidate(0, "Anton", "Java Core, Spring, Spring Boot", true, 1));
        save(new Candidate(0, "Marina", "Algorithms", true, 2));
        save(new Candidate(0, "Ivan", "System administrator", true, 3));
    }

    Map<Integer, Candidate> candidates = new HashMap<>();

    @Override
    public Candidate save(Candidate candidate) {
        int id = nextId.getAndIncrement();
        candidate.setId(id);
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
             new Candidate(oldCandidate.getId(), candidate.getName(), candidate.getDescription(),
                     candidate.getVisible(), candidate.getCityId())
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
