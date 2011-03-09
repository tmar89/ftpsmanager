					/************************************	
					 * FlexTPS proxy editor 			*
					 * Jason Ricles						*
					 * Last Edit: 3/3/11	            *
					 ************************************/
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

//class that sets up the gui for the proxy xml editor 
public class ProxyGUI extends JFrame 
{
	private static final long serialVersionUID = 1L;
	private JPanel main_window = null;
	protected JComboBox cameraip = null;
	private JButton savelocal = null;
	private JButton saveserver = null;
	private JButton download = null;
	private JButton uploadlocal = null;
	private JButton quit = null;
	private JMenuBar menuBar = null;
	private JMenu server = null;
	private JMenu file = null;
	private JMenuItem connect = null;
	private JMenuItem disconnect = null;
	private JMenuItem saveas = null;
	private Login login = new Login();
	private FlexTPSProxy proxy = new FlexTPSProxy();
	private JOptionPane failed = null;

	public ProxyGUI() 
	{
		super();
		//call to intilize the content pane
		initialize();

	}
	
	void initialize() 
	 {
		this.setSize(600,375);
		this.setContentPane(getJContentPane());
		this.setTitle("FlexTPS Proxy Editor");
		
		//makes the menu bar
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		file = new JMenu("File");
		menuBar.add(file);
		saveas = new JMenuItem("Save as");
		file.add(saveas);
		saveas.addMouseListener(new java.awt.event.MouseAdapter() 
		{
			public void mousePressed(java.awt.event.MouseEvent e) 
			{
				proxy.saveLocal();
			}
		});
		server = new JMenu("Server");
		menuBar.add(server);
		connect = new JMenuItem("Connect");
		server.add(connect);
		//pops up with login when pressed
		connect.addMouseListener(new java.awt.event.MouseAdapter() 
		{
			public void mousePressed(java.awt.event.MouseEvent e) 
			{
				login.login();
				proxy.login = login;
			}
		});
		disconnect = new JMenuItem("Disconnect");
		server.add(disconnect);
		//if user wants to disconnect
		disconnect.addMouseListener(new java.awt.event.MouseAdapter() 
		{
			public void mousePressed(java.awt.event.MouseEvent e) 
			{
				login.logout();
			}
		});
	}
	
	//method that sets up the content pane with calls to getters 
	private JPanel getJContentPane() 
	{
		if (main_window == null) 
		{
			main_window = new JPanel();
			main_window.setLayout(null);
			main_window.add(getJComboBox(),null);	
			main_window.add(getJButton(), null);
			main_window.add(getJButton1(), null);
			main_window.add(getJButton2(), null);
			main_window.add(getJButton3(), null);
			main_window.add(getJButton4(), null);
		}
		return main_window;
	}
	
	//getter for camera ip combobox
	public JComboBox getJComboBox() {
		if (cameraip == null) {
			cameraip = new JComboBox();
			cameraip.setBounds(new Rectangle(15, 25, 100, 20));
		}
		return cameraip;
	}
	
	//make button to save locally
	private JButton getJButton() 
	{
		if (savelocal == null) 
		{
			savelocal = new JButton();
			savelocal.setBounds(new Rectangle(215, 275, 115, 25));
			savelocal.setText("Save Local");
		}
		return savelocal;
	}
	
	//make button to save to server
	private JButton getJButton1() 
	{
		if (saveserver == null) 
		{
			saveserver = new JButton();
			saveserver.setBounds(new Rectangle(337, 275, 130, 25));
			saveserver.setText("Save to Server");
		}
		return saveserver;
	}
	
	//make button to download file to fill combo box
	private JButton getJButton2() 
	{
		if (download == null) 
		{
			download = new JButton();
			download.setBounds(new Rectangle(15, 275, 93, 25));
			download.setText("Download");
			download.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
					try 
					{
						proxy.sys_path = "/home/jgr208";
						proxy.getFile("/proxies/etc","proxies.xml");
					} 
					catch(Exception e1)
					 {
						failed = new JOptionPane();
						failed.setSize(new Dimension(119, 69));
						JOptionPane.showMessageDialog(failed, "Failed to download file " + proxy.remotefile);
					 }
			}
			});
		}
		return download;
	}
	
	//button to upload a local xml file
	private JButton getJButton3() 
	{
		if (uploadlocal == null) 
		{
			uploadlocal = new JButton();
			uploadlocal.setBounds(new Rectangle(115, 275, 93, 25));
			uploadlocal.setText("Upload");
			uploadlocal.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mousePressed(java.awt.event.MouseEvent e) 
				{
					proxy.uploadLocal();
				}
			});
		}
		return uploadlocal;
	}
	
	//button to exit program, deletes all temp files and disconnects everything when run
	private JButton getJButton4() 
	{
		if (quit == null) 
		{
			quit = new JButton();
			quit.setBounds(new Rectangle(475, 275, 93, 25));
			quit.setText("Exit");
			quit.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
					System.exit(1);
				}
			});
		}
		return quit;
	}
}

