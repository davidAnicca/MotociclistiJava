package org.example.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Probe {
    private Integer cod;
    private String name;

    public Probe() {
        // Constructor implicit
    }

    @JsonCreator
    public Probe(@JsonProperty("cod") Integer cod, @JsonProperty("name") String name) {
        this.cod = cod;
        this.name = name;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
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
        if (!(o instanceof Probe proba)) return false;
        return Objects.equals(cod, proba.cod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cod);
    }
}
