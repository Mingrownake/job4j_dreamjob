package ru.job4j.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dto.FileDto;
import ru.job4j.model.Vacancy;
import ru.job4j.repository.VacancyRepository;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
@Service
public class SimpleVacancyService implements VacancyService {
    private final VacancyRepository vacancyRepository;
    private final FileService fileService;

    public SimpleVacancyService(VacancyRepository vacancyRepository,
                                FileService fileService) {
        this.vacancyRepository = vacancyRepository;
        this.fileService = fileService;
    }

    @Override
    public Vacancy save(Vacancy vacancy, FileDto fileDto) {
        saveNewFile(vacancy, fileDto);
        return vacancyRepository.save(vacancy);
    }

    private void saveNewFile(Vacancy vacancy, FileDto fileDto) {
        var file = fileService.save(fileDto);
        vacancy.setFileId(file.getId());
    }

    @Override
    public void deleteById(int id) {
        var vacancy = findById(id);
        if (vacancy.isPresent()) {
            fileService.delete(vacancy.get().getFileId());
            vacancyRepository.deleteById(id);
        }
    }

    @Override
    public boolean update(Vacancy vacancy, FileDto fileDto) {
        boolean isNewFileEmpty = fileDto.getContent().length == 0;
        if (isNewFileEmpty) {
            return vacancyRepository.update(vacancy);
        }
        var oldFileId = vacancy.getFileId();
        saveNewFile(vacancy, fileDto);
        boolean isUpdated = vacancyRepository.update(vacancy);
        fileService.delete(oldFileId);
        return isUpdated;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return vacancyRepository.findById(id);
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancyRepository.findAll();
    }
}
