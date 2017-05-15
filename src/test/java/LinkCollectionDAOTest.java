import data.dbDTO.Link;
import data.dbDTO.LinkCollection;
import data.interfaces.LinkCollectionDAO;
import data.interfaces.LinkDAO;
import data.interfaces.PersistenceException;
import data.mongoImpl.MongoLinkCollectionDAO;
import data.mongoImpl.MongoLinkDAO;
import org.junit.Test;

import static junit.framework.TestCase.fail;

/**
 * Created by Christian on 11-05-2017.
 */
public class LinkCollectionDAOTest {


    @Test
    public void testCreate(){
        Link link = new Link("test","test");
        LinkDAO linkDAO = new MongoLinkDAO();
        LinkCollection linkCollection = new LinkCollection("02324F17");
        linkCollection.getLinks().add(link);
        linkCollection.getLinks().add(null);
        LinkCollectionDAO linkCollectionDAO = new MongoLinkCollectionDAO();
        try {
            linkCollectionDAO.save(linkCollection);
        } catch (PersistenceException e) {
            fail();
        }
    }
}
