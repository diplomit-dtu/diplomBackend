package rest.login;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import deployment.DeployConfig;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.ws.rs.*;

import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by Christian on 04-05-2017.
 */
@Path("mongodb")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class MongoDBTestService {
    protected static final String TEST_COLLECTION = "Test";
    MongoClientURI uri = new MongoClientURI(DeployConfig.MONGODB_URI);
    MongoClient client = new MongoClient(uri);

    @GET
    public String getTest(){

        MongoDatabase db = client.getDatabase(uri.getDatabase());
        MongoCollection<Document> test= db.getCollection(TEST_COLLECTION);
        Document first = test.find(new Document("test","test")).first();
        return first.toJson();

    }

    @POST
    public JsonNode postTest(JsonNode json){
        System.out.println(json.toString());
        MongoDatabase db = client.getDatabase(uri.getDatabase());
        MongoCollection<Document> test = db.getCollection(TEST_COLLECTION);
        if (test.find(new Document("test","test")).first()==null){
            test.insertOne(new Document("test","test"));
        }
        Document first = test.find(new Document("test", "test")).first();
        System.out.println(first);

        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map = mapper.convertValue(json,Map.class);
        Document newDoc = new Document(map);
        test.updateOne(new Document("test","test"),new Document("$set", new Document(map)));
        System.out.println(newDoc);
        return json;
    }

    @DELETE
        public static void main(String[] args) {
        MongoDBTestService service = new MongoDBTestService();
        System.out.println(service.getTest());
        JsonNode node = new POJONode(new String("test"));
        System.out.println(service.postTest(node));
    }
}

