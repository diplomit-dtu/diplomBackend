package data.interfaces;

import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by Christian on 11-05-2017.
 */
public interface BaseDAO <T>{
    T save(T element) throws PersistenceException;
    List<T> saveMultiple(List<T> elements) throws PersistenceException;
    T get(ObjectId oid) throws PersistenceException;
    List<T> getAll() throws PersistenceException;

    Boolean delete(ObjectId oid) throws PersistenceException;
}
