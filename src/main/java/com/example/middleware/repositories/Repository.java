package com.example.middleware.repositories;

import java.util.List;

public interface Repository<T> {
    List<T> getAll();
    T getById(int id);
    void add(T entity);
    void update(T entity);
    void remove(T entity);
}
