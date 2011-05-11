					/************************************	
					 * FlexTPS editor 					*
					 * Jason Ricles						*
					 * Last Edit: 4/28/11	            *
					 ************************************/
/* Some of the code can be vastly improve by making functions that pass in strings or booleans to decide what to execute, such as one function to print out a failure message with a string passed into it, instead of functions with a string for each case
found this out too late in the project and did not want to break the code since i would lose time, but this can be vastly improve in the future versions if you have the time to fool around with the code.
also you should change the name of some functions in the future mainly the gui ones to give more detail other than "getJButton" and the such to improve code readability.
Errors: need to fix it allowing a user to save a file with no proxies in it, also the connection seems to lose its handle after x amount of portal/proxy uploads and stop and starts */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Collections;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionListener;
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

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import javax.swing.JList;

/**class that sets up the gui for the proxy xml editor
 * 
 * @author Jason Ricles 
 * @version 1.0 4/28/11
 *
 */
public class ProxyGUI extends JFrame implements WindowListener 
{
	private static final long serialVersionUID = 1L;
	private JPanel main_window = null;
	protected JComboBox cameraip = null;
	protected JList cameralist = null;
	private JButton save = null;
	private JButton delete = null;
	private JButton saveserver = null;
	private JButton download = null;
	private JButton download_portal = null;
	private JButton uploadlocal = null;
	private JButton quit = null;
	private JButton add = null;
	private JMenuBar menuBar = null;
	private JMenu server = null;
	private JMenu file = null;
	private JMenuItem connect = null;
	private JMenuItem disconnect = null;
	private JMenuItem saveas = null;
	private JMenuItem saveas2 = null;
	private Login login = new Login();
	private FlexTPSProxy proxy = new FlexTPSProxy();
	private JOptionPane failed = null;
	public boolean downloaded = false;
	private JOptionPane already_downloaded = null;
	public int place = 0;
	protected JCheckBox pers_true = null;
	protected JTextField ports = null;
	protected int high_port = 3000;
	protected ArrayList port_nums = new ArrayList(); 
	protected boolean is_pers = false;
	protected String port_num;
	protected JTextField ips = null;
	protected String default_ip = "127.0.0.1"; 
	private JLabel port_label = null;
	private JLabel pers_label = null;
	private JLabel ip_label = null;
	private JLabel maxconnect_label = null;
	private JLabel sourceip_label = null;
	private JLabel sourceport_label = null;
	private JLabel sourceid_label = null;
	private JLabel sourcewidth_label = null;
	private JLabel sourceheight_label = null;
	private JLabel exitfail_label = null;
	private JLabel botip_label = null;
	private JLabel botport_label = null;
	private JLabel botid_label = null;
	private JLabel verboselog_label = null;
	private JLabel statuslog_label = null;
	private JLabel scalefactor_label = null;
	private JLabel sourcefps_label = null;
	private JLabel sourceprotocol_label = null;
	private JLabel botprotocol_label = null;
	private JLabel sourcetype_label = null;
	private JLabel bottype_label = null;
	private JLabel id_label = null;
	private JLabel portinuse_label = null;
	private JLabel sitename_label = null;
	private JLabel siteip_label = null;
	private JLabel siteadmin_label = null;
	private JLabel feedname_label = null;
	private JLabel groupname_label = null;
	protected JTextField max_connections = null;
	protected String default_maxconnect = "100"; 
	protected JTextField sourceip = null;
	protected String default_sourceip = ""; 
	protected JTextField sourceport = null;
	protected String default_sourceport = ""; 
	protected JTextField sourceid = null;
	protected String default_sourceid = ""; 
	protected JTextField sourcewidth = null;
	protected String default_sourcewidth = ""; 
	protected JTextField sourceheight = null;
	protected String default_sourceheight = ""; 
	protected JCheckBox exit_fail = null;
	protected JCheckBox verb_log = null;
	protected JCheckBox stat_log = null;
	private JScrollPane jsp = null; 
	protected DefaultListModel proxyListModel;
	protected JTextField botip = null;
	protected String default_botip = ""; 
	protected JTextField botport = null;
	protected String default_botport = ""; 
	protected JTextField botid = null;
	protected String default_botid = ""; 
	boolean added = false;
	protected JComboBox scalefactor = null;
	protected JComboBox sourcefps = null;
	protected JComboBox sourceprotocol = null;
	protected JComboBox botprotocol = null;
	protected JComboBox sourcetype = null;
	protected JComboBox bottype = null;
	private JOptionPane already_port = null;
	private JOptionPane confirm;
	JOptionPane redownload;
	public int yes = 0;
	protected JTextField port_id = null;
	protected JCheckBox port_inuse = null;
	public boolean is_proxy = false;
	protected String default_sitename = "lehigh";
	protected String default_siteip = "128.180.53.2";
	protected String default_email = "tmm3@lehigh.edu";
	protected String default_groupname = "NEES Lehigh";
	protected String default_feedname = "RTMD";
	protected JTextField sitename = null;
	protected JTextField siteip = null;
	protected JTextField siteadmin = null;
	protected JTextField feedname = null;
	protected JTextField groupname = null;
	private JButton start_proxy = null;
	private JButton stop_proxy = null;
	private JButton upload_portal = null;
	private JButton save_portal = null;
	private JButton restart_portal = null;

	/**
	 * Entry point
	 * @param args
	 */
	public static void main (String [] args)
	{
		//call to constructor to start the program, needed to export program into jar
		ProxyGUI start = new ProxyGUI();
	}
	
	/**
	 * Constructor to call the super, and call intilize to make the gui
	 */
	public ProxyGUI() 
	{
		super();
		//call to intilize the content pane
		initialize();
		proxy.gui = this;
	}
	
