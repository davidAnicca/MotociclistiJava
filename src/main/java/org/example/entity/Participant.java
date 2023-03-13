package org.example.entity;

import java.util.Objects;

public class Participant {
    private Integer code;
    private String name;
    private Integer teamCode;
    private Integer capacity;

    public Participant(Integer code, String name, Integer teamCode, Integer capacity) {
        this.code = code;
        this.name = name;
        this.teamCode = teamCode;
        this.capacity = capacity;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(Integer teamCode) {
        this.teamCode = teamCode;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant that)) return false;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
