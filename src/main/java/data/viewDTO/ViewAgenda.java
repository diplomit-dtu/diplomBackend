package data.viewDTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/** View Class that maps Agenda and Course from DB for nice presentation.
 * Created by Christian on 24-06-2017.
 */
@Data
public class ViewAgenda {
    private String AgendaId;
    private List<ViewAgendaActivity> activities = new ArrayList<>();


}