	/**
	 * this method will intilize the gui, as well as the menu bar to save files and connect to the shh server
	 */
	void initialize() 
	 {
		this.setSize(825,600);
		this.setContentPane(getJContentPane());
		this.setTitle("FlexTPS Editor");
		this.setVisible(true);
		addWindowListener(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//makes the menu bar
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		file = new JMenu("File");
		menuBar.add(file);
		saveas = new JMenuItem("Save Proxy as");
		saveas2 = new JMenuItem("Save Portal as");
		file.add(saveas);
		saveas.addMouseListener(new java.awt.event.MouseAdapter() 
		{
			public void mousePressed(java.awt.event.MouseEvent e) 
			{
				proxy.saveLocal();
			}
		});
		file.add(saveas2);
		saveas2.addMouseListener(new java.awt.event.MouseAdapter() 
		{
			public void mousePressed(java.awt.event.MouseEvent e) 
			{
				proxy.saveLocalportal();
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
	/**
	 * This is the method that adds all the interactive items to the JContentPane 
	 * @return JContentPane
	 */
	private JPanel getJContentPane() 
	{
		if (main_window == null) 
		{
			main_window = new JPanel();
			main_window.setLayout(null);
			main_window.add(getJPane(),null);	
			main_window.add(getJButton(), null);
			main_window.add(getJLabel(), null);
			main_window.add(getJLabel1(), null);
			main_window.add(getJLabel2(), null);
			main_window.add(getJLabel3(), null);
			main_window.add(getJLabel4(), null);
			main_window.add(getJLabel5(), null);
			main_window.add(getJLabel6(), null);
			main_window.add(getJLabel7(), null);
			main_window.add(getJLabel8(), null);
			main_window.add(getJLabel9(), null);
			main_window.add(getJLabel10(), null);
			main_window.add(getJLabel11(), null);
			main_window.add(getJLabel12(), null);
			main_window.add(getJLabel13(), null);
			main_window.add(getJLabel14(), null);
			main_window.add(getJLabel15(), null);
			main_window.add(getJLabel16(), null);
			main_window.add(getJLabel17(), null);
			main_window.add(getJLabel18(), null);
			main_window.add(getJLabel19(), null);
			main_window.add(getJLabel20(), null);
			main_window.add(getJLabel21(), null);
			main_window.add(getJLabel22(), null);
			main_window.add(getJLabel23(), null);
			main_window.add(getJLabel24(), null);
			main_window.add(getJLabel25(), null);
			main_window.add(getJLabel26(), null);
			main_window.add(getJLabel27(), null);
			main_window.add(getJButton1(), null);
			main_window.add(getJButton2(), null);
			main_window.add(getJButton3(), null);
			main_window.add(getJButton4(), null);
			main_window.add(getJButton5(), null);
			main_window.add(getJButton6(), null);
			main_window.add(getJButton7(), null);
			main_window.add(getJButton8(), null);
			main_window.add(getJButton9(), null);
			main_window.add(getJButton10(), null);
			main_window.add(getJButton11(), null);
			main_window.add(getJButton12(), null);
			main_window.add(getCheckBox(), null);
			main_window.add(getCheckBox2(), null);
			main_window.add(getCheckBox3(), null);
			main_window.add(getCheckBox4(), null);
			main_window.add(getCheckBox5(), null);
			main_window.add(getTextField(),null);
			main_window.add(getTextField1(),null);
			main_window.add(getTextField2(),null);
			main_window.add(getTextField3(),null);
			main_window.add(getTextField4(),null);
			main_window.add(getTextField5(),null);
			main_window.add(getTextField6(),null);
			main_window.add(getTextField7(),null);
			main_window.add(getTextField8(),null);
			main_window.add(getTextField9(),null);
			main_window.add(getTextField10(),null);
			main_window.add(getTextField11(),null);
			main_window.add(getTextField12(),null);
			main_window.add(getTextField13(),null);
			main_window.add(getTextField14(),null);
			main_window.add(getTextField15(),null);
			main_window.add(getTextField16(),null);
			main_window.add(getJcombobox(),null);
			main_window.add(getJcombobox1(),null);
			main_window.add(getJcombobox2(),null);
			main_window.add(getJcombobox3(),null);
			main_window.add(getJcombobox4(),null);
			main_window.add(getJcombobox5(),null);
		}
		return main_window;
	}
		
	//adds a jlist with all the ips
	/** 
	 * This is the method that adds to the JList all the ips when a user downloads a proxy xml file, and enables all the gui fields for a proxy.
	 * @return JScrollPane
	 * @see #proxy
	 * @see #cameralist
	 * @see #place
	 */
	public JScrollPane getJPane() {
		if (cameralist == null) {
		    proxyListModel = new DefaultListModel();
			cameralist = new JList(proxyListModel);
			proxyListModel.clear();
			jsp = new JScrollPane(cameralist); 
			jsp.setBounds(new Rectangle(15, 25, 125, 100));
			main_window.add(jsp);
			cameralist.addListSelectionListener(new ListSelectionListener() 
			{			
				public void valueChanged(javax.swing.event.ListSelectionEvent e) 
				{
					if (!e.getValueIsAdjusting())
					{
						place = cameralist.getSelectedIndex();
						pers_true.setEnabled(true);
						exit_fail.setEnabled(true);
						ports.setEnabled(true);
						ips.setEnabled(true);
						max_connections.setEnabled(true);
						sourceip.setEnabled(true);
						sourceport.setEnabled(true);
						sourceid.setEnabled(true);
						sourcewidth.setEnabled(true);
						sourceheight.setEnabled(true);
						verb_log.setEnabled(true);
						stat_log.setEnabled(true);
						scalefactor.setEnabled(true);
						sourcefps.setEnabled(true);
						sourceprotocol.setEnabled(true);
						sourcetype.setEnabled(true);
						bottype.setEnabled(true);
						port_inuse.setEnabled(true);
						port_id.setEnabled(true);
						sitename.setEnabled(true);
						siteip.setEnabled(true);
						siteadmin.setEnabled(true);
						feedname.setEnabled(true);
						groupname.setEnabled(true);
						int indexSelected = cameralist.getSelectedIndex();
						if(indexSelected == -1)
							return;
						if(!proxy.proxies.get(place).bot_type.isEmpty())
						{
							botprotocol.setEnabled(true);
							botip.setEnabled(true);
							botport.setEnabled(true);
							botid.setEnabled(true);
						}
						if(proxy.proxies.get(place).bot_type.isEmpty())
						{
							botprotocol.setEnabled(false);
							botip.setEnabled(false);
							botport.setEnabled(false);
							botid.setEnabled(false);
						}
						if(proxy.proxies.get(place).persis.contentEquals("true"))
							pers_true.setSelected(true);	
						else
							pers_true.setSelected(false);
						if(proxy.proxies.get(place).failure.contentEquals("true"))
							exit_fail.setSelected(true);	
						else
							exit_fail.setSelected(false);
						if(proxy.proxies.get(place).verbose_log.contentEquals("true"))
							verb_log.setSelected(true);	
						else
							verb_log.setSelected(false);
						if(proxy.proxies.get(place).useInPortal == true)
						{
							port_inuse.setSelected(true);
							sitename.setEnabled(true);
							siteip.setEnabled(true);
							siteadmin.setEnabled(true);
							feedname.setEnabled(true);
							groupname.setEnabled(true);
						}
						else
						{
							port_inuse.setSelected(false);
							sitename.setEnabled(false);
							siteip.setEnabled(false);
							siteadmin.setEnabled(false);
							feedname.setEnabled(false);
							groupname.setEnabled(false);
						}
						if(proxy.proxies.get(place).status_log.contentEquals("true"))
							stat_log.setSelected(true);	
						else
							stat_log.setSelected(false);
						ports.setText(proxy.proxies.get(place).port);
						ips.setText(proxy.proxies.get(place).ip);
						sourceip.setText(proxy.proxies.get(place).source_ip);
						if(proxy.proxies.get(place).max_connections.isEmpty())
							max_connections.setText(default_maxconnect);
						else
							max_connections.setText(proxy.proxies.get(place).max_connections);
						if(!proxy.proxies.get(place).source_port.isEmpty())
							sourceport.setText(proxy.proxies.get(place).source_port);
						else 
							sourceport.setText("");
						if(!proxy.proxies.get(place).id.isEmpty())
							port_id.setText(proxy.proxies.get(place).id);
						else
							port_id.setText("");
						if(!proxy.proxies.get(place).source_id.isEmpty())
							sourceid.setText(proxy.proxies.get(place).source_id);
						else 
							sourceid.setText("");
						if(!proxy.proxies.get(place).source_width.isEmpty())
							sourcewidth.setText(proxy.proxies.get(place).source_width);
						else 
							sourcewidth.setText("");
						if(!proxy.proxies.get(place).source_height.isEmpty())
							sourceheight.setText(proxy.proxies.get(place).source_height);
						else 
							sourceheight.setText("");
						if(!proxy.proxies.get(place).bot_ip.isEmpty())
							botip.setText(proxy.proxies.get(place).bot_ip);
						else 
							botip.setText("");
						if(!proxy.proxies.get(place).bot_port.isEmpty())
							botport.setText(proxy.proxies.get(place).bot_port);
						else 
							botport.setText("");
						if(!proxy.proxies.get(place).bot_id.isEmpty())
							botid.setText(proxy.proxies.get(place).bot_id);
						else 
							botid.setText("");
						if(proxy.proxies.get(place).scale_factor.isEmpty())
							scalefactor.setSelectedIndex(0);
						else if(!proxy.proxies.get(place).scale_factor.isEmpty())
						{
							if(proxy.proxies.get(place).scale_factor.matches("0.5"))
								scalefactor.setSelectedIndex(1);
							else if(proxy.proxies.get(place).scale_factor.matches("0.25"))
								scalefactor.setSelectedIndex(2);	
							else if(proxy.proxies.get(place).scale_factor.matches("0.125"))
								scalefactor.setSelectedIndex(3);
						}
						if(proxy.proxies.get(place).source_fps.isEmpty())
							sourcefps.setSelectedIndex(0);
						else if(!proxy.proxies.get(place).source_fps.isEmpty())
						{
							if(proxy.proxies.get(place).source_fps.matches("1"))
								sourcefps.setSelectedIndex(1);
							else if(proxy.proxies.get(place).source_fps.matches("2"))
								sourcefps.setSelectedIndex(2);
							else if(proxy.proxies.get(place).source_fps.matches("3"))
								sourcefps.setSelectedIndex(3);
							else if(proxy.proxies.get(place).source_fps.matches("4"))
								sourcefps.setSelectedIndex(4);
							else if(proxy.proxies.get(place).source_fps.matches("5"))
								sourcefps.setSelectedIndex(5);
							else if(proxy.proxies.get(place).source_fps.matches("6"))
								sourcefps.setSelectedIndex(6);
							else if(proxy.proxies.get(place).source_fps.matches("7"))
								sourcefps.setSelectedIndex(7);
							else if(proxy.proxies.get(place).source_fps.matches("8"))
								sourcefps.setSelectedIndex(8);
							else if(proxy.proxies.get(place).source_fps.matches("9"))
								sourcefps.setSelectedIndex(9);
							else if(proxy.proxies.get(place).source_fps.matches("10"))
								sourcefps.setSelectedIndex(10);
							else if(proxy.proxies.get(place).source_fps.matches("15"))
								sourcefps.setSelectedIndex(11);
							else if(proxy.proxies.get(place).source_fps.matches("20"))
								sourcefps.setSelectedIndex(12);
							else if(proxy.proxies.get(place).source_fps.matches("25"))
								sourcefps.setSelectedIndex(13);
							else if(proxy.proxies.get(place).source_fps.matches("30"))
								sourcefps.setSelectedIndex(14);
						}
						if(proxy.proxies.get(place).source_protocol.isEmpty())
							sourceprotocol.setSelectedIndex(0);
						else if(!proxy.proxies.get(place).source_protocol.isEmpty())
						{
							if(proxy.proxies.get(place).source_protocol.matches("http"))
								sourceprotocol.setSelectedIndex(1);
							else if(proxy.proxies.get(place).source_protocol.matches("https"))
								sourceprotocol.setSelectedIndex(2);
						}
						if(proxy.proxies.get(place).bot_protocol.isEmpty())
							botprotocol.setSelectedIndex(0);
						else if(!proxy.proxies.get(place).bot_protocol.isEmpty())
						{
							if(proxy.proxies.get(place).bot_protocol.matches("http"))
								botprotocol.setSelectedIndex(1);
							else if(proxy.proxies.get(place).bot_protocol.matches("https"))
								botprotocol.setSelectedIndex(2);
						}
						if(proxy.proxies.get(place).source_type.isEmpty())
							sourcetype.setSelectedIndex(0);
						else if(!proxy.proxies.get(place).source_type.isEmpty())
						{
							if(proxy.proxies.get(place).source_type.matches("flexTPS::proxy"))
								sourcetype.setSelectedIndex(1);
							else if(proxy.proxies.get(place).source_type.matches("flexTPS::REST"))
								sourcetype.setSelectedIndex(2);
							else if(proxy.proxies.get(place).source_type.matches("Axis::Type1"))
								sourcetype.setSelectedIndex(3);
							else if(proxy.proxies.get(place).source_type.matches("Axis::Type2"))
								sourcetype.setSelectedIndex(4);
							else if(proxy.proxies.get(place).source_type.matches("Sony::SNCRZ30N"))
								sourcetype.setSelectedIndex(5);
							else if(proxy.proxies.get(place).source_type.matches("DLink::DCS900"))
								sourcetype.setSelectedIndex(6);
						}
						if(proxy.proxies.get(place).bot_type.isEmpty())
							bottype.setSelectedIndex(0);
						else if(!proxy.proxies.get(place).bot_type.isEmpty())
						{
							if(proxy.proxies.get(place).bot_type.matches("flexTPS::proxy"))
								bottype.setSelectedIndex(1);
							else if(proxy.proxies.get(place).bot_type.matches("flexTPS::REST"))
								bottype.setSelectedIndex(2);
							else if(proxy.proxies.get(place).bot_type.matches("Axis::Type1"))
								bottype.setSelectedIndex(3);
							else if(proxy.proxies.get(place).bot_type.matches("Axis::Type2"))
								bottype.setSelectedIndex(4);
							else if(proxy.proxies.get(place).bot_type.matches("Sony::SNCRZ30N"))
								bottype.setSelectedIndex(5);
						}
					}
				}});				
		}
		return jsp;
	}
	
	//label for scale factor
	/**
	 * This is the method that makes the jlabel beside the scale factor item, to mark were the scale factor is
	 * @return JLabel
	 */
	private JLabel getJLabel15() 
	{
		if (scalefactor_label == null) {
			scalefactor_label = new JLabel();
			scalefactor_label.setText("Scale-factor:");
			scalefactor_label.setBounds(new Rectangle(150, 225, 284, 16));
		}
		return scalefactor_label;
	}
	
	//scale factor combobox 
	/**
	 * This is the method that will make the combox for the scale factor, in which an array of the numbers that are only allowed are put into the combobox for the user to select from
	 * @return JComboBox
	 */
	public JComboBox getJcombobox()
	{
		if(scalefactor == null)
		{
			String [] scale = {"","0.5","0.25","0.125"};
			scalefactor = new JComboBox(scale);
			scalefactor.setBounds(new Rectangle(225,225,60,16));
			scalefactor.setEnabled(false);
		}
		return scalefactor;
	}
	
	//jlabel for source fps
	/**
	 * This will make the label beside the source fps box, to mark and let the user know where to edit the source fps
	 * @return JLabel
	 */
	private JLabel getJLabel16() 
	{
		if (sourcefps_label == null) {
			sourcefps_label = new JLabel();
			sourcefps_label.setText("Source-fps:");
			sourcefps_label.setBounds(new Rectangle(295, 225, 284, 16));
		}
		return sourcefps_label;
	}
	
	//sourcefps combobox 
	/**
	 * This will make the combobox to allow the use to select the fps for the camera with the given numbers in the scale array
	 * @return JComboBox
	 */
	public JComboBox getJcombobox1()
	{
		if(sourcefps == null)
		{
			String [] scale = {"","1","2","3","4","5","6","7","8","9","10","15","20","25","30"};
			sourcefps = new JComboBox(scale);
			sourcefps.setBounds(new Rectangle(365,225,45,16));
			sourcefps.setEnabled(false);
		}
		return sourcefps;
	}
	
	//jlabel for source protocol
	/**
	 * This will make the jlabel for the source protocol box, so the user will know where they can edit the source protocol
	 * @return JLabel
	 */
	private JLabel getJLabel17() 
	{
		if (sourceprotocol_label == null) {
			sourceprotocol_label = new JLabel();
			sourceprotocol_label.setText("Source-protocol:");
			sourceprotocol_label.setBounds(new Rectangle(420, 225, 284, 16));
		}
		return sourceprotocol_label;
	}
	
	//sourceprotocol combobox
	/**
	 * Makes the combobox for the source protocol, in which the user can only select either http or https
	 * @return JComboBox
	 */
	public JComboBox getJcombobox2()
	{
		if(sourceprotocol == null)
		{
			String [] scale = {"","http","https"};
			sourceprotocol = new JComboBox(scale);
			sourceprotocol.setBounds(new Rectangle(518,225,60,20));
			sourceprotocol.setEnabled(false);
		}
		return sourceprotocol;
	}
	
	//label for robot protocol
	/**
	 * Makes the label for the robotic protocol box so if the user needs to edit the robotic protocol they know where to edit it.
	 * @return JLabel
	 */
	private JLabel getJLabel19() 
	{
		if (botprotocol_label == null) {
			botprotocol_label = new JLabel();
			botprotocol_label.setText("Robotic-protocol:");
			botprotocol_label.setBounds(new Rectangle(510, 150, 284, 16));
		}
		return botprotocol_label;
	}
	
	//botprotocol combobox
	/**
	 * Makes the combobox for the robotic protocol in which the user can only select http or https, and is only enabled when robotic controls are enabled
	 * @return JComboBox
	 * @see #enableBotscontrols()
	 */
	public JComboBox getJcombobox3()
	{
		if(botprotocol == null)
		{
			String [] scale = {"","http","https"};
			botprotocol = new JComboBox(scale);
			botprotocol.setBounds(new Rectangle(610,150,60,20));
			botprotocol.setEnabled(false);
		}
		return botprotocol;
	}
	
	//label for sourcetype
	/**
	 * Makes the label for the source type, so the user where know where to edit the source type
	 * @return JLabel
	 */
	private JLabel getJLabel18() 
	{
		if (sourcetype_label == null) {
			sourcetype_label = new JLabel();
			sourcetype_label.setText("Source-type:");
			sourcetype_label.setBounds(new Rectangle(495, 80, 284, 16));
		}
		return sourcetype_label;
	}
	
	//sourcetype combobox
	/**
	 * Makes the combobox for the source type, and fills with the source types from which the user can select from
	 * @return JComboBox
	 */
	public JComboBox getJcombobox4()
	{
		if(sourcetype == null)
		{
			String [] scale = {"","flexTPS::proxy","flexTPS::REST","Axis::Type1","Axis::Type2","Sony::SNCRZ30N","DLink::DCS900"};
			sourcetype = new JComboBox(scale);
			sourcetype.setBounds(new Rectangle(570,80,130,20));
			sourcetype.setEnabled(false);
		}
		return sourcetype;
	}
	
	//label for robot type
	/**
	 * Makes the label for the robotic type, so the user knows where to edit the robotic type
	 * @return JLabel
	 * @see #enableBotscontrols()
	 */
	private JLabel getJLabel20() 
	{
		if (bottype_label == null) {
			bottype_label = new JLabel();
			bottype_label.setText("Robotic-type:");
			bottype_label.setBounds(new Rectangle(510, 185, 284, 16));
		}
		return bottype_label;
	}
	
	//robotictype combobox
	/**
	 * Makes the combobox in which the user will select the robotic type from the predetermined types to be put into the combobox, only enabaled when robotic controls are true
	 * @return JComboBox
	 * @see #enableBotscontrols()
	 */
	public JComboBox getJcombobox5()
	{
		if(bottype == null)
		{
			String [] scale = {"","flexTPS::proxy","flexTPS::REST","Axis::Type1","Axis::Type2","Sony::SNCRZ30N"};
			bottype = new JComboBox(scale);
			bottype.setBounds(new Rectangle(587,185,130,20));
			bottype.setEnabled(false);
			bottype.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent actionEvent) 
			{
				 enableBotscontrols();
			}});
		}
		return bottype;
	}
	
	/**
	 * This will enabled the robotic controls, if a robotic type is select, else will disable the robotic controls if no robot type is selected
	 */
	private void enableBotscontrols()
	{
		if(bottype.getSelectedIndex() != 0)
		{
			botprotocol.setEnabled(true);
			botip.setEnabled(true);
			botport.setEnabled(true);
			botid.setEnabled(true);
		}
		else 
		{
			botprotocol.setEnabled(false);
			botip.setEnabled(false);
			botport.setEnabled(false);
			botid.setEnabled(false);
		}
	}
	
	//label for persistants 
	/**
	 * makes the label for persistants, so the user knows where persistance is if they want to edit it
	 * @return Jlabel
	 */
	private JLabel getJLabel1() 
	{
		if (pers_label == null) {
			pers_label = new JLabel();
			pers_label.setText("Persistants:");
			pers_label.setBounds(new Rectangle(150, 25, 284, 16));
		}
		return pers_label;
	}
	
	//change persistants to
	/** 
	 * Allows the user to turn persistants on or off through the use of a check box where true if checked and false if not checked
	 * @return JCheckBox
	 */
	private JCheckBox getCheckBox()
	{
		pers_true = new JCheckBox("true");
		pers_true.setBounds(new Rectangle(220, 25, 51, 21));	
		pers_true.setEnabled(false);
		main_window.repaint();
		return pers_true;
	}
	
	//label for exit on failure
	/**
	 * Makes the label for exit on failure, so where the user knows where they can edit exit on failure
	 * @return JLabel
	 */
	private JLabel getJLabel9() 
	{
		if (exitfail_label == null) {
			exitfail_label = new JLabel();
			exitfail_label.setText("Exit-on-failure:");
			exitfail_label.setBounds(new Rectangle(280, 25, 284, 16));
		}
		return exitfail_label;
	}
	
	//change exit on fail to
	/** 
	 * Allows the user to turn exit on failure on or off through the use of a check box where true if checked and false if not checked
	 * @return JCheckBox
	 */
	private JCheckBox getCheckBox2()
	{
		exit_fail = new JCheckBox("true");
		exit_fail.setBounds(new Rectangle(363, 25, 51, 21));	
		exit_fail.setEnabled(false);
		main_window.repaint();
		return exit_fail;
	}
	
	//verbose-log label
	/**
	 * Makes the label for verbose log so the user knows where they can edit the value
	 * @return JLabel
	 */
	private JLabel getJLabel13() 
	{
		if (verboselog_label == null) {
			verboselog_label = new JLabel();
			verboselog_label.setText("Verbose-log:");
			verboselog_label.setBounds(new Rectangle(420, 25, 284, 16));
		}
		return verboselog_label;
	}
	
	//change verbose-log on fail to
	/** 
	 * Allows the user to turn verbose log on or off through the use of a check box where true if checked and false if not checked
	 * @return JCheckBox
	 */
	private JCheckBox getCheckBox3()
	{
		verb_log = new JCheckBox("true");
		verb_log.setBounds(new Rectangle(493, 25, 51, 21));	
		verb_log.setEnabled(false);
		main_window.repaint();
		return verb_log;
	}
	
	//status-log label
	/**
	 * Makes the label for status log, so the user knows where to edit the status log value
	 * @return JLabel
	 */
	private JLabel getJLabel14() 
	{
		if (statuslog_label == null) {
			statuslog_label = new JLabel();
			statuslog_label.setText("Status-log:");
			statuslog_label.setBounds(new Rectangle(550, 25, 284, 16));
		}
		return statuslog_label;
	}
	
	//change status-log on fail to
	/** 
	 * Allows the user to turn status log on or off through the use of a check box where true if checked and false if not checked
	 * @return JCheckBox
	 */
	private JCheckBox getCheckBox4()
	{
		stat_log = new JCheckBox("true");
		stat_log.setBounds(new Rectangle(612, 25, 51, 21));	
		stat_log.setEnabled(false);
		main_window.repaint();
		return stat_log;
	}
	
	//port in use label
	/**
	 * Makes the label for the use in portal, so the user knows where they can edit this value if they need to
	 * @return JLabel
	 */
	private JLabel getJLabel22() 
	{
		if (portinuse_label == null) {
			portinuse_label = new JLabel();
			portinuse_label.setText("Use in Portal:");
			portinuse_label.setBounds(new Rectangle(665, 25, 284, 16));
		}
		return portinuse_label;
	}
	
	//port in use jcheck box
	/** 
	 * Allows the user to turn use in portal on or off through the use of a check box where true if checked and false if not checked
	 * @return JCheckBox
	 */
	private JCheckBox getCheckBox5()
	{
		port_inuse = new JCheckBox("true");
		port_inuse.setBounds(new Rectangle(740, 25, 51, 21));	
		port_inuse.setEnabled(false);
		port_inuse.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent actionEvent) 
		{
			 enablePortcontrols();
		}});
		main_window.repaint();
		return port_inuse;
	}

	//sets the port controls enabled or disabled when the check box is clicked
	//dont think i need the commented code anymore, but kept it in just in case, i needed to test something
	/**
	 * This will enable all the gui items that can be edited for the portal, if the use in portal checkbox is true
	 */
	public void enablePortcontrols()
	{
		//if(port_inuse.getSelectedObjects() == null)
		//{
			//sitename.setEnabled(false);
			//siteip.setEnabled(false);
			//siteadmin.setEnabled(false);
			//feedname.setEnabled(false);
			//groupname.setEnabled(false);
		//}
		//else
		//{
			sitename.setEnabled(true);
			siteip.setEnabled(true);
			siteadmin.setEnabled(true);
			feedname.setEnabled(true);
			groupname.setEnabled(true);
		//}
	}
	
	//label for port
	/**
	 * Makes the label for the port, so the user knows where they can edit the port value
	 * @return JLabel
	 */
	private JLabel getJLabel() 
	{
		if (port_label == null) {
			port_label = new JLabel();
			port_label.setText("Port:");
			port_label.setBounds(new Rectangle(150, 50, 284, 16));
		}
		return port_label;
	}
	
	//sets the port number, if no proxy is selected is defaulted to 3000
	/**
	 * This makes the text field where the port will be held and entered, always defaults to max highest ports number + 1, also each port has to be unique and will not allow to ports of the same value to be used
	 * @return JTextField
	 * @see #high_port
	 * @see #port_num
	 */
	private JTextField getTextField()
	{
		ports = new JTextField();
		ports.setBounds(new Rectangle(180, 50, 51, 21));
		ports.setEnabled(false);
		port_num = Integer.toString(high_port);
		ports.setText(port_num);
		return ports;
	}
	
	//label for ip
	/**
	 * This will make the label for the ip of the proxy, so the user will know where they can edit the ip
	 * @return JLabel
	 */
	private JLabel getJLabel2() 
	{
		if (ip_label == null) {
			ip_label = new JLabel();
			ip_label.setText("IP:");
			ip_label.setBounds(new Rectangle(250, 50, 284, 16));
		}
		return ip_label;
	}
	
	//presents the default ip in a jtextfield
	/**
	 * This is the text field that will hold the ip of the proxy, as well as where the user can change the ip of the proxy, defaults to blank
	 * @return JTextField
	 * @see #default_ip
	 */
	private JTextField getTextField1()
	{
		ips = new JTextField();
		ips.setBounds(new Rectangle(270, 50, 80, 21));
		ips.setEnabled(false);
		ips.setText(default_ip);
		return ips;
	}
	
	//label for max connections
	/**
	 * This will make the label for the max connections within the gui, so the user knows where they can change the value of max connections
	 * @return JLabel
	 */
	private JLabel getJLabel3() 
	{
		if (maxconnect_label == null) {
			maxconnect_label = new JLabel();
			maxconnect_label.setText("Max-Connections:");
			maxconnect_label.setBounds(new Rectangle(370, 50, 284, 16));
		}
		return maxconnect_label;
	}
	
	//jtextfield for maxconnections
	/**
	 * This is the text field that will hold the value of the max number of connections, as well as where the user can edit the value of the max number of connections. Always defaults to 100
	 * @return JTextField
	 * @see #default_maxconnect
	 */
	private JTextField getTextField2()
	{
		max_connections = new JTextField();
		max_connections.setBounds(new Rectangle(480, 50, 40, 21));
		max_connections.setEnabled(false);
		max_connections.setText(default_maxconnect);
		return max_connections;
	}
	
	//label for sourceip
	/**
	 * Makes the label for the user to see where the source ip can be edited
	 * @return JLabel
	 */
	private JLabel getJLabel4() 
	{
		if (sourceip == null) {
			sourceip_label = new JLabel();
			sourceip_label.setText("Source-IP:");
			sourceip_label.setBounds(new Rectangle(150, 80, 284, 16));
		}
		return sourceip_label;
	}
	
	//jtext field for sourceip
	/**
	 * Makes the text field where the user can see and edit the value of the sourceip, is always defaulted to blank
	 * @return JTextField
	 * @see #default_sourceip
	 */
	private JTextField getTextField3()
	{
		sourceip = new JTextField();
		sourceip.setBounds(new Rectangle(215, 80, 90, 21));
		sourceip.setEnabled(false);
		sourceip.setText(default_sourceip);
		return sourceip;
	}
	
	//label for sourceport
	/**
	 * This makes the label for the source port, so the user knows where this box is
	 * @return JLabel
	 */
	private JLabel getJLabel5() 
	{
		if (sourceport_label == null) {
			sourceport_label = new JLabel();
			sourceport_label.setText("Source-Port:");
			sourceport_label.setBounds(new Rectangle(325, 80, 284, 16));
		}
		return sourceport_label;
	}
	
	//jtext field for sourceport
	/**
	 * Makes the text field for the source port, so the user can see the value of the port as well as edit it if they want to
	 * @return JTextField
	 * @see #default_sourceport
	 */
	private JTextField getTextField4()
	{
		sourceport = new JTextField();
		sourceport.setBounds(new Rectangle(400, 80, 90, 21));
		sourceport.setEnabled(false);
		sourceport.setText(default_sourceport);
		return sourceport;
	}
	
	//label for sourceid
	/**
	 * this is the label for the source id, so the user knows where this box is
	 * @return JLabel
	 */
	private JLabel getJLabel6() 
	{
		if (sourceid_label == null) {
			sourceid_label = new JLabel();
			sourceid_label.setText("Source-ID:");
			sourceid_label.setBounds(new Rectangle(150, 115, 284, 16));
		}
		return sourceid_label;
	}
	
	//jtext field for sourceid
	/**
	 * Makes the text field foe the source id, which defaults to blank, and lets the user edit or see the value of the source id as well
	 * @return JTextField
	 * @see #default_sourceid
	 */
	private JTextField getTextField5()
	{
		sourceid = new JTextField();
		sourceid.setBounds(new Rectangle(215, 115, 90, 21));
		sourceid.setEnabled(false);
		sourceid.setText(default_sourceid);
		return sourceid;
	}
	
	//label for sourcewidth
	/**
	 * Makes the label for source width so the user knows where it is
	 * @return JLabel
	 */
	private JLabel getJLabel7() 
	{
		if (sourcewidth_label == null) {
			sourcewidth_label = new JLabel();
			sourcewidth_label.setText("Source-Width:");
			sourcewidth_label.setBounds(new Rectangle(325, 115, 284, 16));
		}
		return sourcewidth_label;
	}
	
	//textfield for sourcewidth
	/** This function returns a JTextField for the source width
	 * @return JTextField
	 * @see #default_sourcewidth
	 */
	private JTextField getTextField6()
	{
		sourcewidth = new JTextField();
		sourcewidth.setBounds(new Rectangle(415, 115, 90, 21));
		sourcewidth.setEnabled(false);
		sourcewidth.setText(default_sourcewidth);
		return sourcewidth;
	}
	
	//label for id
	/**
	 * This makes the label for the port id, so the user knows where to find the port id
	 * @return JLabel
	 */
	private JLabel getJLabel21() 
	{
		if (id_label == null) {
			id_label = new JLabel();
			id_label.setText("ID:");
			id_label.setBounds(new Rectangle(515, 115, 284, 16));
		}
		return id_label;
	}
	
	//textfield for portid
	/**
	 * This makes the text field for the port id, which is defualted to blank, used for the user to see and edit the port id
	 * @return JTextField 
	 */
	private JTextField getTextField11()
	{
		port_id = new JTextField();
		port_id.setBounds(new Rectangle(533, 115, 90, 21));
		port_id.setEnabled(false);
		port_id.setText("");
		return port_id;
	}
	
	//label for sourceheight
	/**
	 * This makes the label for the source height so the user knows where the source height box is
	 * @return JLabel
	 */
	private JLabel getJLabel8() 
	{
		if (sourceheight_label == null) {
			sourceheight_label = new JLabel();
			sourceheight_label.setText("Source-Height:");
			sourceheight_label.setBounds(new Rectangle(150, 150, 284, 16));
		}
		return sourceheight_label;
	}
	
	//jtext field for sourceheight
	/**
	 * This makes the text field for the source height, and defaults to blank, this is where the user can edit and see the source height
	 * @return JTextField
	 * @see #default_sourceheight
	 */
	private JTextField getTextField7()
	{
		sourceheight = new JTextField();
		sourceheight.setBounds(new Rectangle(240, 150, 90, 21));
		sourceheight.setEnabled(false);
		sourceheight.setText(default_sourceheight);
		return sourceheight;
	}
	
	//label for robot ip
	/**
	 * This makes the label for the robot ip so the user knows where this box is if they want to see or edit it
	 * @return JLabel 
	 */
	private JLabel getJLabel10() 
	{
		if (botip_label == null) {
			botip_label = new JLabel();
			botip_label.setText("Robotic-IP:");
			botip_label.setBounds(new Rectangle(345, 150, 284, 16));
		}
		return botip_label;
	}
	
	//text field for robotip
	/**
	 * This makes the text field for the robot ip which is only enabled if a robotic type is selected, defaulted to blank
	 * @return JTextField
	 * @see #enableBotscontrols()
	 * @see #default_botip
	 */
	private JTextField getTextField8()
	{
		botip = new JTextField();
		botip.setBounds(new Rectangle(410, 150, 90, 21));
		botip.setEnabled(false);
		botip.setText(default_botip);
		return botip;
	}
	
	//label for robot port
	/**
	 * This makes the label for the robot port so the user knows where this box is if they want to see or edit it
	 * @return JLabel 
	 */
	private JLabel getJLabel11() 
	{
		if (botport_label == null) {
			botport_label = new JLabel();
			botport_label.setText("Robotic-port:");
			botport_label.setBounds(new Rectangle(150, 185, 284, 16));
		}
		return botport_label;
	}
	
	//text field for robot port
	/**
	 * This makes the text field for the robot port which is only enabled if a robotic type is selected, defaulted to blank
	 * @return JTextField
	 * @see #enableBotscontrols()
	 * @see #default_botport
	 */
	private JTextField getTextField9()
	{
		botport = new JTextField();
		botport.setBounds(new Rectangle(230, 185, 90, 21));
		botport.setEnabled(false);
		botport.setText(default_botport);
		return botport;
	}
	
	//label for robot id
	/**
	 * This makes the label for the robot id so the user knows where this box is if they want to see or edit it
	 * @return JLabel 
	 */
	private JLabel getJLabel12() 
	{
		if (botid_label == null) {
			botid_label = new JLabel();
			botid_label.setText("Robotic-ID:");
			botid_label.setBounds(new Rectangle(345, 185, 284, 16));
		}
		return botid_label;
	}
	
	//text field for robot id
	/**
	 * This makes the text field for the robot id which is only enabled if a robotic type is selected, defaulted to blank
	 * @return JTextField
	 * @see #enableBotscontrols()
	 * @see #default_botid
	 */
	private JTextField getTextField10()
	{
		botid = new JTextField();
		botid.setBounds(new Rectangle(410, 185, 90, 21));
		botid.setEnabled(false);
		botid.setText(default_botid);
		return botid;
	}
	
	//jlabel for sitename
	/**
	 * This makes the label for the sitename for the portal so the user knows where this box is if they want to see or edit it
	 * @return JLabel 
	 */
	private JLabel getJLabel23() 
	{
		if (sitename_label == null) {
			sitename_label = new JLabel();
			sitename_label.setText("Site-name:");
			sitename_label.setBounds(new Rectangle(150, 400, 284, 16));
		}
		return sitename_label;
	}
	
	//presents the site name
	/**
	 * This makes the text field for the site name, only enabled if use in portal is selected
	 * @return JTextField
	 * @see #enablePortcontrols()
	 * @see #default_sitename
	 */
	private JTextField getTextField12()
	{
		sitename = new JTextField();
		sitename.setBounds(new Rectangle(215, 400, 100, 21));
		sitename.setEnabled(false);
		sitename.setText(default_sitename);
		return sitename;
	}
	
	//jlabel for siteip
	/**
	 * This makes the label for the site ip for the portal so the user knows where this box is if they want to see or edit it
	 * @return JLabel 
	 */
	private JLabel getJLabel24() 
	{
		if (siteip_label == null) {
			siteip_label = new JLabel();
			siteip_label.setText("Site-ip:");
			siteip_label.setBounds(new Rectangle(325, 400, 284, 16));
		}
		return siteip_label;
	}
	
	//jtextfield for siteip
	/**
	 * This makes the text field for the site ip, only enabled if use in portal is selected
	 * @return JTextField
	 * @see #enablePortcontrols()
	 * @see #default_siteip
	 */
	private JTextField getTextField13()
	{
		siteip = new JTextField();
		siteip.setBounds(new Rectangle(368, 400, 100, 21));
		siteip.setEnabled(false);
		siteip.setText(default_siteip);
		return siteip;
	}
	
	//jlabel for groupname
	/**
	 * This makes the label for the group name for the portal so the user knows where this box is if they want to see or edit it
	 * @return JLabel 
	 */
	private JLabel getJLabel27() 
	{
		if (groupname_label == null) {
			groupname_label = new JLabel();
			groupname_label.setText("Group-name:");
			groupname_label.setBounds(new Rectangle(480, 400, 284, 16));
		}
		return groupname_label;
	}
	
	//jtext field for group name
	/**
	 * This makes the text field for the group name, only enabled if use in portal is selected
	 * @return JTextField
	 * @see #enablePortcontrols()
	 * @see #default_groupname
	 */
	private JTextField getTextField16()
	{
		groupname = new JTextField();
		groupname.setBounds(new Rectangle(555, 400, 150, 21));
		groupname.setEnabled(false);
		groupname.setText(default_groupname);
		return groupname;
	}
	
	//jlabel for siteadmin
	/**
	 * This makes the label for the site admin for the portal so the user knows where this box is if they want to see or edit it
	 * @return JLabel 
	 */
	private JLabel getJLabel25() 
	{
		if (siteadmin_label == null) {
			siteadmin_label = new JLabel();
			siteadmin_label.setText("Site-admin email:");
			siteadmin_label.setBounds(new Rectangle(150, 450, 284, 16));
		}
		return siteadmin_label;
	}
	
	//jtext field for siteadmin email
	/**
	 * This makes the text field for the site admin, only enabled if use in portal is selected
	 * @return JTextField
	 * @see #enablePortcontrols()
	 * @see #default_email
	 */
	private JTextField getTextField14()
	{
		siteadmin = new JTextField();
		siteadmin.setBounds(new Rectangle(252, 450, 150, 21));
		siteadmin.setEnabled(false);
		siteadmin.setText(default_email);
		return siteadmin;
	}
	
	//jlabel for feedname
	/**
	 * This makes the label for the feed name for the portal so the user knows where this box is if they want to see or edit it
	 * @return JLabel 
	 */
	private JLabel getJLabel26() 
	{
		if (feedname_label == null) {
			feedname_label = new JLabel();
			feedname_label.setText("Feed name:");
			feedname_label.setBounds(new Rectangle(415, 450, 284, 16));
		}
		return feedname_label;
	}
	
	//jtext field for feedname
	/**
	 * This makes the text field for the feed name, only enabled if use in portal is selected
	 * @see #enablePortcontrols()
	 * @see #default_feedname
	 */
	private JTextField getTextField15()
	{
		feedname = new JTextField();
		feedname.setBounds(new Rectangle(485, 450, 150, 21));
		feedname.setEnabled(false);
		feedname.setText(default_feedname);
		return feedname;
	}
	
	/**
	 * This makes the button to save into the arraylist the current proxy you have selected and edited in the jlist, on clicking save goes through and pulls all the text and the such from the gui items
	 * @return JButton
	 * @see #proxy
	 */
	//make button to save to arraylist, commented out saving if only if not empty in case it has to be used in future use for some reason, but as of now saving a file first checks if something is not empty before writing the xml node
	private JButton getJButton() 
	{
		if (save == null) 
		{
			save = new JButton();
			save.setBounds(new Rectangle(215, 275, 115, 25));
			save.setText("Save");
			save.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
					int selected = cameralist.getSelectedIndex();
					if(selected == -1)
						return;
					int t = Integer.parseInt(ports.getText().toString());
					int count = 1;
					for(int i = 0; i < proxy.proxies.size(); i++)
					{
						if(proxy.proxies.get(i).port.matches(ports.getText()) && i != selected)
						{
							already_port = new JOptionPane();
							already_port.setSize(new Dimension(119, 69));
							JOptionPane.showMessageDialog(already_port, "Already used port");
								return;
						}
					}
					proxy.proxies.get(selected).port = ports.getText();
					proxy.proxies.get(selected).id = port_id.getText();
					if(pers_true.isSelected())
						proxy.proxies.get(selected).persis = "true";
					else
						proxy.proxies.get(selected).persis = "false";
					if(port_inuse.isSelected())
						proxy.proxies.get(selected).useInPortal = true;
					else 
						proxy.proxies.get(selected).useInPortal = false;
					if(exit_fail.isSelected())
						proxy.proxies.get(selected).failure = "true";
					else
						proxy.proxies.get(selected).failure = "false";
					if(verb_log.isSelected())
						proxy.proxies.get(selected).verbose_log = "true";
					else
						proxy.proxies.get(selected).verbose_log = "false";
					if(stat_log.isSelected())
						proxy.proxies.get(selected).status_log = "true";
					else
						proxy.proxies.get(selected).status_log = "false";
					proxy.proxies.get(selected).ip = ips.getText();
					proxy.proxies.get(selected).source_ip = sourceip.getText();
					proxy.proxies.get(selected).max_connections = max_connections.getText();
					//if(!sourceport.getText().isEmpty())
						proxy.proxies.get(selected).source_port = sourceport.getText();
					//if(!sourceid.getText().isEmpty())
						proxy.proxies.get(selected).source_id = sourceid.getText();
					//if(!sourcewidth.getText().isEmpty())
						proxy.proxies.get(selected).source_width = sourcewidth.getText();
					//if(!sourceheight.getText().isEmpty())
						proxy.proxies.get(selected).source_height = sourceheight.getText();
					proxy.proxies.get(selected).scale_factor = (String) scalefactor.getSelectedItem();
					proxy.proxies.get(selected).source_fps = (String) sourcefps.getSelectedItem();
					proxy.proxies.get(selected).source_protocol = (String) sourceprotocol.getSelectedItem();
					proxy.proxies.get(selected).source_type = (String) sourcetype.getSelectedItem();
					proxy.proxies.get(selected).bot_type = (String) bottype.getSelectedItem();
					if(bottype.getSelectedIndex() != 0)
					{
						proxy.proxies.get(selected).bot_protocol = (String) botprotocol.getSelectedItem();
						//if(!botid.getText().isEmpty())
							proxy.proxies.get(selected).bot_id = botid.getText();
						//if(!botip.getText().isEmpty())
							proxy.proxies.get(selected).bot_ip = botip.getText();
						//if(!botport.getText().isEmpty())
							proxy.proxies.get(selected).bot_port = botport.getText();
					}
					else
					{
						proxy.proxies.get(selected).bot_protocol = "";
						proxy.proxies.get(selected).bot_id = "";
						proxy.proxies.get(selected).bot_ip = "";
						proxy.proxies.get(selected).bot_port = "";
					}
					boolean current_port_nums = port_nums.add(ports.getText());
					Collections.sort(port_nums);
					high_port = port_nums.lastIndexOf(port_nums);
					proxyListModel.clear();
					for(int i = 0; i < proxy.proxies.size(); i++)
					{
						proxyListModel.addElement(proxy.proxies.get(i).port);
					}
				}
			});
		}
		return save;
	}
	
	//make button to save to server
	/**
	 * This makes the button so the user can save the file direclty to the server through the use of a temporary file which will be deleted after upload
	 * @return JButton
	 * @see #proxy
	 * @see #confirm
	 * @see #failed
	 * @see #saveserver
	 */
	private JButton getJButton1() 
	{
		if (saveserver == null) 
		{
			saveserver = new JButton();
			saveserver.setBounds(new Rectangle(337, 275, 130, 25));
			saveserver.setText("Save to Server");
			saveserver.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
							confirm = new JOptionPane();
							confirm.setSize(new Dimension(119, 69));
							yes = JOptionPane.showConfirmDialog(confirm,"Upload " + "file" +"?","Upload",JOptionPane.YES_NO_OPTION);
						if(yes == JOptionPane.YES_OPTION)
						{
						try
					{
					    String sys_path = "/opt/flexTPS";
						proxy.uploadBack("/proxies/etc","proxies.xml");
					}
						catch(Exception e1)
						 {
							failed = new JOptionPane();
							failed.setSize(new Dimension(119, 69));
							JOptionPane.showMessageDialog(failed, "Failed to upload file " + "file");
							proxy.delete();
							System.out.println(e1);
						 }
			}
				}
			});
		}
		return saveserver;
	}
	
	//make button to download file to fill combo box
	/**
	 * This will make the button to download the xml file when connected to the ssh server
	 * @return JButton
	 * @see #download
	 * @see #proxy
	 * @see #failed
	 */
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
						is_proxy = true;
						proxy.sys_path = "/opt/flexTPS";
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
	/**
	 * This makes the button to upload a local file to the xml server, the user selects the file with a file chooser
	 * @return JButton
	 * @see #uploadlocal
	 * @see #proxy
	 */
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
					try {
						proxy.sys_path = "/opt/flexTPS";
						proxy.uploadLocal("/proxies/etc");
					} catch (JSchException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SftpException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		}
		return uploadlocal;
	}
	
	//button to exit program, deletes all temp files and disconnects everything when run
	/**
	 * This makes the button that the user will click when they want to exit the program, calls system exit to do such, as well as a function to make sure the process dies
	 * @return JButton
	 * @see #quit
	 */
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
	
	//button to add to xml file
	/**
	 * This makes the button where the user can then choose to add a proxy to an already downloaded xml file, or to a new xml file, will also then resort the port nums to find the highest one
	 * @return JButton
	 * @see #add
	 * @see #port_nums
	 */
	private JButton getJButton5() 
	{
		if (add == null) 
		{
			add = new JButton();
			add.setBounds(new Rectangle(15, 150, 93, 25));
			add.setText("Add");
			add.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mousePressed(java.awt.event.MouseEvent e) 
				{
					
					proxy.camera = new FlexTPSCamera(); 
					if(port_nums.isEmpty())
					{
						port_num = "3000";
						high_port = Integer.parseInt(port_num);
					}
					else
					{
						port_num = port_nums.get(port_nums.size()-1).toString();
						high_port = Integer.parseInt(port_num);
						high_port++;
					}
					port_num = Integer.toString(high_port);
					proxyListModel.addElement(port_num);
					proxy.camera.port = port_num;
					port_nums.add(port_num);
					proxy.proxies.add(proxy.camera);	
				}
			});
		}
		return add;
	}
	
	//button to delete from xml file
	/**
	 * This will make the button that is used to delete a proxy from the list model as well as the array list
	 * @return JButton
	 * @see #delete
	 * @see #proxyListModel
	 */
	private JButton getJButton6() 
	{
		if (delete == null) 
		{
			delete = new JButton();
			delete.setBounds(new Rectangle(15, 190, 93, 25));
			delete.setText("Delete");
			delete.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mousePressed(java.awt.event.MouseEvent e) 
				{
					int indexSelected = cameralist.getSelectedIndex();
					if(indexSelected == -1)
						return;
					else 
					{
						for(int i = 0; i < port_nums.size(); i++)
						{
							if(port_nums.get(i).equals(proxy.proxies.get(indexSelected).port))
							{
								port_nums.remove(i);
							}
						}
						proxyListModel.remove(indexSelected);
						proxy.proxies.remove(indexSelected);
						proxyListModel.clear();
						for (int i = 0; i < proxy.proxies.size(); i++)
						{
							proxyListModel.addElement(proxy.proxies.get(i).port);
						}	
						Collections.sort(port_nums);
						high_port = port_nums.lastIndexOf(port_nums);
						pers_true.setEnabled(false);
						exit_fail.setEnabled(false);
						ports.setEnabled(false);
						ips.setEnabled(false);
						max_connections.setEnabled(false);
						sourceip.setEnabled(false);
						sourceport.setEnabled(false);
						sourceid.setEnabled(false);
						sourcewidth.setEnabled(false);
						sourceheight.setEnabled(false);
						verb_log.setEnabled(false);
						stat_log.setEnabled(false);
						scalefactor.setEnabled(false);
						sourcefps.setEnabled(false);
						sourceprotocol.setEnabled(false);
						sourcetype.setEnabled(false);
						bottype.setEnabled(false);
					}
				}
			});
		}
		return delete;
	}
	
	//button that will download the portal file 
	/**
	 * This will make the button to download the portal file (all it does for now and just download and delete file)
	 * @return JButton
	 * @see #download_portal
	 * @see #proxy
	 */
	private JButton getJButton7() 
	{
		if (download_portal == null) 
		{
			download_portal = new JButton();
			download_portal.setBounds(new Rectangle(15, 315, 130, 25));
			download_portal.setText("Download Portal");
			download_portal.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
					proxy.sys_path = "/opt/flexTPS";
					try {
						is_proxy = false;
						proxy.getFile("/portal/etc/conf","portal.xml");
					} catch (JSchException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SftpException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ParserConfigurationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SAXException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		}
		return download_portal;
	}
	
	//button that will start the portal
	/**
	 * This makes the button that the user will click if they want to start the proxy
	 * @return JButton
	 * @see #start_proxy
	 */
	private JButton getJButton8() 
	{
		if (start_proxy == null) 
		{
			start_proxy = new JButton();
			start_proxy.setBounds(new Rectangle(150, 315, 100, 25));
			start_proxy.setText("Start Proxy");
			start_proxy.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
					try
					{
						proxy.start_proxy("sudo /sbin/service flextps_proxies start\n","Proxy Started","Failed to stop Proxy");
					}
					catch (Exception e1)
					{
						failed = new JOptionPane();
						failed.setSize(new Dimension(119, 69));
						JOptionPane.showMessageDialog(failed, "Failed to start proxy");
					}
				}
			});
		}
		return start_proxy;
	}
	
	//button that will upload the portal file 
	/**
	 * This will make the button that the user will click to upload a local portal file
	 * @return JButton
	 * @see #upload_portal
	 * @see #proxy
	 */
	private JButton getJButton10() 
	{
		if (upload_portal == null) 
		{
			upload_portal = new JButton();
			upload_portal.setBounds(new Rectangle(360, 315, 130, 25));
			upload_portal.setText("Upload Portal");
			upload_portal.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mousePressed(java.awt.event.MouseEvent e) 
				{
					try {
						proxy.sys_path = "/opt/flexTPS";
						proxy.uploadLocal("/portal/etc/conf");
					} catch (JSchException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SftpException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		}
		return upload_portal;
	}
	
	//button that will upload the portal file 
	/**
	 * This will make the button for the user to directly upload a portal file that is not saved locally on the system
	 * @return JButton
	 * @see #save_portal
	 * @see #failed
	 * @see #proxy
	 */
	private JButton getJButton11() 
	{
		if (save_portal == null) 
		{
			save_portal = new JButton();
			save_portal.setBounds(new Rectangle(495, 315, 130, 25));
			save_portal.setText("Save to Portal");
			save_portal.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mousePressed(java.awt.event.MouseEvent e) 
				{
					confirm = new JOptionPane();
					confirm.setSize(new Dimension(119, 69));
					yes = JOptionPane.showConfirmDialog(confirm,"Upload " + "file" +"?","Upload",JOptionPane.YES_NO_OPTION);
				if(yes == JOptionPane.YES_OPTION)
				{
				try
			{
			    String sys_path = "/opt/flexTPS";
				proxy.uploadBackPortal("/portal/etc/conf","portal.xml");
			}
				catch(Exception e1)
				 {
					failed = new JOptionPane();
					failed.setSize(new Dimension(119, 69));
					JOptionPane.showMessageDialog(failed, "Failed to upload file " + "file");
					proxy.delete();
					System.out.println(e1);
				 }
					}}});
			}
		return save_portal;
	}
	
	//button to restart portal, on click calls start porxy function and just passes in a different command to have it restart a portal
	/**
	 * This will makes the button that is used to restart the portal
	 * @return JButton
	 * @see #restart_portal
	 * @see #failed
	 */
	private JButton getJButton12() 
	{
		if (restart_portal == null) 
		{
			restart_portal = new JButton();
			restart_portal.setBounds(new Rectangle(630, 315, 125, 25));
			restart_portal.setText("Restart Portal");
			restart_portal.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
					try
					{
						proxy.start_proxy("sudo /sbin/service flextps_httpd restart\n","Portal restarted","Failed to restart portal");
					}
					catch (Exception e1)
					{
						failed = new JOptionPane();
						failed.setSize(new Dimension(119, 69));
						JOptionPane.showMessageDialog(failed, "Failed to restart portal");
					}
				}
			});
			}
		return restart_portal;
	}
	
	//button that will stop the proxy
	/**
	 * This will make the button that when clicked will stop the proxies
	 * @return JButton
	 * @see #start_proxy
	 * @see #failed
	 */
	private JButton getJButton9() 
	{
		if (stop_proxy == null) 
		{
			stop_proxy = new JButton();
			stop_proxy.setBounds(new Rectangle(255, 315, 100, 25));
			stop_proxy.setText("Stop Proxy");
			stop_proxy.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
					try
					{
						proxy.stop_proxy("sudo /sbin/service flextps_proxies stop\n","Proxy stopped");
					}
					catch (Exception e1)
					{
						failed = new JOptionPane();
						failed.setSize(new Dimension(119, 69));
						JOptionPane.showMessageDialog(failed, "Failed to stop proxy");
					}
				}
			});
		}
		return stop_proxy;
	}
	
	//prints out message if combobox is already filled
	/**
	 * This method is used for when the xml file has already been downloaded, it will clear the list model redownload the file so that the same proxies do not appear more than once in the gui, and tells if downloading failed
	 * @see #failed
	 */
	void already()
	{
		//already_downloaded = new JOptionPane();
		//already_downloaded.setSize(new Dimension(119, 69));
		//JOptionPane.showMessageDialog(already_downloaded, "Already downloaded file");
		//code below fails when redownloading xml file
	    proxyListModel.clear();
			try 
			{
				proxy.sys_path = "/opt/flexTPS";
				proxy.getFile("/proxies/etc","proxies.xml");
			} 
			catch(Exception e1)
			 {
				failed = new JOptionPane();
				failed.setSize(new Dimension(119, 69));
				JOptionPane.showMessageDialog(failed, "Failed to download file " + proxy.remotefile);
			 }
	}
	//had to include due to window listener, serves no purpose as of now
	/**
	 * 	had to include due to window listener, serves no purpose as of now
	 */
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	//when window closes, this makes it clean up all temp files, as well as free up system resoucres and dispose of the jframe
	/**
	 * when window closes, this makes it clean up all temp files, as well as free up system resoucres and dispose of the jframe
	 */
	@Override
	public void windowClosed(WindowEvent e) 
	{
		proxy.delete();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		this.dispose();
	}
	//when window closing, this makes it clean up all temp files, as well as free up system resoucres and dispose of the jframe
	/**
	 * when window is closing, this makes it clean up all temp files, as well as free up system resoucres and dispose of the jframe
	 */
	@Override
	public void windowClosing(WindowEvent e) 
	{
		proxy.delete();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		this.dispose();
	}
	//had to include due to window listener, serves no purpose as of now
	/**
	 * 	had to include due to window listener, serves no purpose as of now
	 */
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	//had to include due to window listener, serves no purpose as of now
	/**
	 * 	had to include due to window listener, serves no purpose as of now
	 */
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	//had to include due to window listener, serves no purpose as of now
	/**
	 * 	had to include due to window listener, serves no purpose as of now
	 */
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	//had to include due to window listener, serves no purpose as of now
	/**
	 * 	had to include due to window listener, serves no purpose as of now
	 */
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}

