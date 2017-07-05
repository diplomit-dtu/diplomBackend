package rest;

import auth.*;
import business.impl.AgendaControllerImpl;
import business.impl.CourseControllerImpl;
import business.interfaces.AgendaController;
import business.interfaces.CourseController;
import data.ControllerRegistry;
import data.dbDTO.*;
import data.interfaces.AgendaDAO;
import data.interfaces.PersistenceException;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by Christian on 31-05-2017.
 */
@Path("agendas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AgendaService {
    private AgendaController agendaController = new AgendaControllerImpl();

    @Path("agendainfo")
    public AgendaInfoResource getAgendaInfo(){
        return new AgendaInfoResource();
    }

    @Context
    ContainerRequestContext requestContext;

    @Path("{id}")
    public AgendaResource getAgendaResource(@PathParam("id") String id){
        return new AgendaResource(id);
    }

    @Path("own/{id}")
    public OwnAgendaResource getOwnAgendaResource(@PathParam("id") String id){
        return new OwnAgendaResource(id);
    }

    public class OwnAgendaResource{
        private final String id;

        OwnAgendaResource(String id) {
            this.id = id;
        }

        @POST //For making your own agenda...
        public AgendaInfo createAgenda(AgendaInfo info) throws ValidException, PersistenceException, NotLoggedInException {
            System.out.println(requestContext.getProperty(AuthorizationFilter.USER));

            User userFromContext = UserUtil.getUserFromContext(requestContext);
            if (userFromContext==null) {
                throw new NotLoggedInException("AgendaService: User not logged in.");
            }
            return agendaController.createNewAgenda(info, userFromContext.getId());
        }
        @PUT
        @Path("metadata/{id}")
        public void updateMetaData(@PathParam("id") String subElementId, ElementMetaData metaData) throws ValidException, ElementNotFoundException, PersistenceException {
            AgendaController agendaController= ControllerRegistry.getAgendaController();
            Agenda agenda = agendaController.getAgenda(id);
            if (agenda!=null) {
                agenda.getMetaData().put(subElementId, metaData);
                agendaController.saveAgenda(agenda);
            }else {
                throw new ElementNotFoundException("Agenda not found");
            }

        }

    }


    //----
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public class AgendaInfoResource {

        @Path("template/{id}")
        @GET
        public AgendaInfo getTemplate(@PathParam("id") String Id) throws ValidException, PersistenceException {
            Course course = new CourseControllerImpl().getCourse(Id);
            AgendaInfo agendaInfo = new AgendaInfo();
            agendaInfo.setCourseName(course.getCourseName());
            return agendaInfo;
        }
    }
}
