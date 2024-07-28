package ru.job4j.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.model.Vacancy;
import ru.job4j.service.CityService;
import ru.job4j.service.SimpleVacancyService;
import ru.job4j.service.VacancyService;

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
        model.addAttribute("cities", cityService.findAll());
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
