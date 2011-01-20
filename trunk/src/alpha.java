/*
 * FlexTPS
 * Made by Jason Ricles
 * last revision 1/20/11
 * known problems: none
 */

import com.jcraft.jsch.*;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;
//import javax.swing.JDialog;
//import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JOptionPane;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
public class alpha extends JFrame
{
	//public static void main(String[] args)
	//{
		//alpha a = new alpha();
		//a.initialize();
	//}
	
	//global variables for class
	private static final long serialVersionUID = 1L;
	private JPanel main_window = null;
	private JButton connect = null;
	private JTextField user_field = null;
	private JPasswordField pass = null;
	private JTextPane jTextPane = null;
	private String host = null;  
	private String user = null;
	private JSch jsch = new JSch();  
	private Session session = null;
	private String password = null;
	private JLabel user_label = null;
	private JOptionPane prompt = null; 
	private JOptionPane connected = null;
	private JButton upload = null;
	private JButton download = null;
	private String filename = null;
	private JTextField file = null;
	private JButton quit = null;
	private ChannelSftp channel = null;
	private JOptionPane failed = null;
	private JOptionPane confirm = null;
	public int yes = 0;
	
	//starts the intilization of the login gui
	public alpha() 
	{
		super();
		initialize();
	}
	
	//start of program code
	
	//this is the actual code that logs the user into shh when connect it pressed on the gui, and prints out any errors
	void login()
	 {
		 try
		 {
		 user = host.substring(0, host.indexOf('@'));
		 host = host.substring(host.indexOf('@')+1);
		 this.session = jsch.getSession(user, host, 22);
		 session.setPassword(password);
		 session.setConfig("StrictHostKeyChecking", "no");
		 session.connect();
		 getJOptionPane1();
		 }
		 catch(Exception e)
		 {
			 getJOptionPane2();
		 }
	 }
	
	//this is the code that uploads the file when upload is pressed on the gui, and prints out any errors
	void upload(String password,String remoteDirectory) 
	throws JSchException, SftpException, FileNotFoundException 
	{ 
	remoteDirectory = "/home/jgr208/test";
	this.channel = (ChannelSftp)session.openChannel("sftp"); 
	channel.connect(); 
	channel.cd(remoteDirectory); 
	String localFile = "c:\\users\\jason\\desktop\\" + filename;
	channel.put(new FileInputStream(localFile), filename); 
	} 
	
	//this is the code that downloads the file when download is pressed, and pops up any errors messages
	void download(String password,String remoteDirectory) 
	throws JSchException, SftpException, FileNotFoundException 
	{ 
		remoteDirectory = "/home/jgr208/test";
		this.channel = (ChannelSftp)session.openChannel("sftp"); 
		channel.connect(); 
		channel.cd(remoteDirectory); 
		String localFile = "c:\\users\\jason\\desktop\\";
		//String localFile = System.getProperty("user.dir");
		channel.get((filename), localFile); 
	}
	
	//this is what logs the user out when they press dissconnect on the gui
	void logout()
	{
		try
		 {
		Channel channel2 = session.openChannel("exec");
		((ChannelExec)channel2).setCommand("logout");
		channel2.setInputStream(System.in);
		channel2.setOutputStream(System.out);
		channel2.connect();
		System.exit(1);
		 }
		catch(Exception e)
		 {
			System.exit(1);
		 }
	}
	
	//start of gui code
	
	//this intilizes the window 
	void initialize() 
	 {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("FlexTPS");
	}
	
	//this intilizes the buttons and text inside the window 
	private JPanel getJContentPane() 
	{
		if (main_window == null) 
		{
			user_label = new JLabel();
			user_label.setBounds(new Rectangle(8, 5, 111, 20));
			user_label.setText("Username@server");
			main_window = new JPanel();
			main_window.setLayout(null);
			main_window.add(getJButton(), null);
			main_window.add(getJTextField(), null);
			main_window.add(getJTextPane(), null);
			main_window.add(user_label, null);
		}
		return main_window;
	}
	
