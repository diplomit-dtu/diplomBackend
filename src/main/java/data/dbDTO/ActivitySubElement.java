package data.dbDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashMap;
import java.util.Map;

/** Actual Contents of course activities
 * Created by Christian on 11-05-2017.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ActivitySubElement extends BaseDTO{
    private String title;

    private String content;
    private SubElementType subElementType;
    private String googleSheetId; //For elements in google sheets
    private String hyperLink;


    public enum SubElementType{
        Text, Embedded_Link, Pop_Out_Link, Quiz, Concept_Question, Code
    }
}
