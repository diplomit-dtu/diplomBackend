package data.dbDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import java.util.HashSet;
import java.util.Set;

/** BaseDTO class to handle resourceRights and transform id's
 * Created by Christian on 15-05-2017.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class BaseDTO {

    @Id
    @JsonIgnore
    ObjectId objectId;

    private Set<String> admins = new HashSet<>(); //UserIds that can CRUD
    private Set<String> viewers = new HashSet<>(); //UserIds that can R
    private RightsGroups rightsGroups = new RightsGroups(); //For future new rights

    public String getId(){
        return this.objectId==null?null:objectId.toHexString();
    }
    public void setId(String id){
        if(id!=null) this.objectId=new ObjectId(id);
    }
}
