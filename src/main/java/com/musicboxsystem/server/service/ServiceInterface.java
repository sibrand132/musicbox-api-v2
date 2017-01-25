package com.musicboxsystem.server.service;

import java.util.List;

/**
 * Created by Sibrand on 2017-01-05.
 */
public interface ServiceInterface<T> {

    List<T> getObj();
    T create(T obj);
    T findById(String id);
    T update(T obj, String id);
    void delete(String obj);

}
