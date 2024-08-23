package com.springmvc.springmvc.services;

import java.util.List;

public interface BaseService<T,ID> {
    List<T> findAll();
    T findById(ID id);
    void update(T dto);
    void delete(ID id);
}
