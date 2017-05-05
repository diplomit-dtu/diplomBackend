package data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Christian on 05-05-2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseDTO {
    @JsonIgnore private String _id;
    public String getCollectionName(){ return this.getClass().getSimpleName();}

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
