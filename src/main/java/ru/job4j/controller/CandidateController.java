package ru.job4j.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.model.Candidate;
import ru.job4j.service.CandidateService;
import ru.job4j.service.SimpleCandidateService;

@Controller
@RequestMapping("/candidates")
public class CandidateController {
    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
    public String getCandidates(Model model) {
        model.addAttribute("candidates", candidateService.findAll());
        return "candidates/list";
    }

    @GetMapping("/create")
    public String getCreatePage() {
        return "candidates/create";
    }

    @PostMapping("/create")
    public String postCreate(@ModelAttribute Candidate candidate) {
        candidateService.save(candidate);
        return "redirect:/candidates";
    }

    @GetMapping("{id}")
    public String getId(@PathVariable int id, Model model) {
        var optionalCandidate = candidateService.findById(id);
        if (optionalCandidate.isEmpty()) {
            model.addAttribute("message", "Сотрудник с данным id не найден");
            return "error/404";
        }
        model.addAttribute("candidate", optionalCandidate.get());
        return "candidates/one";
    }

    @PostMapping("/update")
    public String postUpdate(@ModelAttribute Candidate candidate, Model model) {
        var isUpdate = candidateService.update(candidate);
        if (!isUpdate) {
            model.addAttribute("message", "Сотрудник с данным id не найден");
            return "error/404";
        }
        return "redirect:/candidates";
    }

    @GetMapping("/delete/{id}")
    public String getDelete(Model model, @PathVariable int id) {
        var optionalCandidate = candidateService.findById(id);
        if (optionalCandidate.isEmpty()) {
            model.addAttribute("message", "Сотрудник с данным id не найден");
            return "error/404";
        }
        candidateService.deleteById(id);
        return "redirect:/candidates";
    }

}
