package data.dbDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Christian on 15-05-2017.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class BaseDTO {

    @Id
    @JsonIgnore
    ObjectId objectId;

    private Set<String> Admins = new HashSet<>();

    public String getId(){
        return this.objectId==null?null:objectId.toHexString();
    }
    public void setId(String id){
        if(id!=null) this.objectId=new ObjectId(id);
    }
}
