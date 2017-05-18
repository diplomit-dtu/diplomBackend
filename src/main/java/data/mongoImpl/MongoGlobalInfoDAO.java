package data.mongoImpl;

import data.dbDTO.GlobalInfo;
import data.interfaces.GlobalInfoDAO;
import data.interfaces.PersistenceException;
import org.bson.types.ObjectId;
import rest.ValidException;

/**
 * Created by Christian on 18-05-2017.
 */
public class MongoGlobalInfoDAO extends MongoBaseDAO<GlobalInfo> implements GlobalInfoDAO {
    private final String InfoID = "591d8164c10b56a64cde4c02"; //RandomID for GlobalConfig
    public MongoGlobalInfoDAO() {
        super(GlobalInfo.class);
    }

    @Override
    public GlobalInfo getInfo() throws ValidException, PersistenceException {
        GlobalInfo globalInfo = get(InfoID);
        if (globalInfo==null){
            globalInfo=new GlobalInfo();
            globalInfo.setId(InfoID);
            globalInfo.setDefaultLinkCollectionId(new ObjectId().toHexString());
            save(globalInfo);
            System.out.println("globalInfoSaved");
        }
        return globalInfo;
    }
}
