package ru.job4j.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dto.FileDto;
import ru.job4j.model.Candidate;
import ru.job4j.repository.CandidateRepository;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
@Service
public class SimpleCandidateService implements CandidateService {
    private final CandidateRepository candidateRepository;
    private final FileService fileService;

    public SimpleCandidateService(CandidateRepository candidateRepository, FileService fileService) {
        this.candidateRepository = candidateRepository;
        this.fileService = fileService;
    }

    @Override
    public Candidate save(Candidate candidate, FileDto fileDto) {
        saveNewFile(candidate, fileDto);
        return candidateRepository.save(candidate);
    }

    private void saveNewFile(Candidate candidate, FileDto fileDto) {
        var file = fileService.save(fileDto);
        candidate.setFileId(file.getId());
    }

    @Override
    public void deleteById(int id) {
        var candidate = findById(id);
        if (candidate.isPresent()) {
            fileService.delete(candidate.get().getFileId());
            candidateRepository.deleteById(id);
        }
        candidateRepository.deleteById(id);
    }

    @Override
    public boolean update(Candidate candidate, FileDto fileDto) {
        boolean isNewFileEmpty = fileDto.getContent().length == 0;
        if (isNewFileEmpty) {
            return candidateRepository.update(candidate);
        }
        var oldFileId = candidate.getFileId();
        saveNewFile(candidate, fileDto);
        boolean isUpdated = candidateRepository.update(candidate);
        fileService.delete(oldFileId);
        return isUpdated;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return candidateRepository.findById(id);
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidateRepository.findAll();
    }
}
