package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.model.Person;
import introsde.rest.ehealth.model.LifeStatus;
import introsde.rest.ehealth.model.MeasureDefinition;
import introsde.rest.ehealth.model.HealthMeasureHistory;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



@Stateless // will work only inside a Java EE application
@LocalBean // will work only inside a Java EE application
@Path("/person")
public class PersonCollectionResource {

    // Allows to insert contextual objects into the class,
    // e.g. ServletContext, Request, Response, UriInfo
    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    // will work only inside a Java EE application
    @PersistenceUnit(unitName="introsde-jpa")
    EntityManager entityManager;

    // will work only inside a Java EE application
    @PersistenceContext(unitName = "introsde-jpa",type=PersistenceContextType.TRANSACTION)
    private EntityManagerFactory entityManagerFactory;

    // Return the list of people to the user in the browser
    @GET
    @Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
    public List<Person> getPersonsBrowser() {
        System.out.println("Getting list of people...");
        List<Person> people = Person.getAll();
        return people;
    }

    // retuns the number of people
    // to get the total number of records
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCount() {
        System.out.println("Getting count...");
        List<Person> people = Person.getAll();
        int count = people.size();
        return String.valueOf(count);
    }
    
    
    
    // retuns all HealthMEasureHistories
    @GET
    @Path("HealthMeasureHistory")
    @Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
    public List<HealthMeasureHistory> getHealthMeasureHistory() {
        System.out.println("Getting  HealthMeasureHistory...");
        List<HealthMeasureHistory> measure = HealthMeasureHistory.getAll();
        return measure;
    }
    
    // retuns all HealthMEasureHistories per person
    @GET
    @Path("{personId}/HealthMeasureHistoryPerson")
    @Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
    public List<HealthMeasureHistory> getHealthMeasureHistoryPerPerson(@PathParam("personId") int idPerson) {
        System.out.println("Getting  HealthMeasureHistory for a person...");
        List<HealthMeasureHistory> measure = HealthMeasureHistory.getHealthMeasureHistoryPerPerson(idPerson);
        return measure;
    }
    // retuns all HealthMEasureHistories per measure of person
    @GET
    @Path("{personId}/{measureType}")
    @Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
    public List<HealthMeasureHistory> getHealthMeasureHistoryByType(@PathParam("personId") int idPerson, @PathParam("measureType") int measureType) {
        System.out.println("Getting  HealthMeasureHistoryByType for a person...");
        List<HealthMeasureHistory> measure = HealthMeasureHistory.getHealthMeasureHistoryByType(idPerson,measureType);
        return measure;
        
    }
    
    // retuns all HealthMEasureHistories pby id measure of person
    @GET
    @Path("{personId}/{measureType}/{mid}")
    @Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
    public List<HealthMeasureHistory> getHealthMeasureHistoryByTypeByMid (@PathParam("personId") int idPerson, @PathParam("measureType") int measureType,  @PathParam("mid") int mid) {
        System.out.println("Getting  HealthMeasureHistoryByType for a person...");
        List<HealthMeasureHistory> measure = HealthMeasureHistory.getHealthMeasureHistoryByTypeByMid(idPerson,measureType, mid);
        return measure;
        
    }



    //Add a new person maybe with lifestatus
    @POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Person newPerson(Person person) throws IOException, ParseException {
		
		Person p = new Person();
		p.setName(person.getName());
		p.setLastname(person.getLastname());
		p.setBirthdate(person.getBirthdate());
		p.setEmail(person.getEmail());
		List<LifeStatus> lfList = new ArrayList<>();
        Person finalperson;
      
        System.out.println("lifestatus:="+person.getLifeStatus());
        int measureDefId=0;
        if (person.getLifeStatus()!=null){//start of controll
		for (LifeStatus lf : person.getLifeStatus()) {
			LifeStatus nls = new LifeStatus();
			MeasureDefinition md = lf.getMeasureDefinition();
            
			List<MeasureDefinition> mdlist = MeasureDefinition.getAll();
			for (MeasureDefinition rmd : mdlist) {
           
				if (rmd.getMeasureName().equals(md.getMeasureName())) {
					nls.setMeasureDefinition(rmd);
                    measureDefId=rmd.getIdMeasureDef();
                    
				}
			}
			nls.setValue(lf.getValue());
			nls.setPerson(p);
			lfList.add(nls);
		}
		p.setLifeStatus(lfList);
		finalperson = Person.savePerson(p);
		/////save lifestatus in health measure
        for (LifeStatus ls : lfList) {
			// and add this record in history
			HealthMeasureHistory record = new HealthMeasureHistory();
			record.setMeasureDefinition(ls.getMeasureDefinition());
			record.setPerson(finalperson);
                       
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            System.out.println(dateFormat.format(date));
           
			record.setTimestamp(dateFormat.format(date));
            record.setIdMeasureDefinition(ls.getMeasureDefinition().getIdMeasureDef());
			record.setValue(ls.getValue());
			HealthMeasureHistory.saveHealthMeasureHistory(record);
		}
        }//end of controll
        else{
            finalperson = Person.savePerson(person);
        }
        
		return finalperson;
	}
    
    
    //Add new measure for person
    @POST
    @Path("{personId}/{mid}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public LifeStatus newLifestatusForPerson(LifeStatus newlifestatus, @PathParam("personId") int idPerson, @PathParam("mid") int mid) throws IOException, ParseException {
      
        Person person=  Person.getPersonById(idPerson);
        List<LifeStatus> lifestatus= person.getLifeStatus();
        MeasureDefinition measure=null;
        
        for(LifeStatus lf : lifestatus){
            measure=lf.getMeasureDefinition();
            if(person.getIdPerson()==idPerson){
                if(measure.getIdMeasureDef()==mid){
                    LifeStatus.removeLifeStatus(lf);
                    
                    HealthMeasureHistory hlm =new HealthMeasureHistory();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = new Date();
                    hlm.setTimestamp(dateFormat.format(date));
                    hlm.setValue(newlifestatus.getValue());
                    hlm.setPerson(person);
                    hlm.setMeasureDefinition(measure);
                    hlm.setIdMeasureDefinition(measure.getIdMeasureDef());
                    
                    HealthMeasureHistory.saveHealthMeasureHistory(hlm);
                    
                    lf.setValue(newlifestatus.getValue());
                    LifeStatus.saveLifeStatus(lf);
                    
                }
            }
            
            
        }
        
        return newlifestatus;
        
    }
    
    
    

    // Defines that the next path parameter after the base url is
    // treated as a parameter and passed to the PersonResources
    // Allows to type http://localhost:599/base_url/1
    // 1 will be treaded as parameter todo and passed to PersonResource
    @Path("{personId}")
    public PersonResource getPerson(@PathParam("personId") int id) {
        return new PersonResource(uriInfo, request, id);
    }
}