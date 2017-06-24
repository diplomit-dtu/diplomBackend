package rest;

import auth.Permission;
import auth.SecureEndpoint;
import business.impl.AgendaControllerImpl;
import business.interfaces.AgendaController;
import data.dbDTO.Agenda;
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

    @Path("{id}")
    public AgendaResource getAgendaResource(@PathParam("id") String id){
        return new AgendaResource(id);
    }

    @GET
    @Path("user/{id}")
    public Agenda getAgendaByUser(@PathParam("id") String id) throws ValidException, ElementNotFoundException, PersistenceException {

        return agendaController.getAgenda(id);
    }

    @GET
    public Agenda getAgenda(@QueryParam("courseId") String courseId){
        return null;
    }


    //----
    public class AgendaResource {

        private final String id;
        @GET
        public Agenda getAgenda() throws ValidException, ElementNotFoundException, PersistenceException {
            return agendaController.getAgenda(id);
        }

        public AgendaResource(String id) {
            this.id=id;
        }
    }
}