//class to log the user in
class Login
{
	private String host = null;  
	private String user = null;
	private JSch jsch = new JSch();  
	protected Session session = null;
	private String password = null;
	private JOptionPane prompt = null; 
	private JPasswordField pass = null;
	private JOptionPane connected = null;
	private JOptionPane noconnect = null;
	boolean good = false;
	protected ChannelSftp channel = null;

	void login()
	 {
		 try
		 {
		//sees if user is already connected	 
		 if(good == true)
			already();
		 //else if they are not already connected
		else 
		{
			getUserinfo();
			session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			channel = (ChannelSftp)session.openChannel("sftp"); 
			channel.connect(); 
			good();
		}
		 }
		 //if can not connect
		 catch(Exception e)
		 {
			 bad();
		 }
	 }
	//gets their info
	private JOptionPane getUserinfo() 
	{
			prompt = new JOptionPane();
			host = JOptionPane.showInputDialog(null, "User@server");
			prompt.setSize(new Dimension(119, 69));
			user = host.substring(0, host.indexOf('@'));
			host = host.substring(host.indexOf('@')+1);
			getUserpass();
		return prompt;
	}
	
	//gets their password
	@SuppressWarnings("deprecation")
	private JOptionPane getUserpass() 
	{
			prompt = new JOptionPane();
			pass = new JPasswordField();
			prompt.setSize(new Dimension(119, 69));
			pass = new JPasswordField();
			pass.setEchoChar('*');
			JOptionPane.showConfirmDialog(null, pass,
					  "Please enter your password", JOptionPane.OK_CANCEL_OPTION);
			password = pass.getText();
		return prompt;
	}
	
	//confirm login
	void good()
	{
		connected = new JOptionPane();
		connected.setSize(new Dimension(119, 69));
		JOptionPane.showMessageDialog(connected, "You are now connected to " + host);
		good = true;
	}
	
	//confirm no connection happened
	void bad()
	{
		noconnect = new JOptionPane();
		noconnect.setSize(new Dimension(119, 69));
		JOptionPane.showMessageDialog(noconnect, "Could not connect to " + host);
	}
	
	//tell user they are alrady logged into a server
	void already()
	{
		noconnect = new JOptionPane();
		noconnect.setSize(new Dimension(119, 69));
		JOptionPane.showMessageDialog(noconnect, "Already connected to " + host);
	}
	
	//logs user out from server
	void logout()
	{
		channel.disconnect();
		session.disconnect();
		connected = new JOptionPane();
		connected.setSize(new Dimension(119, 69));
		JOptionPane.showMessageDialog(connected, "You are now disconnected from " + host);
		good = false;
	}
}

//class that will be used to get the proxy files, as well as upload the proxy file, where the bulk of the code behind running the program will lay
class FlexTPSProxy 
{
	//have to look over the variables, don't think i need this many, can get quite confusing then
	protected String filename =  "~proxy.xml";
	protected String sys_path = null;
	public Login login;
	private static final long serialVersionUID = 1L;
	public ArrayList <FlexTPSCamera> proxies;
	public FlexTPSCamera camera;
	public File file;
	public DocumentBuilderFactory factory;
	public DocumentBuilder docBuilder;
	public Document doc;
	public NodeList proxy;
	public NodeList list;
	public Element element2;
	public NodeList text2;
	public Node num2;
	public Element second;
	Transformer trans;
	TransformerFactory transfac;
	StringWriter sw; 
	StreamResult result;
	DOMSource source;
	String xmlString;
	File written_to;
	String dir = System.getProperty("user.dir");
	String remoteDirectory = null;
	String remotefile = "proxy.xml";
	JButton openButton, saveButton;
	JTextArea log;
	JFileChooser fc;
	File localsave;
	File uploadFile;
	protected String extension = "xml";
	String localFile;
	ProxyGUI gui;

	//constructor
	public FlexTPSProxy()
	{
		factory = DocumentBuilderFactory.newInstance();
		proxies = new ArrayList <FlexTPSCamera>();
	}
	
	//constructor 
	public FlexTPSProxy(File file,DocumentBuilderFactory factory,String ip)
	{
		this.file = file;
		this.factory = factory;
	}
	
	
	//method to download proxy files
	void getFile(String path,String remotefile) throws JSchException, SftpException, ParserConfigurationException, SAXException, IOException
	{
		remoteDirectory = sys_path;
		login.channel = (ChannelSftp)login.session.openChannel("sftp"); 
		login.channel.connect(); 
		remoteDirectory = remoteDirectory + path;
		login.channel.cd(remoteDirectory); 
	    localFile = "~proxies.xml";
		login.channel.get((remotefile), localFile); 
		docBuilder = factory.newDocumentBuilder();
		doc = docBuilder.parse(localFile);
		doc.getDocumentElement ().normalize ();
		getProxies();
	}
	
