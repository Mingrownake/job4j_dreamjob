package ru.job4j.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Candidate {
    private int id;
    private String name;
    private String description;
    private LocalDateTime creationDate = LocalDateTime.now();

    private boolean visible;
    private int cityId;

    public Candidate() {

    }

    public Candidate(int id, String name, String description, boolean visible, int cityId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.visible = visible;
        this.cityId = cityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDate;
    }

    public void setCreationDateTime(LocalDateTime localDateTime) {
        this.creationDate = localDateTime;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Candidate candidate = (Candidate) o;
        return id == candidate.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
