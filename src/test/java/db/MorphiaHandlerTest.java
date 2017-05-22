package db;

import data.MorphiaHandler;
import data.interfaces.PersistenceException;
import org.bson.types.ObjectId;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Christian on 15-05-2017.
 */

public class MorphiaHandlerTest {

    @Test
    public void TestCRUD() throws Exception, PersistenceException {
        int initialCount = getSize();
        //test one insert
        TestDTO test = MorphiaHandler.getInstance().createOrUpdate(new TestDTO());
        assertTrue(test.getObjectId()!=null);
        int testDTONO = getSize();
        assertTrue(getSize()==initialCount+1);
        //Test update
        ObjectId testID = test.getObjectId();
        test.setSomeText("someText");
        TestDTO test2 = MorphiaHandler.getInstance().createOrUpdate(test);
        assertTrue(testID.equals(test2.getObjectId()));
        assertTrue(testDTONO== getSize());
        assertTrue(getSize()==initialCount+1);
        assertTrue(MorphiaHandler.getInstance().deleteById(test2.getObjectId(),TestDTO.class));
        assertTrue(getSize()==initialCount);

    }

    private int getSize() throws PersistenceException {
        return MorphiaHandler.getInstance().getAll(TestDTO.class).size();
    }
}
