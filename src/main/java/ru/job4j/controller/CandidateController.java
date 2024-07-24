package ru.job4j.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.model.Candidate;
import ru.job4j.repository.CandidateRepository;
import ru.job4j.repository.MemoryCandidateRepository;

@Controller
@RequestMapping("/candidates")
public class CandidateController {
    private final CandidateRepository candidateRepository = MemoryCandidateRepository.getInstance();

    @GetMapping
    public String getCandidates(Model model) {
        model.addAttribute("candidates", candidateRepository.findAll());
        return "candidates/list";
    }

    @GetMapping("/create")
    public String getCreatePage() {
        return "candidates/create";
    }

    @PostMapping("/create")
    public String postCreate(@ModelAttribute Candidate candidate) {
        candidateRepository.save(candidate);
        return "redirect:/candidates";
    }

    @GetMapping("{id}")
    public String getId(@PathVariable int id, Model model) {
        var optionalCandidate = candidateRepository.findById(id);
        if (optionalCandidate.isEmpty()) {
            model.addAttribute("message", "Сотрудник с данным id не найден");
            return "error/404";
        }
        model.addAttribute("candidate", optionalCandidate.get());
        return "candidates/one";
    }

    @PostMapping("/update")
    public String postUpdate(@ModelAttribute Candidate candidate, Model model) {
        var isUpdate = candidateRepository.update(candidate);
        if (!isUpdate) {
            model.addAttribute("message", "Сотрудник с данным id не найден");
            return "error/404";
        }
        return "redirect:/candidates";
    }

    @GetMapping("/delete/{id}")
    public String getDelete(Model model, @PathVariable int id) {
        var optionalCandidate = candidateRepository.findById(id);
        if (optionalCandidate.isEmpty()) {
            model.addAttribute("message", "Сотрудник с данным id не найден");
            return "error/404";
        }
        candidateRepository.deleteById(id);
        return "redirect:/candidates";
    }

}
