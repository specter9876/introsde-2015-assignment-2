package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.model.Person;
import introsde.rest.ehealth.model.HealthMeasureHistory;
import java.util.List;
import java.util.ArrayList;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Stateless // only used if the the application is deployed in a Java EE container
@LocalBean // only used if the the application is deployed in a Java EE container
public class PersonResource {
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    int id;

    EntityManager entityManager; // only used if the application is deployed in a Java EE container

    public PersonResource(UriInfo uriInfo, Request request,int id, EntityManager em) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = id;
        this.entityManager = em;
    }

    public PersonResource(UriInfo uriInfo, Request request,int id) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = id;
    }

    // Application integration
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response getPerson() {
        System.out.println("Request #2: GET /person/{id}");
        Person person = Person.getPersonById(id);
        if (person == null)
            return Response.status(404)
            .entity("Get: Person with " + id + " not found").build();
        System.out.println("Returning person... " + person.getIdPerson());
        return Response.ok(person).build();
    }
    // for the browser
    @GET
    @Produces(MediaType.TEXT_XML)
    public Person getPersonHTML() {
        Person person = this.getPersonById(id);
        if (person == null)
            throw new RuntimeException("Get: Person with " + id + " not found");
        System.out.println("Returning person... " + person.getIdPerson());
        return person;
    }

    @PUT
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Person putPerson(Person person) {
        System.out.println("--> Updating Person... " +this.id);
        System.out.println("--> "+person.toString());
        Person res;
        Person existing = Person.getPersonById(this.id);

        if (existing == null) {
            res = null;
        } else {
            
            person.setIdPerson(this.id);
            res = Person.updatePerson(person);
        }
        return res;
    }
    @DELETE
    public Response deletePerson() {
        Person c = Person.getPersonById(id);
        List<HealthMeasureHistory> hm= HealthMeasureHistory.getAll();
        
        Person p=null;
       
        List<HealthMeasureHistory> hmfinal= new ArrayList<>();
        for (HealthMeasureHistory rmd : hm) {
            p=rmd.getPerson();
            if (p.getIdPerson()==id){
                HealthMeasureHistory.removeHealthMeasureHistory(rmd);
            }
            
        }
        

        
        
        if (c == null ) {
            return Response
            .status(404)
            .entity("Person with the id " + id
                    + " is not present in the database").build();
        } else {
            Person.removePerson(c);
          
            return Response.status(204).build();
        }
    }
    
    
    public Person getPersonById(int personId) {
        System.out.println("Reading person from DB with id: "+personId);

        // this will work within a Java EE container, where not DAO will be needed
        //Person person = entityManager.find(Person.class, personId); 

        Person person = Person.getPersonById(personId);
        System.out.println("Person: "+person.toString());
        return person;
    }
}