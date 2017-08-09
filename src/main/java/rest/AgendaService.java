package rest;

import auth.*;
import business.impl.AgendaControllerImpl;
import business.impl.CourseControllerImpl;
import business.interfaces.AgendaController;
import business.ControllerRegistry;
import data.dbDTO.*;
import data.interfaces.PersistenceException;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.Element;
import java.util.ArrayList;

/**
 * Created by Christian on 31-05-2017.
 */
@Path("agendas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AgendaService {
    private AgendaController agendaController = new AgendaControllerImpl();


    @Context
    ContainerRequestContext requestContext;

    @GET
    @Path("template")
    public Agenda getAgendaTemplate(){
        Agenda agenda = new Agenda();
        agenda.setCourseLinks(new ArrayList<>());
        return agenda;
    }
    @POST
    public Agenda createAgenda(Agenda agenda) throws PersistenceException {
        return agendaController.saveAgenda(agenda);
    }


    @Path("agendainfo")
    public AgendaInfoResource getAgendaInfo(){
        return new AgendaInfoResource();
    }

    @Path("{id}")
    public AgendaIdResource getAgendaIdResource(@PathParam("id") String id){
        return new AgendaIdResource(id);
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
                agenda.getElementMetaData().put(subElementId, metaData);
                agendaController.saveAgenda(agenda);
            }else {
                throw new ElementNotFoundException("Agenda not found");
            }

        }

    }


    //----
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public class AgendaIdResource {
        private final String id;
        public AgendaIdResource(String id) {
            this.id=id;
        }


        @PUT
        public Agenda updateAgenda(Agenda agenda) throws PersistenceException {
            agenda.setId(this.id);
            return agendaController.saveAgenda(agenda);
        }

        @Path("metadatas/{id}")
        public AgendaMetaDataIdResource getMetaDataResource(@PathParam("id") String id){
            return new AgendaMetaDataIdResource(id, this.id);
        }


        @GET
        public Agenda getAgenda() throws ValidException, ElementNotFoundException, PersistenceException {
            return agendaController.getAgenda(id);
        }



        private class AgendaMetaDataIdResource {
            private final String id;
            private final String parentId;

            public AgendaMetaDataIdResource(String id, String parentId) {
                this.id = id;
                this.parentId=parentId;
            }

            @POST
            public void postProgress(ElementMetaData metaData){
                //TODO for faster access
            }

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
