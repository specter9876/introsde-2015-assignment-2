package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.model.MeasureDefinition;
import java.util.List;
import java.util.ArrayList;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.persistence.PersistenceUnit;


@Stateless
// will work only inside a Java EE application
@LocalBean
// will work only inside a Java EE application
@Path("/measureTypes")

public class MeasureTypesResource {
    // Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
    
	// will work only inside a Java EE application
	@PersistenceUnit(unitName = "introsde-jpa")
	EntityManager entityManager;
    // Application integration
    @GET
    @Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
    public List<MeasureDefinition> getMeasureType() {
        System.out.println("Getting list of measureTypes...");
        List<MeasureDefinition> measure = MeasureDefinition.getAll();
        return measure;
    }

  
}