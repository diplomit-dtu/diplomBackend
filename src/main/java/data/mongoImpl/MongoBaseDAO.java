package data.mongoImpl;

import data.MorphiaHandler;
import data.dbDTO.Link;
import data.dbDTO.User;
import data.interfaces.BaseDAO;
import data.interfaces.LinkDAO;
import data.interfaces.PersistenceException;
import org.bson.types.ObjectId;

import javax.xml.transform.sax.SAXSource;
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
    public T get(ObjectId oid) throws PersistenceException {
        return MorphiaHandler.getInstance().getById(oid, type);
    }

    @Override
    public List<T> getAll() throws PersistenceException {
        return MorphiaHandler.getInstance().getAll(type) ;
    }

    @Override
    public Boolean delete(ObjectId oid) throws PersistenceException {
        return MorphiaHandler.getInstance().deleteById(oid,type);
    }
}
