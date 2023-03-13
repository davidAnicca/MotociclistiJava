package org.example.repo;

import java.util.List;

public interface Repo <T>{
    public List<T> getAll();
    public void add(T obj);
    public void remove(T obj);
    public void modify(T obj);

    public T search(T obj);
}
