package ru.job4j.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.model.Candidate;
import ru.job4j.model.Vacancy;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class Sql2oCandidateRepository implements CandidateRepository {
    private final Sql2o sql2o;

    public Sql2oCandidateRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Candidate save(Candidate candidate) {
        try (Connection con = sql2o.open()) {
            var sql = """
                    insert into candidaties(name, description, creation_date, visible, city_id, file_id)
                    values (:name, :description, :creationDate, :visible, :cityId, :fileId)
                    """;
            var query = con.createQuery(sql, true)
                    .addParameter("name", candidate.getName())
                    .addParameter("description", candidate.getDescription())
                    .addParameter("creationDate", candidate.getCreationDateTime())
                    .addParameter("visible", candidate.getVisible())
                    .addParameter("cityId", candidate.getCityId())
                    .addParameter("fileId", candidate.getFileId());
            int generateId = query.executeUpdate().getKey(Integer.class);
            candidate.setId(generateId);
            return candidate;
        }
    }

    @Override
    public void deleteById(int id) {
        try (Connection con = sql2o.open()) {
            var query = con.createQuery("DELETE FROM candidaties WHERE id = :id")
                    .addParameter("id", id).executeUpdate();
        }
    }

    @Override
    public boolean update(Candidate candidate) {
        try (Connection con = sql2o.open()) {
            var sql = """
                    UPDATE candidaties SET name = :name, description = :description, creation_date = :creationDate,
                    visible = :visible, city_id = :cityId, file_id = :fileId
                    where id = :id
                    """;
            var query = con.createQuery(sql)
                    .addParameter("name", candidate.getName())
                    .addParameter("description", candidate.getDescription())
                    .addParameter("creationDate", candidate.getCreationDateTime())
                    .addParameter("visible", candidate.getVisible())
                    .addParameter("cityId", candidate.getCityId())
                    .addParameter("fileId", candidate.getFileId())
                    .addParameter("id", candidate.getId());
            var affectsRow = query.executeUpdate().getResult();
            return affectsRow > 0;
        }
    }

    @Override
    public Optional<Candidate> findById(int id) {
        try (Connection con = sql2o.open()) {
            var sql = "select * from candidaties where id = :id";
            var query = con.createQuery(sql)
                    .addParameter("id", id);
            var candidate = query.setColumnMappings(Candidate.COLUMN_MAPPING).executeAndFetchFirst(Candidate.class);
            return Optional.ofNullable(candidate);
        }
    }

    @Override
    public Collection<Candidate> findAll() {
        try (Connection con = sql2o.open()) {
            var sql = "select * from candidaties";
            var query = con.createQuery(sql);
            return query.setColumnMappings(Candidate.COLUMN_MAPPING).executeAndFetch(Candidate.class);
        }
    }
}