	//just gets the proxy elemnts 
	void getProxies()
	{
		proxy = doc.getElementsByTagName("proxy");
		add();
	}
	
	//adds all the xml elements to the arraylist and puts the ips in the combo box
	public void add()
	{
		for (int i=0; i<proxy.getLength(); i++)
		{
			camera = new FlexTPSCamera();
			element2 = (Element)proxy.item(i);
			text2 = element2.getElementsByTagName("*");
			for(int t = 0; t<text2.getLength();t++)
			{
				if(text2.item(t).getNodeName() == "ip")
				{
					camera.ip = text2.item(t).getTextContent().trim();
				}
				if(text2.item(t).getNodeName() == "port")
				{
					camera.port = text2.item(t).getTextContent().trim();
				}
				if(text2.item(t).getNodeName() == "source-type")
				{
					camera.source_type = text2.item(t).getTextContent().trim();
				}
				if(text2.item(t).getNodeName() == "source-protocol")
				{
					camera.source_protocol = text2.item(t).getTextContent().trim();
				}
				//having trouble getting this to interact with the gui class to populate the combox 
				if(text2.item(t).getNodeName() == "source-ip")
				{
					camera.source_ip = text2.item(t).getTextContent().trim();
					System.out.println("adding to combobox");
				}
				if(text2.item(t).getNodeName() == "source-fps")
				{
					camera.source_fps = text2.item(t).getTextContent().trim();
				}
				if(text2.item(t).getNodeName() == "persistent")
				{
					camera.persis = text2.item(t).getTextContent().trim();
				}
				if(text2.item(t).getNodeName() == "exit-on-failure")
				{
					camera.failure = text2.item(t).getTextContent().trim();
				}
				else
					continue;
			}
			proxies.add(camera);
			delete();
		}
	}
		
	//deletes files that are meant to only be temporary, have to figure out a way as well to call this when (X) is clicked
	public void delete()
	{
		//have to make the string into a file to allow the file to be deleted since you cant not delete a string
	    File trash = new File(localFile);
		trash.delete();
		trash.deleteOnExit();
	}
	
	//this method is to save an xml file locally, having some trouble with this as of now when it goes to the other class
	void saveLocal()
	{
	    fc = new JFileChooser ();
        fc.showSaveDialog(saveButton);
        localsave = fc.getSelectedFile();
        //use absolute path to get the whole path + filename to save file in specified location
		String filename = localsave.getAbsolutePath(); //+ ".xml";
        localsave = new File(filename);
        String fname = filename;
        try
        {
             FileOutputStream fileOut = new FileOutputStream(fname);
             PrintStream oos = new PrintStream (fileOut);
             oos.println("<proxies>");
            // camera.print(oos);
             oos.println("</proxies>");
             oos.close();
         } catch (FileNotFoundException e) { 
             System.out.println(e);   
         } catch (IOException e) { 
        	 StringWriter sw = new StringWriter();
        	 PrintWriter pw = new PrintWriter(sw);
        	 e.printStackTrace(pw);
        	 System.out.println("Error = " + sw.toString());
        	 }
	}
	
	//method to choose the file to upload a local file to the server, want to add where you can only see xml files
	void uploadLocal()
	{
		fc = new JFileChooser();
        fc.showOpenDialog(openButton);
        fc.setFileSelectionMode(1);
        uploadFile = fc.getSelectedFile();
        System.out.println(uploadFile);
	}
}

//class that will be used to edit and write back the xml file to a file
class FlexTPSCamera
{
	 public String ip = "";
	 public String port = "";
	 public String source_type = "";
	 public String source_protocol = "";
	 public String source_ip = "";
	 public String source_fps = "";
	 public String persis = "";
	 public String failure = "";

	 // Various Constructors
	 public FlexTPSCamera() {}
	// public FlexTPSCamera(String ip) {this.ip = ip;}
	 
	 //is supposed to print those all to a file 
	 public void print(PrintStream ps) 
	 {
		 ps.println();
		 ps.println("\t<proxy>");
		 ps.println("\t\t <ip>" + ip + "</ip>");
		 ps.println("\t\t <port>" + port + "</port>");
		 ps.println("\t\t <source-type>" + source_type + "</source-type>");
		 ps.println("\t\t <source-protocol>" + source_protocol + "</source-protocol>");
		 ps.println("\t\t <source-ip>" + source_ip + "</source-ip>");
		 ps.println("\t\t <source-fps>" + source_fps + "</source-fps>");
		 ps.println("\t\t <persistent>" + persis + "</persistent>");
		 ps.println("\t\t <exit-on-failure>" + failure + "</exit-on-failure>");
		 ps.println("\t</proxy>");
		 ps.println();
	 }
}