	//the connect button, also hides the window when main window when pressed
	private JButton getJButton() 
	{
		if (connect == null) 
		{
			connect = new JButton();
			connect.setBounds(new Rectangle(183, 128, 93, 25));
			connect.setText("connect");
			connect.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
					setVisible(false);
					getJOptionPane();
				}
			});
		}
		return connect;
	}
	
	//the username@server field
	private JTextField getJTextField() 
	{
		if (user_field == null) 
		{
			user_field = new JTextField();
			user_field.setBounds(new Rectangle(7, 31, 220, 19));
			
			user_field.addKeyListener(new java.awt.event.KeyAdapter() 
			{
				public void keyReleased(java.awt.event.KeyEvent e) 
				{
					host = user_field.getText(); 
				}
			});
		}
		return user_field;
	}
	
	private JTextPane getJTextPane() 
	{
		if (jTextPane == null) 
		{
			jTextPane = new JTextPane();
			jTextPane.setBounds(new Rectangle(9, 8, 56, 1));
		}
		return jTextPane;
	}
	
	//this pops up after connect is pressed for the user to enter the password
	@SuppressWarnings("deprecation")
	private JOptionPane getJOptionPane() 
	{
		if (prompt == null) 
		{
			prompt = new JOptionPane();
			pass = new JPasswordField();
			prompt.setSize(new Dimension(119, 69));
			pass = new JPasswordField();
			pass.setEchoChar('*');
			JOptionPane.showConfirmDialog(null, pass,
					  "Please enter your password", JOptionPane.OK_CANCEL_OPTION);
			password = pass.getText();
		}
		login();
		return prompt;
	}

	//this pops up after connection to let the user know they have connected
	private JOptionPane getJOptionPane1() 
	{
		if (connected == null) 
		{
			connected = new JOptionPane();
			connected.setSize(new Dimension(119, 69));
			JOptionPane.showMessageDialog(connected, "You are now connected to " + host);
		}
		getJContentPane1();
		return connected;
	}
	
	//this is the new content pane that appears after connection and is repainted to show buttons to upload,download, and dissconnect
	private JPanel getJContentPane1()
	{
		main_window.remove(getJTextPane());
		main_window.remove(getJButton());
		main_window.remove(getJTextField());
		main_window.remove(user_label);
		this.setSize(400, 200);
		main_window.add(getJButton1(), null);
		main_window.add(getJButton2(), null);
		main_window.add(getJTextField1(), null);
		main_window.add(getJButton3(),null);
		user_label.setText("Enter the file name");
		main_window.add(user_label,null);
		repaint();
		setVisible(true);
		return main_window;
	}
	
	//button to upload file that will then confirm the user wants to upload
	private JButton getJButton1() 
	{
		if (upload == null) 
		{
			upload = new JButton();
			upload.setBounds(new Rectangle(50, 100, 93, 25));
			upload.setText("upload");
			upload.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
							confirm = new JOptionPane();
							confirm.setSize(new Dimension(119, 69));
							yes = JOptionPane.showConfirmDialog(confirm,"Upload " + filename +"?","Upload",JOptionPane.YES_NO_OPTION);
						if(yes == JOptionPane.YES_OPTION)
						{
						try
					{
						upload(null,null);
					}
						catch(Exception e1)
						 {
							failed = new JOptionPane();
							failed.setSize(new Dimension(119, 69));
							JOptionPane.showMessageDialog(failed, "Failed to upload file " + filename);
						 }
			}
				}
			});
		}
		return upload;
	}
	
	//button to download file
	private JButton getJButton2() 
	{
		if (download == null) 
		{
			download = new JButton();
			download.setBounds(new Rectangle(150, 100, 93, 25));
			download.setText("download");
			download.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
					try {
						download(null, null);
					} 
					catch(Exception e1)
					 {
						failed = new JOptionPane();
						failed.setSize(new Dimension(119, 69));
						JOptionPane.showMessageDialog(failed, "Failed to download file " + filename);
					 }
			}
			});
		}
		return download;
	}

//this is where the filename to be used is entered 
private JTextField getJTextField1() 
{
	if (file == null) 
	{
		file = new JTextField();
		file.setBounds(new Rectangle(7, 31, 220, 19));
		
		file.addKeyListener(new java.awt.event.KeyAdapter() 
		{
			public void keyReleased(java.awt.event.KeyEvent e) 
			{
				filename = file.getText(); 
			}
		});
	}
	return file;
	}

//this button is used to dissconnect the user
private JButton getJButton3() 
{
		if (quit == null) 
		{
			quit = new JButton();
			quit.setBounds(new Rectangle(250, 100, 108, 25));
			quit.setText("dissconnect");
			quit.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
					logout();
				}
			});
		}
		return quit;
	}

//this is what pops up if connection has failed 
private JOptionPane getJOptionPane2()
{
	if (failed == null) 
	{
		failed = new JOptionPane();
		failed.setSize(new Dimension(119, 69));
		JOptionPane.showMessageDialog(failed, "Failed to connect to " + host);
	}
	return failed;
	}
}