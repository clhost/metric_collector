package io.clhost.devopstask.database.service;

import java.util.List;

public interface GenericEntityService<T, E> {
    List<T> getAll() throws Exception;
    T get(E e, String column) throws Exception;
    void save(T t) throws Exception;
    void delete(E e, String column) throws Exception;
}
