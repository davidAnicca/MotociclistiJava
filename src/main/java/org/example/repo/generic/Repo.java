package org.example.repo.generic;

import jdk.jshell.spi.ExecutionControl;

import java.sql.SQLException;
import java.util.List;

public interface Repo <T>{
    public List<T> getAll() throws SQLException;
    public void add(T obj);
    public void remove(T obj);
    public void modify(T obj) throws ExecutionControl.NotImplementedException;

    public T search(T obj);
}
