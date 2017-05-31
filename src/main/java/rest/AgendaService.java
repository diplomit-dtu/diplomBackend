package rest;

import business.impl.AgendaControllerImpl;
import business.interfaces.AgendaController;
import data.dbDTO.StudentAgenda;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * Created by Christian on 31-05-2017.
 */
@Path("agendas")
public class AgendaService {
    AgendaController agendaController = new AgendaControllerImpl();

    @GET
    @Path("userid/{userid}/courseid/{courseid}")
    public StudentAgenda getAgendaByUser(@PathParam("userid") String userId, @PathParam("courseid") String courseId){

        return agendaController.getAgenda(courseId,userId);
    }

    @GET
    public StudentAgenda getAgenda(@QueryParam("courseId") String courseId){
        return null;
    }

}
