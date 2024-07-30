package ru.job4j.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.model.Vacancy;
import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oVacancyRepository implements VacancyRepository {
    private final Sql2o sql2o;

    public Sql2oVacancyRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        try (Connection con = sql2o.open()) {
            var sql = """
                    insert into vacancies(title, description, creation_date, visible, city_id, file_id)
                    values (:title, :description, :creationDate, :visible, :cityId, :fileId)
                    """;
            var query = con.createQuery(sql, true)
                    .addParameter("title", vacancy.getTitle())
                    .addParameter("description", vacancy.getDescription())
                    .addParameter("creationDate", vacancy.getCreationDate())
                    .addParameter("visible", vacancy.getVisible())
                    .addParameter("cityId", vacancy.getCityId())
                    .addParameter("fileId", vacancy.getFileId());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            vacancy.setId(generatedId);
            return vacancy;
        }
    }

    @Override
    public void deleteById(int id) {
        try (Connection con = sql2o.open()) {
            var query = con.createQuery("DELETE FROM vacancies WHERE id = :id");
            query.addParameter("id", id).executeUpdate();
        }
    }

    @Override
    public boolean update(Vacancy vacancy) {
        try (Connection con = sql2o.open()) {
            var sql = """
                    update vacancies 
                    set title = :title, description = :description, creation_date = :creationDate, 
                    visible = :visible, city_id = :cityId, file_id = :fileId
                    where id = :id
                    """;
            var query = con.createQuery(sql)
                    .addParameter("title", vacancy.getTitle())
                    .addParameter("description", vacancy.getDescription())
                    .addParameter("creationDate", vacancy.getCreationDate())
                    .addParameter("visible", vacancy.getVisible())
                    .addParameter("cityId", vacancy.getCityId())
                    .addParameter("fileId", vacancy.getFileId())
                    .addParameter("id", vacancy.getId());
            var affectsRow = query.executeUpdate().getResult();
            return affectsRow > 0;
        }
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        try (Connection con = sql2o.open()) {
            var query = con.createQuery("select * from vacancies where id = :id");
            query.addParameter("id", id);
            var vacancy = query.setColumnMappings(Vacancy.COLUMN_MAPPING).executeAndFetchFirst(Vacancy.class);
            return Optional.ofNullable(vacancy);
        }
    }

    @Override
    public Collection<Vacancy> findAll() {
        try (Connection con = sql2o.open()) {
            var query = con.createQuery("select * from vacancies");
            return query.setColumnMappings(Vacancy.COLUMN_MAPPING).executeAndFetch(Vacancy.class);
        }
    }
}
