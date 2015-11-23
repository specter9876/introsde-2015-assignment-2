import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import java.util.List;
import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.glassfish.jersey.client.ClientConfig;

import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class TestClient {
    public static int firstpersonid;
    public static int lastpersonid;
    public static int POSTpersonid;
    public static List<String> array=new ArrayList<>();
    public static int measureTypeid;
    public static String measureType;
    public static HelperClient ch =new HelperClient();
    
	public static void main(String[] args)throws IOException, JAXBException, SAXException, TransformerException, ParserConfigurationException {
		
        
        ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI());
		
        System.out.println("STEP 1: URL of restfull web service: "+getBaseURI());
        
        System.out.println("STEP 2: start");
        System.out.println("STEP 3: start");
        System.out.println("=====================================================");

        getRequest1();
        getRequest2();
        getRequest3();
        getRequest4();
        getRequest5();
        getRequest6();
        getRequest7();
        getRequest8();
        getRequest9();
        
         System.out.println("================END OF CLIENT=====================================");
         System.out.println("===================================================================");
        
		
	}
    
    
    
	private static void getRequest1() throws IOException, JAXBException, SAXException, TransformerException, ParserConfigurationException {
		
       
		int responseCode = -1;
		String resp = "";
		String accept = "";
		String contentType = "";
		String result = "";
		int count = 0;
		
        //For XML
		
        String url = getBaseURI() + "/person";
		URL obj = new URL(url);
		HttpURLConnection  con = (HttpURLConnection) obj.openConnection();
        
		con.setRequestMethod("GET");
		accept = "application/xml";
		con.setRequestProperty("Accept", accept);
		contentType = "application/xml";
		con.setRequestProperty("Content-Type", contentType);
        
		System.out.println("Request #3.1: GET " + url + " Accept: " + accept + "Content-type: " + contentType);
		resp = TestClient.getConnectionOutputXML(con);
        
		count = TestClient.countStringOccurence(resp, "<person>");
		if (count < 3) {
			result = "ERROR";
		} else {
			result = "OK";
		}
		System.out.println("=> Result: " + result);
		responseCode = con.getResponseCode();
		System.out.println("=> HTTP Status: " + responseCode);
		System.out.println(resp);
        
        //get id of first and last person
        //get id of first person using nodes
        InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(resp));
        
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);
		NodeList nodes = doc.getElementsByTagName("person");
        Node first_node = nodes.item(0);
        
		NodeList fn_children = first_node.getChildNodes();
		for (int i = 0; i < fn_children.getLength(); i++) {
			Node item = fn_children.item(i);
			if (!(item.getNodeName() == "#text")) {
				if (item.getNodeName().equals("idPerson")) {
                    firstpersonid = Integer.parseInt(item.getTextContent());
				}
			}
		}
        
		// get id of last person using nodes
		Node last_node = nodes.item(nodes.getLength() - 1);
        
		NodeList ln_children = last_node.getChildNodes();
		for (int i = 0; i < ln_children.getLength(); i++) {
			Node item = ln_children.item(i);
			if (!(item.getNodeName() == "#text")) {
				if (item.getNodeName().equals("idPerson")) {
					lastpersonid = Integer.parseInt(item.getTextContent());
				}
			}
		}
       System.out.println("===============================================================================================");
		//For JSON
		
		con = (HttpURLConnection) obj.openConnection();
        
		con.setRequestMethod("GET");
		accept = "application/json";
		con.setRequestProperty("Accept", accept);
		contentType = "application/json";
		con.setRequestProperty("Content-Type", contentType);
        
		System.out.println("Request #3.1: GET " + url + " Accept: " + accept + "Content-type: " + contentType);
		      
		resp = TestClient.getConnectionOutputJSON(con);
		count =TestClient.countStringOccurence(resp, "\"name\"");
        
		if (count < 3) {
			result = "ERROR";
		} else {
			result = "OK";
		}
		System.out.println("=> Result: " + result);
		responseCode = con.getResponseCode();
		System.out.println("=> HTTP Status: " + responseCode);
		System.out.println(resp);
        
        
        
        System.out.println("========================FIRST & LAST PERSON ID================================================");
        System.out.println("First person id:"+firstpersonid);
        System.out.println("Last person id:"+lastpersonid);
        
        
	}
    
    
    
    
    
    
	private static void getRequest2() throws IOException, JAXBException, SAXException, TransformerException, ParserConfigurationException {
        
        int responseCode = -1;
		String resp = "";
		String accept = "";
		String contentType = "";
		String result = "";
        System.out.println("========================================================================");
        System.out.println("========================================================================");

		
        //Accept XML
		
        String url = getBaseURI() + "/person/"+firstpersonid;
		URL obj = new URL(url);
		HttpURLConnection  con = (HttpURLConnection) obj.openConnection();
        
		con.setRequestMethod("GET");
		accept = "application/xml";
		con.setRequestProperty("Accept", accept);
		contentType = "application/xml";
		con.setRequestProperty("Content-Type", contentType);
        
		System.out.println("Request #3.2: GET " + url + " Accept: " + accept + "Content-type: " + contentType);
		resp = TestClient.getConnectionOutputXML(con);
        
        responseCode = con.getResponseCode();
		if (responseCode == 200 || responseCode == 202) {
			result = "OK";
		}
        else {
			result = "ERROR";
		}
        
		System.out.println("=> Result: " + result);
		System.out.println("=> HTTP Status: " + responseCode);
		System.out.println(resp);
        
        System.out.println("========================================================================");
        
        //Accept json
		url = getBaseURI() + "/person/"+firstpersonid;
        
		obj = new URL(url);
		con = (HttpURLConnection) obj.openConnection();
        
		con.setRequestMethod("GET");
		accept = "application/json";
		con.setRequestProperty("Accept", accept);
		contentType = "application/json";
		con.setRequestProperty("Content-Type", contentType);
        
		System.out.println("Request #3.2: GET " + url + " Accept: " + accept + "Content-type: " + contentType);
        
		resp = TestClient.getConnectionOutputJSON(con);
		///inserire controllo sul codice
        
        responseCode = con.getResponseCode();
		if (responseCode == 200 || responseCode == 202) {
			result = "OK";
		}
        else {
			result = "ERROR";
		}
		System.out.println("=> Result: " + result);
		System.out.println("=> HTTP Status: " + responseCode);
		System.out.println(resp);
		
	}
    
    
    
    
    
    private static void  getRequest3() throws IOException, JAXBException, SAXException, TransformerException, ParserConfigurationException {
       
        ch.putmethod();
        
     
       
        
    }
    
    
    
    
    private static void  getRequest4() throws IOException, JAXBException, SAXException, TransformerException, ParserConfigurationException {
        
        POSTpersonid=ch.postPerson();
       
    
    }
    
    private static void getRequest5() throws IOException, JAXBException, SAXException, TransformerException, ParserConfigurationException {
        
         ch.deletePerson(POSTpersonid);
         ch.getdeletePerson(POSTpersonid);

    
    
    }
    
    
  
    
    
    
    
    private static void  getRequest6() throws IOException, JAXBException, SAXException, TransformerException, ParserConfigurationException {
        
        
        System.out.println("========================================================================");
        System.out.println("========================================================================");
        
        int responseCode = -1;
		String resp = "";
		String accept = "";
		String contentType = "";
		String result = "";
		int count = 0;
		
        //For XML
		
        String url = getBaseURI() + "/measureTypes";
		URL obj = new URL(url);
		HttpURLConnection  con = (HttpURLConnection) obj.openConnection();
        
		con.setRequestMethod("GET");
		accept = "application/xml";
		con.setRequestProperty("Accept", accept);
		contentType = "application/xml";
		con.setRequestProperty("Content-Type", contentType);
        
		System.out.println("Request #3.6: GET " + url + " Accept: " + accept + "Content-type: " + contentType);
		resp = TestClient.getConnectionOutputXML(con);
      
        count=countStringOccurence(resp,"<measureName>");
        
        responseCode = con.getResponseCode();
		if (count >3) {
			result = "OK";
		}
        else {
			result = "ERROR";
		}
        

		
		System.out.println("=> Result: " + result);
		responseCode = con.getResponseCode();
		System.out.println("=> HTTP Status: " + responseCode);
		System.out.println(resp);

        
        
        
         System.out.println("=====================================================");
        //looking for creating array of measure
        String temp="";
        InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(resp));
        
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);
		NodeList nodes = doc.getElementsByTagName("measureDefinitions");
        Node first_node = nodes.item(0);
        
		NodeList fn_children = first_node.getChildNodes();
		for (int i = 0; i < fn_children.getLength(); i++) {
			Node item = fn_children.item(i);
            //System.out.println("content: "+item.getNodeName());
            NodeList lastchildren = item.getChildNodes();
            for(int j=0; j<lastchildren.getLength();j++){
                Node tempnode = lastchildren.item(j);

                if (!(tempnode.getNodeName() == "#text")) {
                    if (tempnode.getNodeName().equals("measureName")) {
                        temp = ""+tempnode.getTextContent();
                       // System.out.println(temp);
                        array.add(temp);
                    }
                }
            }
		}
        
        
        
        //For json
        url = getBaseURI() + "/measureTypes";
        
		obj = new URL(url);
		con = (HttpURLConnection) obj.openConnection();
        
		con.setRequestMethod("GET");
		accept = "application/json";
		con.setRequestProperty("Accept", accept);
		contentType = "application/json";
		con.setRequestProperty("Content-Type", contentType);
        
		System.out.println("Request #3.6: GET " + url + " Accept: " + accept + "Content-type: " + contentType);
		
		resp = TestClient.getConnectionOutputJSON(con);
		count =TestClient.countStringOccurence(resp, "\"measureName\"");
        
		if (count < 3) {
			result = "ERROR";
		} else {
			result = "OK";
		}
		System.out.println("=> Result: " + result);
		responseCode = con.getResponseCode();
		System.out.println("=> HTTP Status: " + responseCode);
		System.out.println(resp);

        
        System.out.println("================ARRAY CREATED====================================");
        System.out.println("Array measureType created");
        for(int k=0;k<array.size();k++){
            System.out.println("Array content "+k+": "+array.get(k));
        }
        System.out.println("Array size: "+ array.size());
        
    }
    
    
    
    private static void getRequest7() throws IOException, JAXBException, SAXException, TransformerException, ParserConfigurationException {
        
    
        int responseCode = -1;
		String resp = "";
		String accept = "";
		String contentType = "";
		String result = "";
        System.out.println("================================================================================================");
        System.out.println("==========================FIRST PERSON XML & JSON==============================================");
        String measure="";
        int measureid=0;
		int count=0;
        //Accept XML
        for(int i=0;i<array.size();i++){
            //System.out.println( "fgtfrt:" +array.get(i));
            count=0;
            measure=array.get(i);
            
            if (measure.equals("weight")){
                System.out.println("===========WEIGHT=================");
                measureid=1;
            }
            if (measure.equals("height")){
                System.out.println("=============HEIGHT===============");
                measureid=2;
            }
            if (measure.equals("steps")){
                 System.out.println("============STEPS================");
                measureid=3;
            }
            if (measure.equals("blood pressure")){
                 System.out.println("============BLOOD PRESSURE================");
                measureid=4;
            }
            if (measure.equals("heart rate")){
                 System.out.println("=========HEART RATE===================");
                measureid=5;
            }
            if (measure.equals("bmi")){
                 System.out.println("=============BMI===============");
                measureid=6;
            }
            
            
            String url = getBaseURI() + "/person/"+firstpersonid+"/"+measureid;
            URL obj = new URL(url);
            HttpURLConnection  con = (HttpURLConnection) obj.openConnection();
        
            con.setRequestMethod("GET");
            accept = "application/xml";
            con.setRequestProperty("Accept", accept);
            contentType = "application/xml";
            con.setRequestProperty("Content-Type", contentType);
            
            System.out.println("Request #3.7: GET " + url + " Accept: " + accept + "Content-type: " + contentType);
            resp = TestClient.getConnectionOutputXML(con);
        
            
            
            count = TestClient.countStringOccurence(resp, "<value>");
            if (count < 1) {
                result = "ERROR";
            } else {
                result = "OK";
            }
            responseCode = con.getResponseCode();
            
            System.out.println("=> Result: " + result);
            System.out.println("=> HTTP Status: " + responseCode);
            System.out.println(resp);
            
            //save a measure
            if (measure.equals("height")){
                
                System.out.println("=====================SAVE MEASURETYPE AND MEASUREID===================================================");
                ///mmm sistemare
                String temp="";
                
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(resp));
                
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(is);
                NodeList nodes = doc.getElementsByTagName("healthMeasureHistory");
                Node first_node = nodes.item(0);
                
                NodeList fn_children = first_node.getChildNodes();
                //System.out.println("list node:= "+fn_children.getLength());
                
                
                for (int z = 0; z < fn_children.getLength(); z++) {
                    Node item = fn_children.item(z);
                    //System.out.println("content: "+item.getNodeName());
                    NodeList lastchildren = item.getChildNodes();
                    //System.out.println("node name: "+item.getNodeName());
                    
                    if (item.getNodeName().equals("idMeasureHistory")) {
                      
                        measureTypeid=Integer.parseInt(item.getTextContent());
                        
                    }
                    for(int j=0; j<lastchildren.getLength();j++){
                        Node tempnode = lastchildren.item(j);
                        
                        if (!(tempnode.getNodeName() == "#text")) {
                            if (tempnode.getNodeName().equals("measureName")) {
                                
                                measureType = ""+tempnode.getTextContent();
                                System.out.println(temp);
                            }
                            
                        }
                    }
                }
                
                System.out.println("Save measureTipes "+measureType+" and measureId: "+measureTypeid);
                System.out.println("========================================================================");
                
            }
           
            
          }

       //For json
         System.out.println("========================================================================");
        
        for(int i=0;i<array.size();i++){
            
            count=0;
            measure=array.get(i);
            
            if (measure.equals("weight")){
                System.out.println("===========WEIGHT=================");
                measureid=1;
            }
            if (measure.equals("height")){
                System.out.println("=============HEIGHT===============");
                measureid=2;
            }
            if (measure.equals("steps")){
                System.out.println("============STEPS================");
                measureid=3;
            }
            if (measure.equals("blood pressure")){
                System.out.println("============BLOOD PRESSURE================");
                measureid=4;
            }
            if (measure.equals("heart rate")){
                System.out.println("=========HEART RATE===================");
                measureid=5;
            }
            if (measure.equals("bmi")){
                System.out.println("=============BMI===============");
                measureid=6;
            }
            
            String url = getBaseURI() + "/person/"+firstpersonid+"/"+measureid;
            URL obj = new URL(url);
            HttpURLConnection  con = (HttpURLConnection) obj.openConnection();
        
            con.setRequestMethod("GET");
            accept = "application/json";
            con.setRequestProperty("Accept", accept);
            contentType = "application/json";
            con.setRequestProperty("Content-Type", contentType);
        
            System.out.println("Request #3.7: GET " + url + " Accept: " + accept + "Content-type: " + contentType);
        
            resp = TestClient.getConnectionOutputJSON(con);
            ///inserire controllo sul codice
            
            
            count = TestClient.countStringOccurence(resp, "\"value\"");
            if (count < 1) {
                result = "ERROR";
            } else {
                result = "OK";
            }
            System.out.println("=> Result: " + result);
            System.out.println("=> HTTP Status: " + responseCode);
            System.out.println(resp);
	
            
    
        }
        
        
        System.out.println("================================================================================================");
        System.out.println("==========================LAST PERSON XML & JSON==============================================");
        measure="";
        measureid=0;
		count=0;
        //Accept XML
        for(int i=0;i<array.size();i++){
            //System.out.println( "fgtfrt:" +array.get(i));
            count=0;
            measure=array.get(i);
            
            if (measure.equals("weight")){
                System.out.println("===========WEIGHT=================");
                measureid=1;
            }
            if (measure.equals("height")){
                System.out.println("=============HEIGHT===============");
                measureid=2;
            }
            if (measure.equals("steps")){
                System.out.println("============STEPS================");
                measureid=3;
            }
            if (measure.equals("blood pressure")){
                System.out.println("============BLOOD PRESSURE================");
                measureid=4;
            }
            if (measure.equals("heart rate")){
                System.out.println("=========HEART RATE===================");
                measureid=5;
            }
            if (measure.equals("bmi")){
                System.out.println("=============BMI===============");
                measureid=6;
            }
            
            
            String url = getBaseURI() + "/person/"+lastpersonid+"/"+measureid;
            URL obj = new URL(url);
            HttpURLConnection  con = (HttpURLConnection) obj.openConnection();
            
            con.setRequestMethod("GET");
            accept = "application/xml";
            con.setRequestProperty("Accept", accept);
            contentType = "application/xml";
            con.setRequestProperty("Content-Type", contentType);
            
            System.out.println("Request #3.7: GET " + url + " Accept: " + accept + "Content-type: " + contentType);
            resp = TestClient.getConnectionOutputXML(con);
            
            
            
            count = TestClient.countStringOccurence(resp, "<value>");
            if (count < 1) {
                result = "ERROR";
            } else {
                result = "OK";
            }
            responseCode = con.getResponseCode();
            
            System.out.println("=> Result: " + result);
            System.out.println("=> HTTP Status: " + responseCode);
            System.out.println(resp);
            
            
        }
        
        //For json
        System.out.println("========================================================================");
        
        for(int i=0;i<array.size();i++){
            
            count=0;
            measure=array.get(i);
            
            if (measure.equals("weight")){
                System.out.println("===========WEIGHT=================");
                measureid=1;
            }
            if (measure.equals("height")){
                System.out.println("=============HEIGHT===============");
                measureid=2;
            }
            if (measure.equals("steps")){
                System.out.println("============STEPS================");
                measureid=3;
            }
            if (measure.equals("blood pressure")){
                System.out.println("============BLOOD PRESSURE================");
                measureid=4;
            }
            if (measure.equals("heart rate")){
                System.out.println("=========HEART RATE===================");
                measureid=5;
            }
            if (measure.equals("bmi")){
                System.out.println("=============BMI===============");
                measureid=6;
            }
            
            String url = getBaseURI() + "/person/"+lastpersonid+"/"+measureid;
            URL obj = new URL(url);
            HttpURLConnection  con = (HttpURLConnection) obj.openConnection();
            
            con.setRequestMethod("GET");
            accept = "application/json";
            con.setRequestProperty("Accept", accept);
            contentType = "application/json";
            con.setRequestProperty("Content-Type", contentType);
            
            System.out.println("Request #3.7: GET " + url + " Accept: " + accept + "Content-type: " + contentType);
            
            resp = TestClient.getConnectionOutputJSON(con);
            ///inserire controllo sul codice
            
            
            count = TestClient.countStringOccurence(resp, "\"value\"");
            if (count < 1) {
                result = "ERROR";
            } else {
                result = "OK";
            }
            System.out.println("=> Result: " + result);
            System.out.println("=> HTTP Status: " + responseCode);
            System.out.println(resp);
            
            
            
        }
        
        
        
        
    }
    
    
    
    private static void getRequest8() throws IOException, JAXBException, SAXException, TransformerException, ParserConfigurationException {
        int responseCode = -1;
		String resp = "";
		String accept = "";
		String contentType = "";
		String result = "";
        System.out.println("========================================================================");
        System.out.println("========================================================================");
        
		
        //Accept XML
		
        String url = getBaseURI() + "/person/"+firstpersonid+"/2/"+measureTypeid;
		URL obj = new URL(url);
		HttpURLConnection  con = (HttpURLConnection) obj.openConnection();
        
		con.setRequestMethod("GET");
		accept = "application/xml";
		con.setRequestProperty("Accept", accept);
		contentType = "application/xml";
		con.setRequestProperty("Content-Type", contentType);
        
		System.out.println("Request #3.8: GET " + url + " Accept: " + accept + "Content-type: " + contentType);
		resp = TestClient.getConnectionOutputXML(con);
        
        responseCode = con.getResponseCode();
		if (responseCode == 200 || responseCode == 202) {
			result = "OK";
		}
        else {
			result = "ERROR";
		}
        
		System.out.println("=> Result: " + result);
		System.out.println("=> HTTP Status: " + responseCode);
		System.out.println(resp);
        
        System.out.println("========================================================================");
        
        //Accept json
		url = getBaseURI() + "/person/"+firstpersonid+"/2/"+measureTypeid;
        
		obj = new URL(url);
		con = (HttpURLConnection) obj.openConnection();
        
		con.setRequestMethod("GET");
		accept = "application/json";
		con.setRequestProperty("Accept", accept);
		contentType = "application/json";
		con.setRequestProperty("Content-Type", contentType);
        
		System.out.println("Request #3.8: GET " + url + " Accept: " + accept + "Content-type: " + contentType);
        
		resp = TestClient.getConnectionOutputJSON(con);
		///inserire controllo sul codice
        
        responseCode = con.getResponseCode();
		if (responseCode == 200 || responseCode == 202) {
			result = "OK";
		}
        else {
			result = "ERROR";
		}
		System.out.println("=> Result: " + result);
		System.out.println("=> HTTP Status: " + responseCode);
		System.out.println(resp);


    
    }
    
    
    
    
    private static void getRequest9() throws IOException, JAXBException, SAXException, TransformerException, ParserConfigurationException {
        
        System.out.println("========================================================================");
        System.out.println("========================================================================");
        
        //Accept XML
        int responseCode = -1;
        //choose height as measure type i refer to it as 2
        String url = getBaseURI() + "/person/"+firstpersonid+"/2";
		URL obj = new URL(url);
		HttpURLConnection  con = (HttpURLConnection) obj.openConnection();
        String resp="";
		con.setRequestMethod("GET");
		String accept = "application/xml";
		con.setRequestProperty("Accept", accept);
		String contentType = "application/xml";
		con.setRequestProperty("Content-Type", contentType);
        
		System.out.println("Request #3.9: GET " + url + " Accept: " + accept + "Content-type: " + contentType);
		resp = TestClient.getConnectionOutputXML(con);
        
        String result="";
        int count=countStringOccurence(resp,"<idMeasureDefinition>");
   
        responseCode = con.getResponseCode();
		if (responseCode == 200 || responseCode == 202) {
			result = "OK";
		}
        else {
			result = "ERROR";
		}
        
		System.out.println("=> Result: " + result);
		System.out.println("=> HTTP Status: " + responseCode);
		System.out.println(resp);
        
       
        System.out.println("========================================================================");
        ch.finalPost(count,firstpersonid, 2);
        
        

    }
    
 
    
	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://immense-shore-7749.herokuapp.com").build();
	}
    
    
    
	private static String getConnectionOutputXML(HttpURLConnection con)
    throws IOException, TransformerException, ParserConfigurationException, SAXException {
		PrintWriter writer = new PrintWriter("out.xml", "UTF-8");
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
        
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
			writer.println(inputLine);
		}
		in.close();
		writer.close();
        
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse("out.xml");
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		transformer.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(outputStream, "UTF-8")));
        
		return outputStream.toString();
	}
    
    
    
	private static String getConnectionOutputJSON(HttpURLConnection con)
    throws IOException, TransformerException, ParserConfigurationException, SAXException {
		PrintWriter writer = new PrintWriter("out.json", "UTF-8");
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
        
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		String retval = new GsonBuilder().setPrettyPrinting().create()
        .toJson(new JsonParser().parse(response.toString()));
		writer.write(retval);
		writer.close();
		return retval;
	}
    
    
	public static int countStringOccurence(String str, String findStr) {
		int lastIndex = 0;
		int count = 0;
		while (lastIndex != -1) {

			lastIndex = str.indexOf(findStr, lastIndex);
			if (lastIndex != -1) {
				count++;
				lastIndex += findStr.length();
			}
		}
		return count;
	}
}


