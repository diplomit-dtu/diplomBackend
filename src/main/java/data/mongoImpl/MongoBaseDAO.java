package data.mongoImpl;

import data.MorphiaHandler;
import data.interfaces.BaseDAO;
import data.interfaces.PersistenceException;
import org.bson.types.ObjectId;
import rest.ValidException;

import java.util.List;
import java.util.Objects;

/** Base Generic MongoDBImplementation
 * Created by Christian on 11-05-2017.
 */
public class MongoBaseDAO<T> implements BaseDAO<T> {
    private final Class<T> type;
    //Needed for persistence
    public MongoBaseDAO(Class<T> type) {
        this.type = type;
    }

    @Override
    public T save(T element) throws PersistenceException {
        if(element!=null) element =  MorphiaHandler.getInstance().createOrUpdate(element);
        return element;
    }

    /**
     * @param elements
     * Note: Removes null elements from list!
     * @return same elements - but with objectId
     */
    @Override
    public List<T> saveMultiple(List<T> elements) throws PersistenceException {
        try {
            elements.removeIf(Objects::isNull);
        } catch (UnsupportedOperationException e){
            throw new PersistenceException(e.getMessage());
        }

        MorphiaHandler.getInstance().getDatastore().save(elements);

        return elements;
    }

    @Override
    public T get(String oid) throws PersistenceException, ValidException {
        try {
            ObjectId objectId = new ObjectId(oid);
            return MorphiaHandler.getInstance().getById(objectId, type);
        } catch (IllegalArgumentException e){
            throw new ValidException("ObjectID not Valid: " + oid);
        }
    }

    @Override
    public List<T> findByField(String fieldName, String value) throws PersistenceException {
        List<T> ts = MorphiaHandler.getDS().createQuery(type)
                .field(fieldName).equal(value).asList();
        return ts;
    }

    @Override
    public List<T> getAll() throws PersistenceException {
        return MorphiaHandler.getInstance().getAll(type) ;
    }

    @Override
    public Boolean delete(String oid) throws PersistenceException, ValidException {
        try {
            ObjectId objectId = new ObjectId(oid);
            return MorphiaHandler.getInstance().deleteById(objectId,type);
        } catch (IllegalArgumentException e){
            throw new ValidException("ObjectID not Valid: " + oid);
        }

    }
}
