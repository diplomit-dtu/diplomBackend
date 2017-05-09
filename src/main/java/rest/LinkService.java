package rest;

import data.LinkDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 09-05-2017.
 */
@Path("links")
@Produces(MediaType.APPLICATION_JSON)
public class LinkService {

    @GET
    @Path("default")
    public List<LinkDTO> getDefaultLinks(){
        ArrayList<LinkDTO> links = new ArrayList<>();
        links.add(new LinkDTO("campusnet", "http://cn.dtu.dk"));
        return links;
    }
}
