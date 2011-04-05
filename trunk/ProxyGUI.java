					/************************************	
					 * FlexTPS proxy editor 			*
					 * Jason Ricles						*
					 * Last Edit: 3/23/11	            *
					 ************************************/
//still need to implement some of the buttons, such as save to server, and upload local, as well as change max connections listener
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
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
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import javax.swing.JList;

//class that sets up the gui for the proxy xml editor 
public class ProxyGUI extends JFrame 
{
	private static final long serialVersionUID = 1L;
	private JPanel main_window = null;
	protected JComboBox cameraip = null;
	protected JList cameralist = null;
	private JButton save = null;
	private JButton delete = null;
	private JButton saveserver = null;
	private JButton download = null;
	private JButton uploadlocal = null;
	private JButton quit = null;
	private JButton add = null;
	private JMenuBar menuBar = null;
	private JMenu server = null;
	private JMenu file = null;
	private JMenuItem connect = null;
	private JMenuItem disconnect = null;
	private JMenuItem saveas = null;
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

	public ProxyGUI() 
	{
		super();
		//call to intilize the content pane
		initialize();
		proxy.gui = this;
	}
	
	void initialize() 
	 {
		this.setSize(740,375);
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
			main_window.add(getJButton1(), null);
			main_window.add(getJButton2(), null);
			main_window.add(getJButton3(), null);
			main_window.add(getJButton4(), null);
			main_window.add(getJButton5(), null);
			main_window.add(getJButton6(), null);
			main_window.add(getCheckBox(), null);
			main_window.add(getCheckBox2(), null);
			main_window.add(getCheckBox3(), null);
			main_window.add(getCheckBox4(), null);
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
	private JCheckBox getCheckBox()
	{
		pers_true = new JCheckBox("true");
		pers_true.setBounds(new Rectangle(220, 25, 51, 21));	
		pers_true.setEnabled(false);
		main_window.repaint();
		return pers_true;
	}
	
	//label for exit on failure
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
	private JCheckBox getCheckBox2()
	{
		exit_fail = new JCheckBox("true");
		exit_fail.setBounds(new Rectangle(363, 25, 51, 21));	
		exit_fail.setEnabled(false);
		main_window.repaint();
		return exit_fail;
	}
	
	//verbose-log label
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
	private JCheckBox getCheckBox3()
	{
		verb_log = new JCheckBox("true");
		verb_log.setBounds(new Rectangle(493, 25, 51, 21));	
		verb_log.setEnabled(false);
		main_window.repaint();
		return verb_log;
	}
	
	//status-log label
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
	private JCheckBox getCheckBox4()
	{
		stat_log = new JCheckBox("true");
		stat_log.setBounds(new Rectangle(612, 25, 51, 21));	
		stat_log.setEnabled(false);
		main_window.repaint();
		return stat_log;
	}
	
	//label for port
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
	private JTextField getTextField1()
	{
		ips = new JTextField();
		ips.setBounds(new Rectangle(270, 50, 80, 21));
		ips.setEnabled(false);
		ips.setText(default_ip);
		return ips;
	}
	
	//lable for max connections
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
	private JTextField getTextField2()
	{
		max_connections = new JTextField();
		max_connections.setBounds(new Rectangle(480, 50, 40, 21));
		max_connections.setEnabled(false);
		max_connections.setText(default_maxconnect);
		return max_connections;
	}
	
	//label for sourceip
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
	private JTextField getTextField3()
	{
		sourceip = new JTextField();
		sourceip.setBounds(new Rectangle(215, 80, 90, 21));
		sourceip.setEnabled(false);
		sourceip.setText(default_sourceip);
		return sourceip;
	}
	
	//label for sourceport
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
	private JTextField getTextField4()
	{
		sourceport = new JTextField();
		sourceport.setBounds(new Rectangle(400, 80, 90, 21));
		sourceport.setEnabled(false);
		sourceport.setText(default_sourceport);
		return sourceport;
	}
	
	//label for sourceid
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
	private JTextField getTextField5()
	{
		sourceid = new JTextField();
		sourceid.setBounds(new Rectangle(215, 115, 90, 21));
		sourceid.setEnabled(false);
		sourceid.setText(default_sourceid);
		return sourceid;
	}
	
	//label for sourcewidth
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
	private JTextField getTextField6()
	{
		sourcewidth = new JTextField();
		sourcewidth.setBounds(new Rectangle(415, 115, 90, 21));
		sourcewidth.setEnabled(false);
		sourcewidth.setText(default_sourcewidth);
		return sourcewidth;
	}
	
	//label for sourceheight
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
	private JTextField getTextField7()
	{
		sourceheight = new JTextField();
		sourceheight.setBounds(new Rectangle(240, 150, 90, 21));
		sourceheight.setEnabled(false);
		sourceheight.setText(default_sourceheight);
		return sourceheight;
	}
	
	//label for robot ip
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
	private JTextField getTextField8()
	{
		botip = new JTextField();
		botip.setBounds(new Rectangle(410, 150, 90, 21));
		botip.setEnabled(false);
		botip.setText(default_botip);
		return botip;
	}
	
	//label for robot port
	private JLabel getJLabel11() 
	{
		if (botport_label == null) {
			botport_label = new JLabel();
			botport_label.setText("Robotic-port:");
			botport_label.setBounds(new Rectangle(150, 185, 284, 16));
		}
		return botport_label;
	}
	
	//tetx field for robot port
	private JTextField getTextField9()
	{
		botport = new JTextField();
		botport.setBounds(new Rectangle(230, 185, 90, 21));
		botport.setEnabled(false);
		botport.setText(default_botport);
		return botport;
	}
	
	//label for robot id
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
	private JTextField getTextField10()
	{
		botid = new JTextField();
		botid.setBounds(new Rectangle(410, 185, 90, 21));
		botid.setEnabled(false);
		botid.setText(default_botid);
		return botid;
	}
	
	//make button to save to server, trouble "repainting" after clearing of jlist
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
					if(pers_true.isSelected())
						proxy.proxies.get(selected).persis = "true";
					else
						proxy.proxies.get(selected).persis = "false";
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
					if(!sourceport.getText().isEmpty())
						proxy.proxies.get(selected).source_port = sourceport.getText();
					if(!sourceid.getText().isEmpty())
						proxy.proxies.get(selected).source_id = sourceid.getText();
					if(!sourcewidth.getText().isEmpty())
						proxy.proxies.get(selected).source_width = sourcewidth.getText();
					if(!sourceheight.getText().isEmpty())
						proxy.proxies.get(selected).source_height = sourceheight.getText();
					proxy.proxies.get(selected).scale_factor = (String) scalefactor.getSelectedItem();
					proxy.proxies.get(selected).source_fps = (String) sourcefps.getSelectedItem();
					proxy.proxies.get(selected).source_protocol = (String) sourceprotocol.getSelectedItem();
					proxy.proxies.get(selected).source_type = (String) sourcetype.getSelectedItem();
					proxy.proxies.get(selected).bot_type = (String) bottype.getSelectedItem();
					if(bottype.getSelectedIndex() != 0)
					{
						proxy.proxies.get(selected).bot_protocol = (String) botprotocol.getSelectedItem();
						if(!botid.getText().isEmpty())
							proxy.proxies.get(selected).bot_id = botid.getText();
						if(!botip.getText().isEmpty())
							proxy.proxies.get(selected).bot_ip = botip.getText();
						if(!botport.getText().isEmpty())
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
					    String sys_path = "/home/jgr208";
						proxy.uploadBack("/proxies/etc","proxies.xml");
					}
						catch(Exception e1)
						 {
							failed = new JOptionPane();
							failed.setSize(new Dimension(119, 69));
							JOptionPane.showMessageDialog(failed, "Failed to upload file " + "file");
							System.out.println(e1);
						 }
			}
				}
			});
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
	
	//button to add to xml file
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
	
	//prints out message if combobox is already filled
	void already()
	{
		//already_downloaded = new JOptionPane();
		//already_downloaded.setSize(new Dimension(119, 69));
		//JOptionPane.showMessageDialog(already_downloaded, "Already downloaded file");
		//code below fails when redownloading xml file
	    proxyListModel.clear();
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
	public ProxyGUI gui;
	protected String path;

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
	public void delete()
	{
		//have to make the string into a file to allow the file to be deleted since you cant not delete a string
		localFile = "~proxies.xml";
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
	void uploadLocal()
	{
		fc = new JFileChooser();
        fc.showOpenDialog(openButton);
        fc.setFileSelectionMode(1);
        uploadFile = fc.getSelectedFile();
        System.out.println(uploadFile);
	}
	
	//the code to upload a file to a server that is not saved locally 
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
	 
	 // Various Constructors
	 public FlexTPSCamera() {}
	// public FlexTPSCamera(String ip) {this.ip = ip;}
	 
	 //is supposed to print those all to a file 
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
}
