package ru.job4j.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dto.FileDto;
import ru.job4j.model.Vacancy;
import ru.job4j.service.CityService;
import ru.job4j.service.VacancyService;

import java.io.IOException;

@Controller
@RequestMapping("/vacancies")
public class VacancyController {
    private final VacancyService vacancyService;
    private final CityService cityService;

    public VacancyController(VacancyService vacancyService,
                             CityService cityService) {
        this.vacancyService = vacancyService;
        this.cityService = cityService;
    }

    @GetMapping
    public String getList(Model model) {
        model.addAttribute("vacancies", vacancyService.findAll());
        return "vacancies/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("cities", cityService.findAll());
        return "vacancies/create";
    }

    @PostMapping("/create")
    public String postCreatePage(@ModelAttribute Vacancy vacancy, @RequestParam MultipartFile file, Model model) {
        try {
            vacancyService.save(vacancy, new FileDto(file.getOriginalFilename(), file.getBytes()));
            return "redirect:/vacancies";
        } catch (IOException e) {
            model.addAttribute("message", e.getMessage());
            return "error/404";
        }
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var optionalVacancy = vacancyService.findById(id);
        if (optionalVacancy.isEmpty()) {
            model.addAttribute("message", "Пользователь с данным id не найден");
            return "error/404";
        }
        model.addAttribute("vacancy", optionalVacancy.get());
        model.addAttribute("cities", cityService.findAll());
        return "vacancies/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Vacancy vacancy, @RequestParam MultipartFile file,  Model model) {
        try {
            var isUpdate = vacancyService.update(vacancy, new FileDto(file.getOriginalFilename(), file.getBytes()));
            if (!isUpdate) {
                model.addAttribute("message", "Вакансия с данным id не найдена");
                return "error/404";
            }
            return "redirect:/vacancies";
        } catch (IOException e) {
            model.addAttribute("message", e.getMessage());
            return "error/404";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var optionalVacancy = vacancyService.findById(id);
        if (optionalVacancy.isEmpty()) {
            model.addAttribute("message", "Пользователь с данным id не найден");
            return "error/404";
        }
        vacancyService.deleteById(id);
        return "redirect:/vacancies";
    }
}
