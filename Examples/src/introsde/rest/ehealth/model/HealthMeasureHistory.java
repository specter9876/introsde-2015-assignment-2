package introsde.rest.ehealth.model;

import introsde.rest.ehealth.dao.LifeCoachDao;
import introsde.rest.ehealth.model.Person;
import introsde.rest.ehealth.model.MeasureDefinition;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.*;


/**
 * The persistent class for the "HealthMeasureHistory" database table.
 * 
 */
@Entity
@Table(name="HealthMeasureHistory")
@NamedQuery(name="HealthMeasureHistory.findAll", query="SELECT h FROM HealthMeasureHistory h")
//@NamedQuery(name="HealthMeasureHistory.findByPerson", query="SELECT e " + "FROM HealthMeasureHistory e " + "WHERE e.value = :field")
@XmlRootElement
public class HealthMeasureHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="sqlite_mhistory")
	@TableGenerator(name="sqlite_mhistory", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="HealthMeasureHistory")
	@Column(name="idMeasureHistory")
	private int idMeasureHistory;

	//@Temporal(TemporalType.DATE)
	@Column(name="timestamp")
	private String timestamp;

	@Column(name="value")
	private String value;
    
    @Column(name="idMeasureDefinition")
	private int idMeasureDefinition;
    

	@ManyToOne
	@JoinColumn(name = "idMeasureDef", referencedColumnName = "idMeasureDef")
	private MeasureDefinition measureDefinition;

	// notice that we haven't included a reference to the history in Person
	// this means that we don't have to make this attribute XmlTransient
	@ManyToOne
	@JoinColumn(name = "idPerson", referencedColumnName = "idPerson")
	private Person person;

	public HealthMeasureHistory() {
	}
    
    
    //@XmlTransient
	public int getIdMeasureHistory() {
		return this.idMeasureHistory;
	}
    public int getIdMeasureDefinition() {
		return this.idMeasureDefinition;
	}

	public void setIdMeasureHistory(int idMeasureHistory) {
		this.idMeasureHistory = idMeasureHistory;
	}
    public void setIdMeasureDefinition (int idMeasureDefinition) {
		this.idMeasureDefinition = idMeasureDefinition;
	}

	public String getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public MeasureDefinition getMeasureDefinition() {
	    return measureDefinition;
	}

	public void setMeasureDefinition(MeasureDefinition param) {
	    this.measureDefinition = param;
	}
    @XmlTransient
	public Person getPerson() {
	    return person;
	}

	public void setPerson(Person param) {
	    this.person = param;
	}

	// database operations
	public static HealthMeasureHistory getHealthMeasureHistoryById(int id) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
        em.getEntityManagerFactory().getCache().evictAll();
		HealthMeasureHistory p = em.find(HealthMeasureHistory.class, id);
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}
	
    
    public static List<HealthMeasureHistory> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
        em.getEntityManagerFactory().getCache().evictAll();
	    List<HealthMeasureHistory> list = em.createNamedQuery("HealthMeasureHistory.findAll", HealthMeasureHistory.class).getResultList();
	    LifeCoachDao.instance.closeConnections(em);
        
        HealthMeasureHistory healthtemp=null;
        for(int i=0;i<list.size();i++){
            healthtemp=list.get(i);
            System.out.println("===============================================================");
            System.out.println("IDMEASUREHISTORY:="+healthtemp.idMeasureHistory);
            System.out.println("IDPERSON:="+healthtemp.getPerson().getIdPerson());
            System.out.println("VALUE:="+healthtemp.value);
            System.out.println("===============================================================");
        }
	    return list;
	}
    
    
    
    
    
    
    public static List<HealthMeasureHistory> getHealthMeasureHistoryByType(int idPerson, int measureType) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
        em.getEntityManagerFactory().getCache().evictAll();
       // System.out.println("FLAAAAG");
	    List<HealthMeasureHistory> list = em.createNamedQuery("HealthMeasureHistory.findAll", HealthMeasureHistory.class).getResultList();
	    LifeCoachDao.instance.closeConnections(em);
        List<HealthMeasureHistory> resultlist =new ArrayList<HealthMeasureHistory>();
        List<HealthMeasureHistory> finallist =new ArrayList<HealthMeasureHistory>();
        //System.out.println("FLAAAAG");
        
        HealthMeasureHistory healthtemp=null;
        for(int i=0;i<list.size();i++){
            healthtemp=list.get(i);
            if(healthtemp.getPerson().getIdPerson()==idPerson){
                System.out.println("Trovato inserisco nella lista di ritorno");
                resultlist.add(healthtemp);
                System.out.println("contiene: " +resultlist.size());
            }
            System.out.println("looop");
        }
        System.out.println("measuretype: "+measureType);
        healthtemp=null;
        for(int i=0;i<resultlist.size();i++){
            System.out.println("FLAAAAG");
            healthtemp=resultlist.get(i);
            System.out.println("===============================================================");
            System.out.println("IDMEASUREHISTORY:="+healthtemp.idMeasureHistory);
            
            MeasureDefinition measure =healthtemp.getMeasureDefinition();
            
            System.out.println("IDMEASURTYPE:="+measure.getIdMeasureDef());
            System.out.println("IDMEASURTYPE:="+measureType);
            System.out.println("IDPERSON:="+healthtemp.getPerson().getIdPerson());
            System.out.println("VALUE:="+healthtemp.value);
            System.out.println("===============================================================");
            if(measure.getIdMeasureDef()==measureType){
            
                System.out.println("OK inserita");
                finallist.add(healthtemp);
            }
            
        }
    
        return finallist;
    }
    
    
    
    
    
    
    public static List<HealthMeasureHistory> getHealthMeasureHistoryByTypeByMid(int idPerson, int measureType, int mid) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
        em.getEntityManagerFactory().getCache().evictAll();
	    List<HealthMeasureHistory> list = em.createNamedQuery("HealthMeasureHistory.findAll", HealthMeasureHistory.class).getResultList();
	    LifeCoachDao.instance.closeConnections(em);
        List<HealthMeasureHistory> resultlist =new ArrayList<HealthMeasureHistory>();
        List<HealthMeasureHistory> finallist =new ArrayList<HealthMeasureHistory>();
        List<HealthMeasureHistory> lastllist =new ArrayList<HealthMeasureHistory>();

        
        HealthMeasureHistory healthtemp=null;
        for(int i=0;i<list.size();i++){
            healthtemp=list.get(i);
            if(healthtemp.getPerson().getIdPerson()==idPerson){
                
                resultlist.add(healthtemp);
            }
        }
        
        healthtemp=null;
        for(int i=0;i<resultlist.size();i++){
            healthtemp=resultlist.get(i);
            if(healthtemp.idMeasureDefinition==measureType){
                
                // System.out.println("OK inserita");
                finallist.add(healthtemp);
            }
            
        }
        healthtemp=null;
        for(int i=0;i<finallist.size();i++){
            healthtemp=finallist.get(i);
            if(healthtemp.idMeasureHistory==mid){
                
                // System.out.println("OK inserita");
                lastllist.add(healthtemp);
            }
            
        }
        
        return lastllist;
    }
    
    
    
  
    public static List<HealthMeasureHistory> getHealthMeasureHistoryPerPerson( int idPerson) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
        em.getEntityManagerFactory().getCache().evictAll();
	    List<HealthMeasureHistory> list = em.createNamedQuery("HealthMeasureHistory.findAll", HealthMeasureHistory.class).getResultList();
	    LifeCoachDao.instance.closeConnections(em);
        List<HealthMeasureHistory> resultlist =new ArrayList<HealthMeasureHistory>();
        
        HealthMeasureHistory healthtemp=null;
        for(int i=0;i<list.size();i++){
            healthtemp=list.get(i);
            System.out.println("===============================================================");
            System.out.println("IDMEASUREHISTORY:="+healthtemp.idMeasureHistory);
            System.out.println("IDPERSON:="+healthtemp.getPerson().getIdPerson());
            System.out.println("VALUE:="+healthtemp.value);
            System.out.println("===============================================================");
            if(healthtemp.getPerson().getIdPerson()==idPerson){
                System.out.println("OK inserita");
                resultlist.add(healthtemp);
            }
            
        }
        
	    return resultlist;
	}
	
    
    
    
    
    
    
    
	public static HealthMeasureHistory saveHealthMeasureHistory(HealthMeasureHistory p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
        em.getEntityManagerFactory().getCache().evictAll();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static HealthMeasureHistory updateHealthMeasureHistory(HealthMeasureHistory p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
        em.getEntityManagerFactory().getCache().evictAll();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p=em.merge(p);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static void removeHealthMeasureHistory(HealthMeasureHistory p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
        em.getEntityManagerFactory().getCache().evictAll();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    p=em.merge(p);
	    em.remove(p);
	    tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	}
}
