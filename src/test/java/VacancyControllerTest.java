import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.controller.VacancyController;
import ru.job4j.dto.FileDto;
import ru.job4j.model.City;
import ru.job4j.model.Vacancy;
import ru.job4j.service.CityService;
import ru.job4j.service.VacancyService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VacancyControllerTest {
    private VacancyController vacancyController;
    private VacancyService vacancyService;
    private CityService cityService;
    private MultipartFile multipartFile;

    @BeforeEach
    public void initService() {
        vacancyService = mock(VacancyService.class);
        cityService = mock(CityService.class);
        multipartFile = new MockMultipartFile("image.jpeg", new byte[] {1, 2, 3});
        vacancyController = new VacancyController(vacancyService, cityService);
    }

    @Test
    public void whenRequestVacancyListPageThenGetPageWithVacancies() {
        var vacancies1 = new Vacancy(1, "test1", "desc1", LocalDateTime.now(), true, 1, 2);
        var vacancies2 = new Vacancy(2, "test2", "desc2", LocalDateTime.now(), true, 3, 4);
        var expectedValues = List.of(vacancies1, vacancies2);
        when(vacancyService.findAll()).thenReturn(expectedValues);

        var model = new ConcurrentModel();
        var view = vacancyController.getList(model);
        var actualVacancies = model.getAttribute("vacancies");

        assertThat(view).isEqualTo("vacancies/list");
        assertThat(actualVacancies).isEqualTo(expectedValues);
    }

    @Test
    public void whenRequestVacancyCreationPageThenGetPageWithCities() {
        City city1 = new City(1, "Москва");
        City city2 = new City(2, "Санкт-Петербург");
        City city3 = new City(3, "Киров");
        var expectedValues = List.of(city1, city2, city3);
        when(cityService.findAll()).thenReturn(expectedValues);

        var model = new ConcurrentModel();
        var view = vacancyController.getCreationPage(model);
        var actualCities = model.getAttribute("cities");

        assertThat(view).isEqualTo("vacancies/create");
        assertThat(actualCities).isEqualTo(expectedValues);
    }

    @Test
    public void whenPostVacancyWithFileThenSameDataAndRedirectToVacanciesPage() throws IOException {
        var vacancy = new Vacancy(1, "test1", "desc1", LocalDateTime.now(), true, 1, 2);
        var fileDto = new FileDto(multipartFile.getOriginalFilename(), multipartFile.getBytes());
        var vacancyArgumentCaptor = ArgumentCaptor.forClass(Vacancy.class);
        var fileDtoArgumentCaptor = ArgumentCaptor.forClass(FileDto.class);
        when(vacancyService.save(vacancyArgumentCaptor.capture(), fileDtoArgumentCaptor.capture())).thenReturn(vacancy);

        var model = new ConcurrentModel();
        var view = vacancyController.postCreatePage(vacancy, multipartFile, model);
        var actualVacancy = vacancyArgumentCaptor.getValue();
        var actualFileDto = fileDtoArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/vacancies");
        assertThat(actualVacancy).isEqualTo(vacancy);
        assertThat(fileDto).usingRecursiveComparison().isEqualTo(actualFileDto);
    }

    @Test
    public void whenSomeExceptionThrownThenGetErrorPageWithMessage() {
        var expectedException = new RuntimeException("Failed to write file");
        when(vacancyService.save(any(), any())).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = vacancyController.postCreatePage(new Vacancy(), multipartFile, model);
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("error/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenRequestVacancyIdThenPageOne() throws IOException {
        var vacancy = new Vacancy(1, "test1", "desc1", LocalDateTime.now(), true, 1, 1);

        when(vacancyService.findById(any(Integer.class))).thenReturn(Optional.of(vacancy));

        var model = new ConcurrentModel();
        var view = vacancyController.getById(model, vacancy.getId());

        assertThat(view).isEqualTo("vacancies/one");
    }

    @Test
    public void whenRequestNotExistVacancyIdThenPageError() {
        var vacancy = new Vacancy(1, "test1", "desc1", LocalDateTime.now(), true, 1, 1);

        when(vacancyService.findById(any(Integer.class))).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = vacancyController.getById(model, vacancy.getId());

        assertThat(view).isEqualTo("error/404");
    }
}
