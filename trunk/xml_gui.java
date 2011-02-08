import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.OutputStream;
import java.io.StringWriter;
import org.w3c.dom.*;

import javax.xml.parsers.*; 
import javax.xml.transform.*; 
import javax.xml.transform.dom.DOMSource; 
import javax.xml.transform.stream.StreamResult; 
import org.xml.sax.SAXException;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Rectangle;
import javax.swing.JComboBox;
//thinking i might eventually have to add some of the xml info to an array so this is a place holder for the array lib. i will have to use
import java.util.ArrayList; 
 
public class xml_gui extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JPanel main_window = null;
	private JComboBox jComboBox = null;
	private String ip;
	ArrayList<String> myArr = new ArrayList<String>();
	
	public xml_gui() 
	{
		super();
		initialize();
	}
	
	void initialize() 
	 {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("XML");
	}
	
	void xml()
	{
		try {	
			File file = new File("c:\\users\\jason\\desktop\\proxies.xml");
		 
			//Create instance of DocumentBuilderFactory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 
			//Get the DocumentBuilder
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
		 
			//Using existing XML Document
			Document doc = docBuilder.parse(file);
			
			//normalize the text
			doc.getDocumentElement ().normalize ();
		 
			//gets just the name of the root
			String simple_root = doc.getDocumentElement().getNodeName();
			
			Element root = doc.getDocumentElement();
						
			//gets the ip elements 
			NodeList proxy = doc.getElementsByTagName("proxy");
			
			//checks the make sure i got the ip elements by printing out the number of occurances 
			int total = proxy.getLength();
			
			NodeList list  = doc.getElementsByTagName("*");
			System.out.println("\nElements in the proxy file:");
			int proxy_num = 1;
			for (int i=0; i<list.getLength(); i++)
			{
				Node num2 = proxy.item(i);
				Element second = (Element) num2;
				Element element2 = (Element)list.item(i);
				NodeList text2 = element2.getChildNodes();
				if(element2.getNodeName() != "proxies" && element2.getNodeName() != "proxy")
				{
			     if(element2.getNodeName() == "ip")
			     {
			    	 System.out.println("");
			    	 System.out.println("Proxy #: " + proxy_num);
			    	 proxy_num++;
			    	 System.out.println(element2.getNodeName() + ": " + ((Node)text2.item(0)).getNodeValue().trim());
			    	 
			     }
			     else
				System.out.println(element2.getNodeName() + ": " + ((Node)text2.item(0)).getNodeValue().trim());
			     if(element2.getNodeName() == "source-ip")
			     {
			    	 ip = ((Node)text2.item(0)).getNodeValue().trim();
			    	 myArr.add(ip);
			    	 jComboBox.addItem(ip);
			     }
				}
			}
				
			//set up a transformer
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
		 
		        //create string from xml tree
		        StringWriter sw = new StringWriter();
		        StreamResult result = new StreamResult(sw);
		        DOMSource source = new DOMSource(doc);
		        trans.transform(source, result);
		        String xmlString = sw.toString();
		 
		        OutputStream f0;
			byte buf[] = xmlString.getBytes();
			f0 = new FileOutputStream("c:\\users\\jason\\desktop\\connections.xml");
			for(int i=0;i<buf .length;i++) {
			   f0.write(buf[i]);
			}
			f0.close();
			buf = null;
		     }
		     catch(SAXException e) {
			e.printStackTrace();		
		     }
		     catch(IOException e) {
		        e.printStackTrace();		
		     }
		     catch(ParserConfigurationException e) {
		       e.printStackTrace();		
		     }
		     catch(TransformerConfigurationException e) {
		       e.printStackTrace();		
		     }
		     catch(TransformerException e) {
		       e.printStackTrace();		
		     }
		  }
	
	private JPanel getJContentPane() 
	{
		if (main_window == null) 
		{
			main_window = new JPanel();
			jComboBox = new JComboBox();
			xml();
			main_window.add(getJComboBox(),null);
		}
		return main_window;
	}
	
	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new JComboBox();
			jComboBox.addItem(ip);
			jComboBox.setBounds(new Rectangle(8, 50, 100, 20));
		}
		return jComboBox;
	}
}