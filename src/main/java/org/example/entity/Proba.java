package org.example.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Proba {
    private Integer cod;
    private String name;
    private List<Participant> participanti;

    public Proba(Integer cod, String name) {
        this.cod = cod;
        this.name = name;
        this.participanti = new ArrayList<>();
    }

    public void addParticipant(Participant participant){
        this.participanti.add(participant);
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

    public List<Participant> getParticipanti() {
        return participanti;
    }

    public void setParticipanti(List<Participant> participanti) {
        this.participanti = participanti;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Proba proba)) return false;
        return Objects.equals(cod, proba.cod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cod);
    }
}
