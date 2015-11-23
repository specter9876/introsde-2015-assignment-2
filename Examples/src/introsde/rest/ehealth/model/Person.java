package introsde.rest.ehealth.model;

import introsde.rest.ehealth.dao.LifeCoachDao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@Entity  // indicates that this class is an entity to persist in DB
@Table(name="Person") // to whole table must be persisted 
@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p")
@XmlRootElement
@XmlType(propOrder = { "idPerson","name", "lastname", "birthdate", "lifeStatus" ,"email", "username"})
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id // defines this attributed as the one that identifies the entity
    @GeneratedValue(generator="sqlite_person")
    @TableGenerator(name="sqlite_person", table="sqlite_sequence",
        pkColumnName="name", valueColumnName="seq",
        pkColumnValue="Person")
    @Column(name="idPerson")
    private int idPerson;
    @Column(name="lastname")
    private String lastname;
    @Column(name="name")
    private String name;
    @Column(name="username")
    private String username;
   // @Temporal(TemporalType.DATE) // defines the precision of the date attribute
    @Column(name="birthdate")
    private String birthdate;
    @Column(name="email")
    private String email;
    
    // mappedBy must be equal to the name of the attribute in LifeStatus that maps this relation
    @OneToMany(mappedBy="person",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    private List<LifeStatus> lifeStatus;
    
    @XmlElementWrapper(name = "HealthProfile")
    public List<LifeStatus> getLifeStatus() {
        return lifeStatus;
    }
    
    public void setLifeStatus(List<LifeStatus> lifeStatus) {
		this.lifeStatus = lifeStatus;
	}
    // add below all the getters and setters of all the private attributes
    
    // getters
    //@XmlTransient
    public int getIdPerson(){
        return idPerson;
    }

    public String getLastname(){
        return lastname;
    }
    public String getName(){
        return name;
    }
    public String getUsername(){
        return username;
    }
    public String getBirthdate(){
        return birthdate;
    }
    public String getEmail(){
        return email;
    }
    
    // setters
    public void setIdPerson(int idPerson){
        this.idPerson = idPerson;
    }
    public void setLastname(String lastname){
        this.lastname = lastname;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setBirthdate(String birthdate){
        this.birthdate = birthdate;
    }
    public void setEmail(String email){
        this.email = email;
    }
    
    public static Person getPersonById(int personId) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        em.getEntityManagerFactory().getCache().evictAll();
        Person p = em.find(Person.class, personId);
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

    public static List<Person> getAll() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        em.getEntityManagerFactory().getCache().evictAll();
        List<Person> list = em.createNamedQuery("Person.findAll", Person.class)
            .getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }

    public static Person savePerson(Person p) {
        
        if (p.getLifeStatus()!=null){
        System.out.println("===================================================");
        System.out.println("Name:= "+p.getName());
        System.out.println("SurName:= "+p.getLastname());
        List <LifeStatus> lf=p.getLifeStatus();
        System.out.println("Lifestatus:= "+lf.size());
        System.out.println("===================================================");
        }
        
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        em.getEntityManagerFactory().getCache().evictAll();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    } 

    public static Person updatePerson(Person p) {
        
       Person old=Person.getPersonById(p.getIdPerson());
 
    
        if(p.getName().equals(null)){
            p.setName(old.getName());
        }
        if(p.getLastname()==null){
            p.setLastname(old.getLastname());
        }
        if(p.getBirthdate()==null){
            p.setBirthdate(old.getBirthdate());
            
        }
        if(p.getEmail()==null){
            p.setEmail(old.getEmail());
            
        }
        if(p.getUsername()==null){
            p.setUsername(old.getUsername());
            
        }
     
        p.setLifeStatus(old.getLifeStatus());

        
        
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        em.getEntityManagerFactory().getCache().evictAll();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

    public static void removePerson(Person p) {
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