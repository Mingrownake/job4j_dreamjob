package ru.job4j.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.model.Vacancy;
import ru.job4j.service.SimpleVacancyService;
import ru.job4j.service.VacancyService;

@Controller
@RequestMapping("/vacancies")
public class VacancyController {
    private final VacancyService vacancyService = SimpleVacancyService.getInstance();

    @GetMapping
    public String getList(Model model) {
        model.addAttribute("vacancies", vacancyService.findAll());
        return "vacancies/list";
    }

    @GetMapping("/create")
    public String getCreationPage() {
        return "vacancies/create";
    }

    @PostMapping("/create")
    public String postCreatePage(@ModelAttribute Vacancy vacancy) {
        vacancyService.save(vacancy);
        return "redirect:/vacancies";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var optionalVacancy = vacancyService.findById(id);
        if (optionalVacancy.isEmpty()) {
            model.addAttribute("message", "Пользователь с данным id не найден");
            return "error/404";
        }
        model.addAttribute("vacancy", optionalVacancy.get());
        return "vacancies/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Vacancy vacancy, Model model) {
        var isUpdateVacancy = vacancyService.update(vacancy);
        if (!isUpdateVacancy) {
            model.addAttribute("message", "Пользователь с данным id не найден");
            return "error/404";
        }
        return "redirect:/vacancies";
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
