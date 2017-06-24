package data.viewDTO;

import data.dbDTO.CourseActivity;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Christian on 24-06-2017.
 */
@Data
public class ViewAgendaActivity {

    private String title;
    private String Description;
    private CourseActivity.ActivityStatus status = CourseActivity.ActivityStatus.VISIBLE;

    private Date startDate;
    private Date EndDate;
    private List<ViewAgendaActivityElement> activityElementList = new ArrayList<>();


}
