package rest;

import business.impl.AgendaControllerImpl;
import business.interfaces.AgendaController;
import data.dbDTO.StudentAgenda;
import data.interfaces.PersistenceException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * Created by Christian on 31-05-2017.
 */
@Path("agendas")
public class AgendaService {
    private AgendaController agendaController = new AgendaControllerImpl();

    @GET
    @Path("id/{id}")
    public StudentAgenda getAgendaByUser(@PathParam("id") String id) throws ValidException, ElementNotFoundException, PersistenceException {

        return agendaController.getAgenda(id);
    }

    @GET
    public StudentAgenda getAgenda(@QueryParam("courseId") String courseId){
        return null;
    }

}
