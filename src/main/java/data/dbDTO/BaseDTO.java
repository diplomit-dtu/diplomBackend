package data.dbDTO;

import lombok.Data;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by Christian on 15-05-2017.
 */
@Data
public class BaseDTO {
    @Id
    ObjectId objectId;

    public String getId(){
        return this.objectId==null?null:objectId.toHexString();
    }
    public void setId(String id){
        if(id!=null) this.objectId=new ObjectId(id);
    }
}
