package data.interfaces;

import rest.ValidException;

import java.util.List;
import java.util.Map;

/**
 * Created by Christian on 11-05-2017.
 */
public interface BaseDAO <T>{
    T save(T element) throws PersistenceException;
    List<T> saveMultiple(List<T> elements) throws PersistenceException;
    T get(String oid) throws PersistenceException, ValidException;

    List<T> findByField(String fieldName, String value) throws PersistenceException;

    List<T> findByFields(Map<String, Object> fields) throws PersistenceException;

    List<T> getAll() throws PersistenceException;

    Boolean delete(String oid) throws PersistenceException, ValidException;
}
