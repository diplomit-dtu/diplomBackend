package data;

import business.ControllerRegistry;
import business.impl.RoleControllerImpl;
import business.interfaces.RoleController;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteResult;
import config.Config;
import config.DeployConfig;
import data.dbDTO.BaseDTO;
import data.dbDTO.Role;
import data.dbDTO.User;
import data.interfaces.PersistenceException;
import data.interfaces.UserDAO;
import data.mongoImpl.MongoUserDAO;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import rest.ElementNotFoundException;
import rest.ValidException;

import java.util.List;

/**
 * Created by Christian on 11-05-2017.
 */
public class MorphiaHandler {
    private static MorphiaHandler morphiaHandler;
    private Morphia morphia;
    private Datastore datastore;
    MongoClientURI uri;
    MongoClient client;

    private MorphiaHandler() throws PersistenceException {
        initializeDataStore();
    }

    private void initializeDataStore() throws PersistenceException {
        if (DeployConfig.MONGODB_URI==null) {
            throw new PersistenceException("Environment variable: MONGODB_URI not set - contact Sysadmin");
        }
        uri = new MongoClientURI(DeployConfig.MONGODB_URI);
        client = new MongoClient(uri);
        morphia = new Morphia();
        morphia.mapPackage(Config.DATA_DB_DTO);
        datastore = morphia.createDatastore(client,getDbName(DeployConfig.MONGODB_URI));
        datastore.ensureIndexes();
        ensureSuperUser();
        //For nice cleanup of MongoDB connection - make sure that morphiaHandler get garbage collected.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> morphiaHandler=null));
    }

    private String getDbName(String mongodbUri) {
        String dbname = mongodbUri.split(":")[1];
        return dbname.substring(2);

    }

    private void ensureSuperUser() throws PersistenceException {
        String userString = DeployConfig.PORTAL_SUPER_USER;
        List<User> userList = datastore.createQuery(User.class)
                .field("userName").equal(userString).asList();
        RoleController roleController = ControllerRegistry.getRoleController();
        if (userList.size()<1){
            System.out.println("MorphiaHandle found no SuperUser - creating one");
            User portalAdmin = new User(userString);

            portalAdmin.getRoles().add(roleController.getPortalAdmin());
            createOrUpdate(portalAdmin);
        } else if (userList.size()>=1){
            User user = userList.get(0);
            boolean isSuperUser = false;
            for (Role role: user.getRoles()){
                if (role.getRoleName()==Config.PORTAL_ADMIN){
                    isSuperUser=true;
                }
            }
            if (!isSuperUser){
                user.getRoles().add(roleController.getPortalAdmin());
                createOrUpdate(user);
            }
        }
    }

    public static MorphiaHandler getInstance() throws PersistenceException {
        if (morphiaHandler==null) morphiaHandler= new MorphiaHandler();
        return morphiaHandler;
    }

    public Datastore getDatastore() throws PersistenceException {
        if (datastore==null){
            initializeDataStore();
        }
        return datastore;
    }

    public static Datastore getDS() throws PersistenceException {
        return getInstance().getDatastore();
    }
    public <T extends BaseDTO> T createOrUpdate(T dto) throws PersistenceException {
        getDatastore().save(dto);
        System.out.println(this.getClass() + ": dto :" + dto + dto.getId());
        return dto;
    }

    public <T> T getById(ObjectId objectId, Class<T> clazz) throws PersistenceException {
        return getDatastore().get(clazz,objectId);
    }

    public <T> T getById(String Id, Class<T> clazz) throws PersistenceException {

        return getDatastore().get(clazz,Id);
    }

    public <T> Boolean deleteById(ObjectId objectId, Class<T> clazz) throws PersistenceException {
        WriteResult delete = getDatastore().delete(clazz, objectId);
        return delete.getN()>=1;
    }


    public <T> List<T> getAll(Class<T> clazz){

        return datastore.find(clazz).asList();

    }


}
