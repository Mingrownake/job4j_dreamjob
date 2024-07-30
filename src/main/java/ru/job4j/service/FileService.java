package ru.job4j.service;

import ru.job4j.dto.FileDto;
import ru.job4j.model.File;

import java.util.Optional;

public interface FileService {
    File save(FileDto file);

    Optional<FileDto> getFileById(int id);

    void delete(int id);
}
