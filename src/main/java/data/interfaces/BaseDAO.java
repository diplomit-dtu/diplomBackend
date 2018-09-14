package data.interfaces;

import data.dbDTO.BaseDTO;
import business.interfaces.ValidException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Christian on 11-05-2017.
 */
public interface BaseDAO <T extends BaseDTO>{
    T save(T element) throws PersistenceException;
    List<T> saveMultiple(List<T> elements) throws PersistenceException;

    List<T> getAll() throws PersistenceException;
    T get(String id) throws PersistenceException, ValidException;
    List<T> multiGet(Collection<String> ids) throws PersistenceException, ValidException;

    List<T> findByField(String fieldName, String value) throws PersistenceException;
    List<T> findByFields(Map<String, Object> fields) throws PersistenceException;

    int findByFieldAndUpdateField(String findField, Object findFieldValue, String updateField, Object newValue) throws PersistenceException;

    Boolean delete(String oid) throws PersistenceException, ValidException;


}
