package data.mongoImpl;

import data.MorphiaHandler;
import data.dbDTO.BaseDTO;
import data.interfaces.BaseDAO;
import data.interfaces.PersistenceException;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import rest.ValidException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Base Generic MongoDBImplementation
 * Created by Christian on 11-05-2017.
 */
public class MongoBaseDAO<T extends BaseDTO> implements BaseDAO<T> {
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
    public T get(String id) throws PersistenceException, ValidException {
        try {
            ObjectId objectId = new ObjectId(id);
            return MorphiaHandler.getInstance().getById(objectId, type);
        } catch (IllegalArgumentException e){
            throw new ValidException("ObjectID not Valid: " + id);
        }
    }

    @Override
    public List<T> findByField(String fieldName, String value) throws PersistenceException {
        List<T> ts = MorphiaHandler.getDS().createQuery(type)
                .field(fieldName).equal(value).asList();
        return ts;
    }

    @Override
    public List<T> findByFields(Map<String, Object> fields) throws PersistenceException {
        Query<T> query = MorphiaHandler.getDS().createQuery(type);
        Set<Map.Entry<String, Object>> entries = fields.entrySet();
        for (Map.Entry<String,Object> entry: entries) {
            query.field(entry.getKey()).equals(entry.getValue());
        }
        return query.asList();
    }

    @Override
    public int findByFieldAndUpdateField(String findField, Object findFieldValue, String updateField, Object newValue) throws PersistenceException {
        Datastore datastore = MorphiaHandler.getDS();
        Query<T> query = datastore.createQuery(type).field(findField).equal(findFieldValue);
        UpdateOperations<T> updateOp = datastore.createUpdateOperations(type).set(updateField, newValue);
        UpdateResults updateResults = datastore.update(query, updateOp);
        return updateResults.getInsertedCount();
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
