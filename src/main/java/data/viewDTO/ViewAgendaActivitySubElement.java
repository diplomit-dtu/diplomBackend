package data.viewDTO;

import data.dbDTO.CourseActivitySubElement;

/**
 * Created by Christian on 24-06-2017.
 */
public class ViewAgendaActivitySubElement {
    private String title;

    private String content;
    private CourseActivitySubElement.SubElementType subElementType;
    private String googleSheetId; //For elements in google sheets
    private String hyperLink;
}