//class to log the user in
/**
 * Class that is used for the backend code to log the user in
 */
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

	/**
	 * Method that is used to connect the user to the ssh server, by calling other methods to get their password and username
	 * @see #getUserinfo()
	 * @see #getUserpass()
	 * @see #channel
	 * @see #session
	 */
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
	/**
	 * This is the method that is used to get the users information such as the host and the user to log into the shh server through the use of a pop up window
	 * @return JOptionPane
	 * @see #host
	 * @see #user
	 */
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
	/**
	 * This is the method used to get the users password, through the use of a pop up window
	 * @return JOptionPane
	 * @see #pass
	 */
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
	/**
	 * This is the method that is used to display the pop up when the connection to the server is a sucess 
	 */
	void good()
	{
		connected = new JOptionPane();
		connected.setSize(new Dimension(119, 69));
		JOptionPane.showMessageDialog(connected, "You are now connected to " + host);
		good = true;
	}
	
	//confirm no connection happened
	/**
	 * This is the method used to display the pop up when it was unable to connection to the shh server
	 */
	void bad()
	{
		noconnect = new JOptionPane();
		noconnect.setSize(new Dimension(119, 69));
		JOptionPane.showMessageDialog(noconnect, "Could not connect to " + host);
	}
	
	//tell user they are alrady logged into a server
	/**
	 * This displays a pop up if a user trys to log into an ssh server when they are already connected to one
	 */
	void already()
	{
		noconnect = new JOptionPane();
		noconnect.setSize(new Dimension(119, 69));
		JOptionPane.showMessageDialog(noconnect, "Already connected to " + host);
	}
	
	//logs user out from server
	/**
	 * This is the method that is used to log the user out from the ssh server if they decide to dissconnect
	 * @see #channel
	 * @see #session
	 */
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
/**
 * This is the class where the backcode of the program to interact with the ssh server, as in upload files, download files, stop and start the server lays
 */
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
	public NodeList portal;
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
	public ProxyGUI gui;
	protected String path;
	protected Channel proxy_channel;

	//constructor
	/**
	 * This is a contrcutor that sets up some of the variables for the FlexTPS proxy class
	 * @see #factory
	 * @see #proxies
	 */
	public FlexTPSProxy()
	{
		factory = DocumentBuilderFactory.newInstance();
		proxies = new ArrayList <FlexTPSCamera>();
	}
	
	//constructor 
	/**
	 * Sets up some more vaiables through parameters 
	 * @param file used to set up a file descriptor 
	 * @param factory the documentbuilderfactry for the xml file
	 * @param ip the string for the ip address to use
	 */
	public FlexTPSProxy(File file,DocumentBuilderFactory factory,String ip)
	{
		this.file = file;
		this.factory = factory;
	}
	
	//this is the method to start the proxies, and confirms it has started with a popup
	/**
	 * this is the method to start the proxies or restarts the portal depending on what param is passed, and confirms it has with a popup
	 * @param command the command for what to do with the proxy or portal
	 * @param sucess what to print out in the message box if the function works properly without an error 
	 * @param failure what to print out in the message box if the function throws an error 
	 * @throws JSchException, InterruptedException 
	 */
	void start_proxy(String command, String sucess, String failure) throws JSchException, InterruptedException
	{
	    JOptionPane start= new JOptionPane();
		start.setSize(new Dimension(119, 69));
		proxy_channel = login.session.openChannel("shell");
		InputStream input = null;
	      try 
	      {   
	        input = new ByteArrayInputStream(command.getBytes("UTF-8"));    	  
		  } 
	      catch (UnsupportedEncodingException e) 
	      {
			  JOptionPane.showMessageDialog(start, failure);
	      }	
	      proxy_channel.setInputStream(input);
	      proxy_channel.setOutputStream(System.out);      
	      proxy_channel.connect(3*1000);      
	      Thread.sleep(1000);
		  JOptionPane.showMessageDialog(start, sucess);
	}
	
	//this is the method to stop the proxies, and confirms it has started with a popup
	/**
	 * this is the method to stop the proxies, and confirms it has with a popup
	 * @param command the command for what to do with the proxy 
	 * @param sucess what to print out in the message box if the function works properly without an error 
	 * @throws JSchException, InterruptedException 
	 */
	void stop_proxy(String command, String sucess) throws JSchException, InterruptedException
	{
	    JOptionPane start= new JOptionPane();
		start.setSize(new Dimension(119, 69));
		proxy_channel = login.session.openChannel("shell");
		InputStream input = null;
	      try 
	      {   
	        input = new ByteArrayInputStream(command.getBytes("UTF-8"));    	  
		  } 
	      catch (UnsupportedEncodingException e) 
	      {
			  JOptionPane.showMessageDialog(start, "Failed to stop");
	      }	
	      proxy_channel.setInputStream(input);
	      proxy_channel.setOutputStream(System.out);      
	      proxy_channel.connect(3*1000);      
	      Thread.sleep(1000);
		  JOptionPane.showMessageDialog(start, sucess);
	}
	
	//method to download proxy files
	/**
	 * This is the method that is used to download the get the file from the ssh server 
	 * @param path this is the path to the file that will be downloaded
	 * @param remotefile this is the name of the file on the server to download
	 * @throws JSchException, SftpException, ParserConfigurationException, SAXException, IOException
	 */
	void getFile(String path,String remotefile) throws JSchException, SftpException, ParserConfigurationException, SAXException, IOException
	{
		remoteDirectory = sys_path;
		login.channel = (ChannelSftp)login.session.openChannel("sftp"); 
		login.channel.connect(); 
		remoteDirectory = remoteDirectory + path;
		login.channel.cd(remoteDirectory); 
		//checks if it is downloading a proxy or portal file
		if(gui.is_proxy ==true)
			localFile = "~proxies.xml";
		else
			localFile = "~portal.xml";
		login.channel.get((remotefile), localFile); 
		docBuilder = factory.newDocumentBuilder();
		doc = docBuilder.parse(localFile);
		doc.getDocumentElement ().normalize ();
		//will check if it is parsing a proxy or portal file 
		if(gui.is_proxy ==true)
			getProxies();
		else
			getPortals();
	}
	
	//just gets the proxy elemnts 
	/**
	 * Used to get the proxy elements of a file, into a nodelist
	 * @see #proxy
	 */
	void getProxies()
	{
		proxy = doc.getElementsByTagName("proxy");
		add();
	}
	//will be used to get the portal elements 
	/**
	 * Used to get the portal elements of a file, into a nodelist
	 * @see #portal
	 */
	void getPortals()
	{
		portal = doc.getElementsByTagName("portal");
		delete();
	}
	
	//adds all the xml elements to the arraylist and puts the ips in the combo box
	/**
	 * This will be used to add the xml elements to the arraylist, and then put the ports into the combobox of the gui, then calls the method to delete the temporary file with the xml information
	 * @see #proxies
	 * @see #gui
	 * @see #delete()
	 */
	public void add()
	{
		if(gui.downloaded == true)
		{
			gui.redownload = new JOptionPane();
			gui.redownload.setSize(new Dimension(119, 69));
			gui.yes = JOptionPane.showConfirmDialog(gui.redownload,"Download the file again?","Download",JOptionPane.YES_NO_OPTION);
			if(gui.yes == JOptionPane.YES_OPTION)
				{
					gui.proxyListModel.clear();
					proxies.clear();
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
								gui.port_nums.add(camera.port);
								gui.proxyListModel.addElement(camera.port);	
							}
							if(text2.item(t).getNodeName() == "source-type")
							{
								camera.source_type = text2.item(t).getTextContent().trim();
							}
							if(text2.item(t).getNodeName() == "source-protocol")
							{
								camera.source_protocol = text2.item(t).getTextContent().trim();
							}
							if(text2.item(t).getNodeName() == "source-ip")
							{
								camera.source_ip = text2.item(t).getTextContent().trim();
								//gui.proxyListModel.addElement(camera.source_ip);				
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
							if(text2.item(t).getNodeName() == "scale-factor")
							{
								camera.scale_factor = text2.item(t).getTextContent().trim();
							}
							if(text2.item(t).getNodeName() == "max-connections")
							{
								camera.max_connections = text2.item(t).getTextContent().trim();
							}
							if(text2.item(t).getNodeName() == "source-port")
							{
								camera.source_port = text2.item(t).getTextContent().trim();
							}
							if(text2.item(t).getNodeName() == "source-id")
							{
								camera.source_id = text2.item(t).getTextContent().trim();
							}
							if(text2.item(t).getNodeName() == "source-width")
							{
								camera.source_width = text2.item(t).getTextContent().trim();
							}
							if(text2.item(t).getNodeName() == "source-height")
							{
								camera.source_height = text2.item(t).getTextContent().trim();
							}
							if(text2.item(t).getNodeName() == "robotic-type")
							{
								camera.bot_type = text2.item(t).getTextContent().trim();
							}
							if(text2.item(t).getNodeName() == "robotic-protocol")
							{
								camera.bot_protocol = text2.item(t).getTextContent().trim();
							}
							if(text2.item(t).getNodeName() == "robotic-ip")
							{
								camera.bot_ip = text2.item(t).getTextContent().trim();
							}
							if(text2.item(t).getNodeName() == "robotic-port")
							{
								camera.bot_port = text2.item(t).getTextContent().trim();
							}
							if(text2.item(t).getNodeName() == "robotic-id")
							{
								camera.bot_id = text2.item(t).getTextContent().trim();
							}
							if(text2.item(t).getNodeName() == "verbose-log")
							{
								camera.verbose_log = text2.item(t).getTextContent().trim();
							}
							if(text2.item(t).getNodeName() == "status-log")
							{
								camera.status_log = text2.item(t).getTextContent().trim();
							}
							else
								continue;
						}
						proxies.add(camera);		
					}
				}
			else
				return;
			gui.pers_true.setEnabled(false);
			gui.exit_fail.setEnabled(false);
			gui.ports.setEnabled(false);
			gui.ips.setEnabled(false);
			gui.max_connections.setEnabled(false);
			gui.sourceip.setEnabled(false);
			gui.sourceport.setEnabled(false);
			gui.sourceid.setEnabled(false);
			gui.sourcewidth.setEnabled(false);
			gui.sourceheight.setEnabled(false);
			gui.verb_log.setEnabled(false);
			gui.stat_log.setEnabled(false);
			gui.scalefactor.setEnabled(false);
			gui.sourcefps.setEnabled(false);
			gui.sourceprotocol.setEnabled(false);
			gui.sourcetype.setEnabled(false);
			gui.bottype.setEnabled(false);
		}
			
		if(gui.downloaded == false)
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
						gui.port_nums.add(camera.port);
						gui.proxyListModel.addElement(camera.port);	
					}
					if(text2.item(t).getNodeName() == "source-type")
					{
						camera.source_type = text2.item(t).getTextContent().trim();
					}
					if(text2.item(t).getNodeName() == "source-protocol")
					{
						camera.source_protocol = text2.item(t).getTextContent().trim();
					}
					if(text2.item(t).getNodeName() == "source-ip")
					{
						camera.source_ip = text2.item(t).getTextContent().trim();
						//gui.proxyListModel.addElement(camera.source_ip);				
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
					if(text2.item(t).getNodeName() == "scale-factor")
					{
						camera.scale_factor = text2.item(t).getTextContent().trim();
					}
					if(text2.item(t).getNodeName() == "max-connections")
					{
						camera.max_connections = text2.item(t).getTextContent().trim();
					}
					if(text2.item(t).getNodeName() == "source-port")
					{
						camera.source_port = text2.item(t).getTextContent().trim();
					}
					if(text2.item(t).getNodeName() == "source-id")
					{
						camera.source_id = text2.item(t).getTextContent().trim();
					}
					if(text2.item(t).getNodeName() == "source-width")
					{
						camera.source_width = text2.item(t).getTextContent().trim();
					}
					if(text2.item(t).getNodeName() == "source-height")
					{
						camera.source_height = text2.item(t).getTextContent().trim();
					}
					if(text2.item(t).getNodeName() == "robotic-type")
					{
						camera.bot_type = text2.item(t).getTextContent().trim();
					}
					if(text2.item(t).getNodeName() == "robotic-protocol")
					{
						camera.bot_protocol = text2.item(t).getTextContent().trim();
					}
					if(text2.item(t).getNodeName() == "robotic-ip")
					{
						camera.bot_ip = text2.item(t).getTextContent().trim();
					}
					if(text2.item(t).getNodeName() == "robotic-port")
					{
						camera.bot_port = text2.item(t).getTextContent().trim();
					}
					if(text2.item(t).getNodeName() == "robotic-id")
					{
						camera.bot_id = text2.item(t).getTextContent().trim();
					}
					if(text2.item(t).getNodeName() == "verbose-log")
					{
						camera.verbose_log = text2.item(t).getTextContent().trim();
					}
					if(text2.item(t).getNodeName() == "status-log")
					{
						camera.status_log = text2.item(t).getTextContent().trim();
					}
					else
						continue;
				}
				proxies.add(camera);		
			}
			Collections.sort(gui.port_nums);
			gui.downloaded = true;
			delete();
		}
	}
		
	//deletes files that are meant to only be temporary, have to figure out a way as well to call this when (X) is clicked
	/**
	 * This is the method that is called when a temporary file needs to be deleted 
	 */
	public void delete()
	{
		//have to make the string into a file to allow the file to be deleted since you cant not delete a string
		//made it to delete both files, since if you try to use the bool may run into a problem at some time and leave file on the system
		//also delete on exit is the back up just incase it was not deleted for some reason or the program crashes during download 
		localFile = "~proxies.xml";
	    File trash = new File(localFile);
		trash.delete();
		trash.deleteOnExit();
		localFile = "~portal.xml";
		trash = new File(localFile);
		trash.delete();
		trash.deleteOnExit();
	}
	
	//this method is to save an xml file locally, having some trouble with this as of now when it goes to the other class
	/**
	 * This is the code that will save the file locally on the file system if the user chooses to do so
	 * @see #fc
	 */
	void saveLocal()
	{
	    fc = new JFileChooser ();
        fc.showSaveDialog(saveButton);
        localsave = fc.getSelectedFile();
        if(localsave == null)
        	return;
        //use absolute path to get the whole path + filename to save file in specified location
		String filename = localsave.getAbsolutePath() + ".xml";
        localsave = new File(filename);
        String fname = filename;
        try
        {
             FileOutputStream fileOut = new FileOutputStream(fname);
             PrintStream oos = new PrintStream (fileOut);
             oos.println("<proxies>");
             for(int i = 0; i < proxies.size();i++)
            	 proxies.get(i).print(oos);
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
	/**
	 * This is the method where the local file decided by the user to upload, is uploaded 
	 * @see #fc
	 * @throws JSchException, SftpException, IOException
	 */
	void uploadLocal(String path) throws JSchException, SftpException, IOException
	{
		fc = new JFileChooser();
        fc.showOpenDialog(openButton);
        fc.setFileSelectionMode(1);
        uploadFile = fc.getSelectedFile();
        if(uploadFile == null)
        	return;
        String filename = uploadFile.getName();
        remoteDirectory = sys_path;
		this.login.channel = (ChannelSftp)login.session.openChannel("sftp"); 
		login.channel.connect(); 
		remoteDirectory = remoteDirectory + path;
		System.out.println(remoteDirectory);
		login.channel.cd(remoteDirectory); 
		FileInputStream fd = new FileInputStream(uploadFile);
		login.channel.put(fd,filename); 
        System.out.println(filename);
        fd.close();
	}
	
	//the code to upload a file to a server that is not saved locally 
	/**
	 * This is the code that will temporaliry save a proxy file, and then upload that file to the server. Then upon completiton that file will be deleted 
	 * @see #delete()
	 * @throws JSchException, SftpException, IOException
	 */
	void uploadBack(String path, String file) throws JSchException, SftpException, IOException
	{
		String localFile = "~proxies.xml";
		//localsave = new File(localFile);
        String fname = localFile;
        try
        {
             FileOutputStream fileOut = new FileOutputStream(fname);
             PrintStream oos = new PrintStream (fileOut);
             oos.println("<proxies>");
             for(int i = 0; i < proxies.size();i++)
            	 proxies.get(i).print(oos);
             oos.println("</proxies>");
             oos.close();
             fileOut.close();
         } catch (FileNotFoundException e) { 
             System.out.println(e);   
         } catch (IOException e) { 
        	 StringWriter sw = new StringWriter();
        	 PrintWriter pw = new PrintWriter(sw);
        	 e.printStackTrace(pw);
        	 System.out.println("Error = " + sw.toString());
        	 }
		remoteDirectory = sys_path;
		this.login.channel = (ChannelSftp)login.session.openChannel("sftp"); 
		login.channel.connect(); 
		remoteDirectory = remoteDirectory + path;
		login.channel.cd(remoteDirectory); 
		FileInputStream fd = new FileInputStream(localFile);
		login.channel.put(fd,file); 
		fd.close();
		delete();
		} 
	
	//the code to upload a file to a server that is not saved locally 
	/**
	 * This is the code that will temporaliry save a portal file, and then upload that file to the server. Then upon completiton that file will be deleted 
	 * @see #delete()
	 * @throws JSchException, SftpException, IOException
	 */
	void uploadBackPortal(String path, String file) throws JSchException, SftpException, IOException
	{
		String localFile = "~portal.xml";
		//localsave = new File(localFile);
        String fname = localFile;
        try
        {
        	 FileOutputStream fileOut = new FileOutputStream(fname);
             PrintStream oos = new PrintStream (fileOut);
             oos.println("<portal>");
             oos.println();
    		 oos.println("\t<site-name>" + gui.sitename.getText() + "</site-name>"); 
    		 oos.println("\t<site-ip>" + gui.siteip.getText() + "</site-ip>");
    		 oos.println("\t<site-admin-email>" + gui.siteadmin.getText() + "</site-admin-email>");
    		 oos.println("\t<dvr>" + "</dvr>"); 
             for(int i = 0; i < proxies.size();i++)
             {
            	 if(proxies.get(i).useInPortal && !proxies.get(i).id.isEmpty())
            	 {
            		 proxies.get(i).printPortalproxy(oos);
            	 }
             }
             oos.println("<group>");
             oos.println("\t<id>" + gui.groupname.getText() + "</id>");
             oos.println("<feed>");
             oos.println("\t<id>" + gui.feedname.getText() + "</id>");
             for(int i = 0; i < proxies.size();i++)
             {
            	 if(proxies.get(i).useInPortal && !proxies.get(i).id.isEmpty())
            	 {
            		 proxies.get(i).printPortalstream(oos);
            	 }
             }
             oos.println("</feed>");
             oos.println("<sections>");
             oos.println("\t<id>local_video</id>");
             oos.println("\t<id>embedded_local_video</id>");
             oos.println("</sections>");
             oos.println("<users>");
             oos.println("\t<id>*</id>");
             oos.println("</users>");
             oos.println("</group>");
             oos.println("</portal>");
             oos.close();
         } catch (FileNotFoundException e) { 
             System.out.println(e);   
         } catch (IOException e) { 
        	 StringWriter sw = new StringWriter();
        	 PrintWriter pw = new PrintWriter(sw);
        	 e.printStackTrace(pw);
        	 System.out.println("Error = " + sw.toString());
        	 }
		remoteDirectory = sys_path;
		this.login.channel = (ChannelSftp)login.session.openChannel("sftp"); 
		login.channel.connect(); 
		remoteDirectory = remoteDirectory + path;
		login.channel.cd(remoteDirectory); 
		FileInputStream fd = new FileInputStream(localFile);
		System.out.println("Dir: " + remoteDirectory);
		System.out.println("File: " + file);
		login.channel.put(fd,file); 
		fd.close();
		delete();
		} 
	
	//this is the method that will be used to save a portal xml file locally
	/**
	 * This is the code that will save the portal file locally on the file system if the user chooses to do so
	 * @see #fc
	 */
	void saveLocalportal()
	{
		fc = new JFileChooser ();
        fc.showSaveDialog(saveButton);
        localsave = fc.getSelectedFile();
        if(localsave == null)
        	return;
        //use absolute path to get the whole path + filename to save file in specified location
		String filename = localsave.getAbsolutePath() + ".xml";
        localsave = new File(filename);
        String fname = filename;
        try
        {
             FileOutputStream fileOut = new FileOutputStream(fname);
             PrintStream oos = new PrintStream (fileOut);
             oos.println("<portal>");
             oos.println();
    		 oos.println("\t<site-name>" + gui.sitename.getText() + "</site-name>"); 
    		 oos.println("\t<site-ip>" + gui.siteip.getText() + "</site-ip>");
    		 oos.println("\t<site-admin-email>" + gui.siteadmin.getText() + "</site-admin-email>");
    		 oos.println("\t<dvr>" + "</dvr>"); 
             for(int i = 0; i < proxies.size();i++)
             {
            	 if(proxies.get(i).useInPortal && !proxies.get(i).id.isEmpty())
            	 {
            		 proxies.get(i).printPortalproxy(oos);
            	 }
             }
             oos.println("<group>");
             oos.println("\t<id>" + gui.groupname.getText() + "</id>");
             oos.println("<feed>");
             oos.println("\t<id>" + gui.feedname.getText() + "</id>");
             for(int i = 0; i < proxies.size();i++)
             {
            	 if(proxies.get(i).useInPortal && !proxies.get(i).id.isEmpty())
            	 {
            		 proxies.get(i).printPortalstream(oos);
            	 }
             }
             oos.println("</feed>");
             oos.println("<sections>");
             oos.println("\t<id>local_video</id>");
             oos.println("\t<id>embedded_local_video</id>");
             oos.println("</sections>");
             oos.println("<users>");
             oos.println("\t<id>*</id>");
             oos.println("</users>");
             oos.println("</group>");
             oos.println("</portal>");
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
}

//class that will be used to edit and write back the xml file to a file
/**
 * Class used to print the files into the xml format and save to a file 
 */
class FlexTPSCamera
{
	 public String ip = "";
	 public String port = "";
	 public String source_type = "";
	 public String source_protocol = "";
	 public String source_ip = "";
	 public String source_fps = "";
	 public String persis = "false";
	 public String failure = "false";
	 public String max_connections = "";
	 public String source_id = "";
	 public String scale_factor = "";
	 public String source_port = "";
	 public String source_width = "";
	 public String source_height = "";
	 public String bot_type = "";
	 public String bot_protocol = "";
	 public String bot_ip = "";
	 public String bot_port ="";
	 public String bot_id = "";
	 public String verbose_log = "false";
	 public String status_log = "false";
	 public String id = "";
	 public boolean useInPortal = false;
	 public ProxyGUI gui;
	 
	 // Various Constructors
	 /**
	  * Constructor 
	  */
	 public FlexTPSCamera() {}
	// public FlexTPSCamera(String ip) {this.ip = ip;}
	 
	 //is supposed to print those all to a file 
	 /**
	  * This is the method that is used to print the proxy file xml
	  * @param ps this is used to pass in the information from the FlexTPSProxy that needs to be included into the file
	  */
	 public void print(PrintStream ps) 
	 {
		 ps.println();
		 ps.println("\t<proxy>");
		 if(!ip.isEmpty())
			 ps.println("\t\t <ip>" + ip + "</ip>");
		 if(!port.isEmpty())
			 ps.println("\t\t <port>" + port + "</port>");
		 if(!source_type.isEmpty())
			 ps.println("\t\t <source-type>" + source_type + "</source-type>");
		 if(!source_protocol.isEmpty())
			 ps.println("\t\t <source-protocol>" + source_protocol + "</source-protocol>");
		 if(!source_ip.isEmpty())
			 ps.println("\t\t <source-ip>" + source_ip + "</source-ip>");
		 if(!source_fps.isEmpty())
			 ps.println("\t\t <source-fps>" + source_fps + "</source-fps>");
		 if(!persis.isEmpty())
			 ps.println("\t\t <persistent>" + persis + "</persistent>");
		 if(!failure.isEmpty())
			 ps.println("\t\t <exit-on-failure>" + failure + "</exit-on-failure>");
		 if(!max_connections.isEmpty())
			 ps.println("\t\t <max-connections>" + max_connections + "</max-connections>");
		 if(!source_id.isEmpty())
			 ps.println("\t\t <source-id>" + source_id + "</source-id>");
		 if(!scale_factor.isEmpty())
			 ps.println("\t\t <scale-factor>" + scale_factor + "</scale-factor>");
		 if(!source_port.isEmpty())
			 ps.println("\t\t <source-port>" + source_port + "</source-port>");
		 if(!source_width.isEmpty())
			 ps.println("\t\t <source-width>" + source_width + "</source-width>");
		 if(!source_height.isEmpty())
			 ps.println("\t\t <source-height>" + source_height + "</source-height>");
		 if(!bot_type.isEmpty())
			 ps.println("\t\t <robotic-type>" + bot_type + "</robotic-type>");
		 if(!bot_protocol.isEmpty())
			 ps.println("\t\t <robotic-protocol>" + bot_protocol + "</robotic-protocol>");
		 if(!bot_ip.isEmpty())
			 ps.println("\t\t <robotic-ip>" + bot_ip + "</robotic-ip>");
		 if(!bot_port.isEmpty())
			 ps.println("\t\t <robotic-port>" + bot_port + "</robotic-port>");
		 if(!bot_id.isEmpty())
			 ps.println("\t\t <robotic-id>" + bot_id + "</robotic-id>");
		 if(!verbose_log.isEmpty())
			 ps.println("\t\t <verbose-log>" + verbose_log + "</verbose-log>");
		 if(!status_log.isEmpty())
			 ps.println("\t\t <status-log>" + status_log + "</status-log>");
		 ps.println("\t</proxy>");
		 ps.println();
	 }
	 
	 //method to print the proxy information in the portal xml file 
	 /**
	  * This is the method that is used to print the proxy information needed into the portal file 
	  * @param ps this is used to pass in the information from the FlexTPSProxy that needs to be included into the file
	  */
	 public void printPortalproxy(PrintStream ps) 
	 {
		 ps.println();
		 ps.println("\t<proxy>");
		 if(!id.isEmpty())
			 ps.println("\t\t <id>" + id + "</id>");
		 if(!ip.isEmpty())
			 ps.println("\t\t <ip>" + ip + "</ip>");
		 if(!port.isEmpty())
			 ps.println("\t\t <port>" + port + "</port>");
		 ps.println("\t</proxy>");
		 ps.println();
	 }
	 
	 //method to print the stream information for the proxy xml file 
	 /**
	  * This is the method used to print the portal file in the correct format
	  * @param ps is used to get the information needed from the FlexTPSProxy class so that the portal file is correct 
	  */
	 public void printPortalstream(PrintStream ps) 
	 {
		 ps.println();
		 ps.println("\t<stream>");
		 if(!id.isEmpty())
			 ps.println("\t\t <id>" + id + "</id>");
		 if(!max_connections.isEmpty())
			 ps.println("\t\t <max-connection-length>" + max_connections + "</max-connection-length>");			
		 if(!source_fps.isEmpty())
			 ps.println("\t\t <max-fps>" + source_fps + "</max-fps>");			
		 if(!id.isEmpty())
			 ps.println("\t\t <proxy-id>" + id + "</proxy-id>");	
		 if(!source_fps.isEmpty())
			 ps.println("\t\t <initial-fps>" + source_fps + "</initial-fps>");			
		 ps.println("\t\t <stream-enabled>true</stream-enabled>");
		 ps.println("\t\t <video-box-size>medium</video-box-size>");
		 if(!bot_type.isEmpty())
		 {
			 ps.println("\t\t <robotic-enabled>true</robotic-enabled>");
			 ps.println("\t\t <robotic-controls>");
			 ps.println("\t\t\t <zoom>true</zoom>");
			 ps.println("\t\t\t <tilt>false</tilt>");
			 ps.println("\t\t\t <iris>false</iris>");
			 ps.println("\t\t\t <focus>false</focus>");
			 ps.println("\t\t\t <pan>true</pan>");
			 ps.println("\t\t </robotic-controls>");
		 }
			 ps.println("\t</stream>");
			 ps.println();
	 }
}