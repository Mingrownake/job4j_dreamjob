package ru.job4j.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.job4j.dto.FileDto;
import ru.job4j.model.File;
import ru.job4j.repository.FileRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@Service
public class SimpleFileService implements FileService {
    private final FileRepository fileRepository;

    private final String storageDirectory;

    public SimpleFileService(FileRepository fileRepository,
                             @Value("file.directory") String storageDirectory) {
        this.fileRepository = fileRepository;
        this.storageDirectory = storageDirectory;
        createStorageDirectory(storageDirectory);
    }

    private void createStorageDirectory(String storageDirectory) {
        try {
            Path path = Path.of(storageDirectory);
            if (!path.toFile().exists()) {
                Files.createDirectory(path);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File save(FileDto file) {
        var path = getNewPathFile(file.getName());
        writeFileBytes(path, file.getContent());
        return fileRepository.save(new File(file.getName(), path));
    }

    private String getNewPathFile(String sourceName) {
        return storageDirectory + java.io.File.separator + UUID.randomUUID() + sourceName;
    }

    private void writeFileBytes(String path, byte[] content) {
        try {
            Files.write(Path.of(path), content);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    @Override
    public Optional<FileDto> getFileById(int id) {
        var file = fileRepository.findById(id);
        if (file.isEmpty()) {
            return Optional.empty();
        }
        var content = readFileAsBytes(file.get().getPath());
        return Optional.of(new FileDto(file.get().getName(), content));
    }

    private byte[] readFileAsBytes(String path) {
        try {
            return Files.readAllBytes(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        var file = fileRepository.findById(id);
        if (file.isPresent()) {
            deleteFile(file.get().getPath());
            fileRepository.deleteById(id);
        }
    }

    private void deleteFile(String path) {
        try {
            Files.deleteIfExists(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
