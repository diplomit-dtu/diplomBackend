package data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import deployment.DeployConfig;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Map;

/**
 * Base Class for Crud operations
 * Created by Christian on 05-05-2017.
 */
public class AbstractCRUDMongoDAO {
    MongoClientURI uri = new MongoClientURI(DeployConfig.MONGODB_URI);
    MongoClient client = new MongoClient(uri);

    //Create
    public <T extends BaseDTO> T simpleSave(T element) {
        MongoCollection<Document> coll = getCollection(element);
        Document doc = mapObjectToDoc(element);
        coll.insertOne(doc);
        System.out.println(doc);
        element.set_id(doc.get("_id").toString());
        return element;
    }

    //Read
    public <T extends BaseDTO> T simpleGet(Class<T> clazz, String id) throws IllegalAccessException, InstantiationException {
        MongoCollection<Document> coll = getCollection(clazz.newInstance());
        Document doc = coll.find(new Document("_id", new ObjectId(id))).first();
        return mapDocToObject(doc, clazz);
    }

    public <T extends BaseDTO> T simpleUpdate(T element, String id) {
        Class clazz = element.getClass();
        Document doc = mapObjectToDoc(element);
        MongoCollection<Document> coll = getCollection(element);
        coll.replaceOne(new Document("_id", new ObjectId(id)), doc);
        return mapDocToObject(doc, (Class<T>) clazz);
    }

    private <T extends BaseDTO> T mapDocToObject(Document doc, Class<T> clazz) {
        String json = doc.toJson();
        try {
            T element = getMapper().readValue(json, clazz);
            element.set_id(doc.get("_id").toString());
            return element;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T extends BaseDTO> Document mapObjectToDoc(T element) {
        return new Document(mapObject(element));
    }


    private <T extends BaseDTO> Map<String, Object> mapObject(T element) {
        return getMapper().convertValue(element, Map.class);
    }

    private ObjectMapper getMapper() {
        return new ObjectMapper();
    }

    private <T extends BaseDTO> MongoCollection<Document> getCollection(T type) {
        MongoDatabase database = client.getDatabase(uri.getDatabase());
        return database.getCollection(type.getCollectionName());
    }

    public static void main(String[] args) {
        AbstractCRUDMongoDAO baseDao = new AbstractCRUDMongoDAO();
        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setHref("test");
        LinkDTO savedDTO = baseDao.simpleSave(linkDTO);

        try {
            LinkDTO obj = baseDao.simpleGet(LinkDTO.class, savedDTO.get_id());
            System.out.println(obj);
            obj.setHref("retest");
            baseDao.simpleUpdate(linkDTO,linkDTO.get_id());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
