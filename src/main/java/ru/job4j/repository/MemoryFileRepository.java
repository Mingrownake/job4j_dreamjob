package ru.job4j.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.model.File;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class MemoryFileRepository implements FileRepository {
    private AtomicInteger nextId = new AtomicInteger(0);

    private Map<Integer, File> files = new ConcurrentHashMap<>();

    @Override
    public File save(File file) {
        file.setId(nextId.incrementAndGet());
        files.put(file.getId(), file);
        return file;
    }

    @Override
    public Optional<File> findById(int id) {
        return Optional.ofNullable(files.get(id));
    }

    @Override
    public void deleteById(int id) {
        files.remove(id);
    }
}
