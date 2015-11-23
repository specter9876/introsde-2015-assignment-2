//package introsde.rest.ehealth.HelperClient;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Entity;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.IOException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.ws.rs.core.MediaType;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import org.glassfish.jersey.client.ClientConfig;
import javax.xml.bind.JAXBException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public class HelperClient {
    
    ClientConfig clientConfig =null;
    Client client =null;
    WebTarget service=null;
    
	public HelperClient() {
	
        clientConfig = new ClientConfig();
        client = ClientBuilder.newClient(clientConfig);
        service = client.target(getBaseURI());
        
    }
    
    
    public void  putmethod() throws ParserConfigurationException,
    SAXException, IOException, TransformerException{
    
        String result="";
        int count=0;
        String  responseCode="";
        String url = getBaseURI() + "/person/1";
        String accept = MediaType.APPLICATION_JSON;
        String contentType = "application/json";
        System.out.println("========================================================================");
        System.out.println("========================================================================");
      
        //For JSON
      
        String bodyj = "{\"name\": \"PAVEL\"}";
        System.out.println("Request #3.3: PUT " + url + " Accept: " + accept+ " Content-type: " + contentType);
        
        Response response = service.path("person/1").request().accept(MediaType.APPLICATION_JSON).put(Entity.json(bodyj));
        String resp = response.readEntity(String.class);
     
        //easy way to looking dor the new name in response
        count=TestClient.countStringOccurence(resp, "PAVEL");
        responseCode = ""+response.getStatus();
        if (count>0 ) {
            result = "OK";
        }
        else {
            result = "ERROR";
        }
        System.out.println("=> Result: " + result);
        System.out.println("=> HTTP Status: " + responseCode);
      
      
        System.out.println(formatJSON(resp));

      
      
        System.out.println("============================================");
      
        //For XML
      
        url = getBaseURI() + "/person/1";
        accept = MediaType.APPLICATION_XML;
        contentType = "application/xml";
      
        bodyj = "<person><name>PAVEL</name></person>";
        System.out.println("Request #3.3: PUT " + url + " Accept: " + accept+ " Content-type: " + contentType);
      
        response = service.path("person/1").request().accept(MediaType.TEXT_XML).put(Entity.xml(bodyj));
        resp = response.readEntity(String.class);
        result="";
      
      
        String name="";
        String namecontrol="PAVEL";
      
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(resp));
      
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(is);
        NodeList nodes = doc.getElementsByTagName("person");
      
        //looking for the changed name in response
        Node first_node = nodes.item(0);
      
        NodeList fn_children = first_node.getChildNodes();
        for (int i = 0; i < fn_children.getLength(); i++) {
            Node item = fn_children.item(i);
            if (!(item.getNodeName() == "#text")) {
                if (item.getNodeName().equals("name")) {
                    name = item.getTextContent();
                }
            }
        }
        System.out.println("namechange:= "+name);
        responseCode = ""+response.getStatus();
        if (namecontrol.equals(name)) {
            result = "OK";
        }
        else {
            result = "ERROR";
        }
        System.out.println("=> Result: " + result);
        System.out.println("=> HTTP Status: " + responseCode);
        System.out.println(formatXML(resp));
    }
    
    
    
    public int postPerson()throws ParserConfigurationException,
    SAXException, IOException, TransformerException{
        
        System.out.println("========================================================================");
        System.out.println("========================================================================");
        //For JSON
        String url = getBaseURI() + "/person";
		String accept = MediaType.APPLICATION_JSON;
        String result="";
		String contentType = "application/json";
		System.out.println("Request #3.4: POST " + url + " Accept: " + accept+ " Content-type: " + contentType);
        String resp="";
        //body of the request
        String bodyj ="{"
         +   "\"lastname\": \"Chuck\","
         +   "\"name\": \"Norris\","
         +   "\"birthdate\": \"1945-01-01\","
         +     "\"lifeStatus\": ["
                              +"{"
                                +"\"value\": \"78.9\","
                                +"\"measureDefinition\": {"
                                    +"\"measureName\": \"weight\""
                                +"}"
                              +"},"
                              +"{"
                              +"\"value\": \"172\","
                              +"\"measureDefinition\": {"
                              +"\"measureName\": \"height\""
                            +"}"
                            +"}"
                        +"]"
                + "}";
		
        Response response = service.path("person/").request().accept(accept)
        .post(Entity.json(bodyj));
		resp = response.readEntity(String.class);
		String responseCode = ""+response.getStatus();
		if (responseCode.equals("200") || responseCode.equals("201")|| responseCode.equals("202")) {
			result = "OK";
		} else {
			result = "ERROR";
		}
		System.out.println("=> Result: " + result);
		System.out.println("=> HTTP Status: " + responseCode);
		System.out.println(formatJSON(resp));
        
        ////For XML
        url = getBaseURI() + "/person";
		accept = MediaType.APPLICATION_XML;
        result="";
        contentType = "application/xml";
		System.out.println("Request #3.4: POST " + url + " Accept: " + accept+ " Content-type: " + contentType);
        resp="";
        
        bodyj = "<person><birthdate>1945-01-01</birthdate>"
         +"<lastname>Norris</lastname>"
         +"<HealthProfile>"
         +"<lifeStatus>"
         +"<measureDefinition>"
         +"<measureName>weight</measureName>"
         +"</measureDefinition>"
         +"<value>78.9</value>"
         +"</lifeStatus>"
         +"<lifeStatus>"
         +"<measureDefinition>"
        +"<measureName>height</measureName>"
         +"</measureDefinition>"
         +"<value>172</value>"
         +"</lifeStatus>"
         +"</HealthProfile>"
         +"<name>Chuck</name>"
        +"</person>";
        
        response = service.path("person/").request().accept(accept)
        .post(Entity.xml(bodyj));
		resp = response.readEntity(String.class);
        responseCode = ""+response.getStatus();
		if (responseCode.equals("200") || responseCode.equals("201")|| responseCode.equals("202")) {
			result = "OK";
		} else {
			result = "ERROR";
		}
		System.out.println("=> Result: " + result);
		System.out.println("=> HTTP Status: " + responseCode);
		System.out.println(formatXML(resp));
        
        int id=0;
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(resp));
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);
		NodeList nodes = doc.getElementsByTagName("person");
		// flooking for new id in response
		Node first_node = nodes.item(0);
		NodeList fn_children = first_node.getChildNodes();
		for (int i = 0; i < fn_children.getLength(); i++) {
			Node item = fn_children.item(i);
			if (!(item.getNodeName() == "#text")) {
				if (item.getNodeName().equals("idPerson")) {
					id = Integer.parseInt(item.getTextContent());
				}
			}
		}


        System.out.println("===================NEW ID===============================");
        System.out.println("id after POST: "+id);
        return id;
    }
    
    
    
    public void deletePerson(int id) throws ParserConfigurationException, SAXException, IOException, TransformerException{
        
        System.out.println("========================================================================");
        System.out.println("========================================================================");
        
    
        String url = getBaseURI() + "/person/"+id;
        String result="";
		System.out.println("Request #3.5: DELETE " + url + " Accept: " + "none"+ " Content-type: " + "none");
        String resp="";
        
        Response response = service.path("person/"+id).request().delete();
		resp = response.readEntity(String.class);
        String responseCode = ""+response.getStatus();
		if (responseCode.equals("204") || responseCode.equals("201")|| responseCode.equals("202")) {
			result = "OK";
		} else {
			result = "ERROR";
		}
		System.out.println("=> Result: " + result);
		System.out.println("=> HTTP Status: " + responseCode);
     }
    
    
    
    public void getdeletePerson(int id) throws ParserConfigurationException, SAXException, IOException, TransformerException{
        
        System.out.println("========================================================================");
        
        String url = getBaseURI() + "/person/"+id;
		String accept = MediaType.APPLICATION_XML;
        String result="";
		String contentType = "application/xml";
		System.out.println("Request #3.5: GET " + url + " Accept: " + accept + " Content-type: " + contentType);
        String resp="";
        

        ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI());
     
        
        Response response = service.path("person/"+id).request().accept(MediaType.APPLICATION_XML).get();
		resp = response.readEntity(String.class);
        String responseCode = ""+response.getStatus();
        
		if (responseCode.equals("404")) {
			result = "OK";
		} else {
			result = "ERROR";
		}
		System.out.println("=> Result: " + result);
		System.out.println("=> HTTP Status: " + responseCode);
        
        System.out.println("Not found");
    }
    
    
   
    public void finalPost (int count, int firstpersonid, int measure)throws ParserConfigurationException,
    SAXException, IOException, TransformerException{
        
        System.out.println("========================================================================");
        System.out.println("========================================================================");
        
        //For json
        String url = getBaseURI()+"/person/"+firstpersonid+"/"+measure;
		String accept = MediaType.APPLICATION_JSON;
        String result="";
		String contentType = "application/json";
		System.out.println("Request #3.9: POST " + url + " Accept: " + accept+ " Content-type: " + contentType);
        String resp="";
        
        String bodyj = "{"
                            +"\"value\": \"58.9\""
                      +"}";
        
        Response response = service.path("person/"+firstpersonid+"/"+measure).request().accept(accept)
        .post(Entity.json(bodyj));
		resp = response.readEntity(String.class);
        
		String responseCode = ""+response.getStatus();
		if (responseCode.equals("200") || responseCode.equals("201")|| responseCode.equals("202")) {
			result = "OK";
		} else {
			result = "ERROR";
		}
		System.out.println("=> Result: " + result);
		System.out.println("=> HTTP Status: " + responseCode);
		System.out.println(formatJSON(resp));
        
        
        System.out.println("========================================================================");
        
        url = getBaseURI() + "/person/"+firstpersonid+"/"+measure;
        accept = MediaType.APPLICATION_JSON;
        result="";
        contentType = "application/json";
		System.out.println("Request #3.9: GET " + url + " Accept: " + accept + " Content-type: " + contentType);
        resp="";
        
        clientConfig = new ClientConfig();
        client = ClientBuilder.newClient(clientConfig);
		service = client.target(getBaseURI());
        
        response = service.path("/person/"+firstpersonid+"/"+measure).request().accept(MediaType.APPLICATION_JSON).get();
		resp = response.readEntity(String.class);
        responseCode = ""+response.getStatus();
        
        int count2=0;
        count2=TestClient.countStringOccurence(resp, "\"idMeasureDefinition\"");
    
        
        if(count2-count==1){
            result="OK";
        }
        else{
            result="ERROR";
        }
        
        System.out.println("=> Result: " + result);
		System.out.println("=> HTTP Status: " + responseCode);
        System.out.println(formatJSON(resp));
        

        
        System.out.println("========================================================================");
        
        //For XML
        count=count2;
        url = getBaseURI()+"/person/"+firstpersonid+"/"+measure;
        accept = MediaType.APPLICATION_XML;
        result="";
		contentType = "application/xml";
		System.out.println("Request #3.9: POST " + url + " Accept: " + accept+ " Content-type: " + contentType);
        resp="";
        
        String body = "<Measure>"
        +"<value>1900</value>"
        +"</Measure>";
        response = service.path("person/"+firstpersonid+"/"+measure).request().accept(accept)
        .post(Entity.xml(body));
		resp = response.readEntity(String.class);
        
		responseCode = ""+response.getStatus();
		if (responseCode.equals("200") || responseCode.equals("201")|| responseCode.equals("202")) {
			result = "OK";
		} else {
			result = "ERROR";
		}
		System.out.println("=> Result: " + result);
		System.out.println("=> HTTP Status: " + responseCode);
		System.out.println(formatXML(resp));
        
        
        System.out.println("========================================================================");
        
        url = getBaseURI() + "/person/"+firstpersonid+"/"+measure;
        accept = MediaType.APPLICATION_XML;
        result="";
        contentType = "application/xml";
		System.out.println("Request #3.9: GET " + url + " Accept: " + accept + " Content-type: " + contentType);
        resp="";
        
        ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI());
        
        response = service.path("/person/"+firstpersonid+"/"+measure).request().accept(MediaType.APPLICATION_XML).get();
		resp = response.readEntity(String.class);
        responseCode = ""+response.getStatus();
        
        count2=0;
        count2=TestClient.countStringOccurence(resp, "<idMeasureDefinition>");
                if(count2-count==1){
            result="OK";
        }
        else{
            result="ERROR";
        }
        
        System.out.println("=> Result: " + result);
		System.out.println("=> HTTP Status: " + responseCode);
        System.out.println(formatXML(resp));
        
    }
    
    
    
    
    
    
    
    
    
    public String formatXML(String unformattedXml) {
        try {
            final Document document = parseXmlFile(unformattedXml);
            
            OutputFormat format = new OutputFormat(document);
            format.setLineWidth(65);
            format.setIndenting(true);
            format.setIndent(2);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);
            
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
   

	private static String formatJSON(String string) {
		String result = new GsonBuilder().setPrettyPrinting().create()
        .toJson(new JsonParser().parse(string));
		return result;
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://immense-shore-7749.herokuapp.com/").build();
	}

}