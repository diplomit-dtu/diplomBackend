package data.dbDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 11-05-2017.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CourseActivityElement extends BaseDTO {

    @Reference
    private List<CourseActivitySubElement> subElements = new ArrayList<>();
    private String hyperLink;
    private String GoogleSheetId;
    private String googleUniqueId;
    private String title;
    private transient Double progression;


    private ActivityElementType activityElementType = ActivityElementType.Native; //Pr default native

    public enum ActivityElementType{
        Native, Link, GoogleSheet, Text,Unavailable
    }

}
