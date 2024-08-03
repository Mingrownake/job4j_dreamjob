package ru.job4j.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dto.FileDto;
import ru.job4j.model.Candidate;
import ru.job4j.model.User;
import ru.job4j.repository.CityRepository;
import ru.job4j.service.CandidateService;
import ru.job4j.service.SimpleCandidateService;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/candidates")
public class CandidateController {
    private final CandidateService candidateService;
    private final CityRepository cityRepository;

    public CandidateController(
            CandidateService candidateService,
            CityRepository sql2oCityRepository) {
        this.candidateService = candidateService;
        this.cityRepository = sql2oCityRepository;
    }

    @GetMapping
    public String getCandidates(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
        model.addAttribute("candidates", candidateService.findAll());
        return "candidates/list";
    }

    @GetMapping("/create")
    public String getCreatePage(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
        model.addAttribute("cities", cityRepository.findAll());
        return "candidates/create";
    }

    @PostMapping("/create")
    public String postCreate(@ModelAttribute Candidate candidate, @RequestParam MultipartFile file, Model model) {
        try {
            candidateService.save(candidate, new FileDto(file.getOriginalFilename(), file.getBytes()));
            return "redirect:/candidates";
        } catch (IOException e) {
            model.addAttribute("message", e.getMessage());
            return "error/404";
        }
    }

    @GetMapping("{id}")
    public String getId(@PathVariable int id, Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
        var optionalCandidate = candidateService.findById(id);
        if (optionalCandidate.isEmpty()) {
            model.addAttribute("message", "Сотрудник с данным id не найден");
            return "error/404";
        }
        model.addAttribute("candidate", optionalCandidate.get());
        model.addAttribute("cities", cityRepository.findAll());
        return "candidates/one";
    }

    @PostMapping("/update")
    public String postUpdate(@ModelAttribute Candidate candidate, MultipartFile file, Model model) {
        try {
            var isUpdate = candidateService.update(candidate, new FileDto(file.getOriginalFilename(), file.getBytes()));
            if (!isUpdate) {
                model.addAttribute("message", "Сотрудник с данным id не найден");
                return "error/404";
            }
            return "redirect:/candidates";
        } catch (IOException e) {
            model.addAttribute("message", e.getMessage());
            return "error/404";
        }

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
