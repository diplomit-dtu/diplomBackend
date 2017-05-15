package rest.login;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import config.DeployConfig;
import data.dbDTO.Link;
import org.bson.Document;

import javax.ws.rs.*;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/** Initial service for testing purposes
 * Created by Christian on 04-05-2017.
 */
@Deprecated
@Path("mongodb")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class MongoDBTestService {
    protected static final String TEST_COLLECTION = "Test";
    protected static final String LINK_COLLECTION = "Links";
    MongoClientURI uri = new MongoClientURI(DeployConfig.MONGODB_URI);
    MongoClient client = new MongoClient(uri);

    @GET
    public String getTest(){

        MongoDatabase db = client.getDatabase(uri.getDatabase());
        MongoCollection<Document> testCollection= db.getCollection(TEST_COLLECTION);
        MongoCursor<Document> cursor = testCollection.find().iterator();
        while (cursor.hasNext()){
            System.out.println(cursor.next().toString());
        }
        Document  first = testCollection.find(new Document("test","test")).first();
        return first.get("links").toString();

    }

    @POST
    @Path("links")
    public String postTest(Link link){
        MongoDatabase db = client.getDatabase(uri.getDatabase());
        MongoCollection<Document> links = db.getCollection(LINK_COLLECTION);
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.convertValue(link, Map.class);
        links.insertOne(new Document(map));
        return map.toString();
    }

    @DELETE
        public static void main(String[] args) {
        MongoDBTestService service = new MongoDBTestService();
        System.out.println(service.getTest());
        JsonNode node = new POJONode(new String("test"));
        System.out.println(service.postTest(null));
    }
}

