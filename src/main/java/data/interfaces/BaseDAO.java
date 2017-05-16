package data.interfaces;

import org.bson.types.ObjectId;
import rest.ValidException;

import java.util.List;

/**
 * Created by Christian on 11-05-2017.
 */
public interface BaseDAO <T>{
    T save(T element) throws PersistenceException;
    List<T> saveMultiple(List<T> elements) throws PersistenceException;
    T get(String oid) throws PersistenceException, ValidException;
    List<T> getAll() throws PersistenceException;

    Boolean delete(String oid) throws PersistenceException, ValidException;
}
