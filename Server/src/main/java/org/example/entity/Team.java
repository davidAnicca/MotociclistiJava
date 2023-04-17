package org.example.entity;

import java.util.Objects;

public class Team {
    private Integer code;
    private String name;

    public Team(Integer code, String name) {
        this.code = code;
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team team)) return false;
        return Objects.equals(code, team.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
