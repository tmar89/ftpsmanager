/*
 * FlexTPS
 * Made by Jason Ricles
 * last revision 1/28/11
 * known problems: None
 */

import com.jcraft.jsch.*;

import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JDialog;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JOptionPane;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
@SuppressWarnings("unused")
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
	private JButton start_prox = null;
	private JButton stop_prox = null;
	private JButton start_portal = null;
	private JButton stop_portal = null;
	private JTextField path = null;
	private String sys_path = null;
	private String pwd = null;
	private JLabel pwd_label = null;
	private JButton change_path = null;
	private String remoteDirectory;
	private JButton downloadp = null;
	private JButton uploadp = null;
	Channel channel3 = null;
	Channel channel4 = null;
	
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
		 session = jsch.getSession(user, host, 22);
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
	void upload(String path, String file) 
	throws JSchException, SftpException, FileNotFoundException 
	{ 
	//remoteDirectory = "/home/jgr208/test";
	remoteDirectory = sys_path;
	this.channel = (ChannelSftp)session.openChannel("sftp"); 
	channel.connect(); 
	remoteDirectory = remoteDirectory + path;
	System.out.println(remoteDirectory);
	channel.cd(remoteDirectory); 
	String localFile = "c:\\users\\jason\\desktop\\" + file;
	channel.put(new FileInputStream(localFile), file); 
	} 
	
	//this is the code that downloads the file when download is pressed, and pops up any errors messages
	void download(String path, String file) 
	throws JSchException, SftpException, FileNotFoundException 
	{ 
		//remoteDirectory = "/home/jgr208/test";
		remoteDirectory = sys_path;
		this.channel = (ChannelSftp)session.openChannel("sftp"); 
		channel.connect(); 
		remoteDirectory = remoteDirectory + path;
		System.out.println(remoteDirectory);
		channel.cd(remoteDirectory); 
		String localFile = "c:\\users\\jason\\desktop\\";
		//String localFile = System.getProperty("user.dir");
		channel.get((file), localFile); 
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
	
	//this is the method to start the proxy, and confirms it has started with a popup
	void start_camera()
	throws JSchException, IOException, InterruptedException 
	{
		channel3 = session.openChannel("shell");
		String command = "sudo /sbin/service flextps_proxies start\n";;
		InputStream input = null;
	      try 
	      {   
	        input = new ByteArrayInputStream(command.getBytes("UTF-8"));    	  
		  } 
	      catch (UnsupportedEncodingException e) {}	
	      channel3.setInputStream(input);
	      channel3.setOutputStream(System.out);      
	      channel3.connect(3*1000);      
	      Thread.sleep(1000);
	      connected = new JOptionPane();
		  connected.setSize(new Dimension(119, 69));
		  JOptionPane.showMessageDialog(connected, "Proxy started");
	}
	
	//this is the method to stop the proxy, and confirms it has stopped with a popup
	void stop_camera()
	throws JSchException, InterruptedException
	{
		channel3 = session.openChannel("shell");
		String command = "sudo /sbin/service flextps_proxies stop\n";;
		InputStream input = null;
	      try 
	      {   
	        input = new ByteArrayInputStream(command.getBytes("UTF-8"));    	  
		  } 
	      catch (UnsupportedEncodingException e) {}	
	      channel3.setInputStream(input);
	      channel3.setOutputStream(System.out);      
	      channel3.connect(3*1000);      
	      Thread.sleep(1000);
	      connected = new JOptionPane();
		  connected.setSize(new Dimension(119, 69));
		  JOptionPane.showMessageDialog(connected, "Proxy stopped");
	}
	
	//this is the method to start the httpd, and confirms it has started with a popup
	void start_portal()
	throws JSchException, IOException, InterruptedException 
	{
		channel4 = session.openChannel("shell");
		String command = "sudo /sbin/service flextps_httpd start\n";
		InputStream input = null;
	      try 
	      {   
	        input = new ByteArrayInputStream(command.getBytes("UTF-8"));    	  
		  } 
	      catch (UnsupportedEncodingException e) {}	
	      channel4.setInputStream(input);
	      channel4.setOutputStream(System.out);      
	      channel4.connect(3*1000);      
	      Thread.sleep(1000);
	      connected = new JOptionPane();
		  connected.setSize(new Dimension(119, 69));
		  JOptionPane.showMessageDialog(connected, "Portal started");
	}
	
	//this is the method to start the httpd, and confirms it has stopped with a popup
	void stop_portal()
	throws JSchException, IOException, InterruptedException 
	{
		channel4 = session.openChannel("shell");
		String command = "sudo /sbin/service flextps_httpd stop\n";
		InputStream input = null;
	      try 
	      {   
	        input = new ByteArrayInputStream(command.getBytes("UTF-8"));    	  
		  } 
	      catch (UnsupportedEncodingException e) {}	
	      channel4.setInputStream(input);
	      channel4.setOutputStream(System.out);      
	      channel4.connect(3*1000);      
	      Thread.sleep(1000);
	      connected = new JOptionPane();
		  connected.setSize(new Dimension(119, 69));
		  JOptionPane.showMessageDialog(connected, "Portal stopped");
	}
	
	//the current path method envokes the sftp channel to use the built in string function
	// with pwd command to get the path you are currently in and 
	//then set that path in the gui to text to show the user
	//where they are
	/* 
	 * Use this for a pop up confirmation not as a label
	 */
	void current_path()
	throws JSchException, IOException, InterruptedException 
	{
		try
		{
		Channel channel5 = (ChannelSftp)session.openChannel("sftp"); 
		channel5.connect();
		pwd = ((ChannelSftp) channel5).pwd();
		}
		catch (Exception e1)
		{
		  System.out.println(e1);
		}
	}
	
	void change_path()
	throws JSchException, SftpException, FileNotFoundException
	{
		try
		{
		//remoteDirectory = "/home/jgr208/test";
		this.channel = (ChannelSftp)session.openChannel("sftp");
		remoteDirectory = sys_path;
		channel.connect(); 
		channel.cd(remoteDirectory); 
		}
		catch (Exception e1)
		{
		 System.out.println(e1);
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
			user_field = new JTextField("jgr208@neespop.nees.lehigh.edu");
			user_field.setBounds(new Rectangle(7, 31, 220, 19));
			host = "jgr208@neespop.nees.lehigh.edu";
			user_field.addKeyListener(new java.awt.event.KeyAdapter() 
			{
				public void keyReleased(java.awt.event.KeyEvent e) 
				{
					host = user_field.getText(); 
					
				}
			});
			//host = "jgr208@neespop.nees.lehigh.edu";
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
		this.setSize(745, 350);
		main_window.add(getJButton1(), null);
		main_window.add(getJButton2(), null);
		main_window.add(getJTextField1(), null);
		main_window.add(getJButton3(),null);
		main_window.add(getJButton4(),null);
		main_window.add(getJButton5(),null);
		main_window.add(getJButton6(),null);
		main_window.add(getJButton7(),null);
		main_window.add(getJButton8(),null);
		main_window.add(getJTextField2(),null);
		main_window.add(getJButton10(),null);
		main_window.add(getJButton11(),null);
		user_label.setText("Enter the file name");
		pwd_label = new JLabel();
		pwd_label.setBounds(new Rectangle(7, 200, 400, 19));
		pwd_label.setText("FlexTPS path");
		main_window.add(user_label,null);
		main_window.add(pwd_label, null);
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
			upload.setBounds(new Rectangle(10, 65, 130, 25));
			upload.setText("upload proxy");
			upload.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
					filename = "proxies.xml";
							confirm = new JOptionPane();
							confirm.setSize(new Dimension(119, 69));
							yes = JOptionPane.showConfirmDialog(confirm,"Upload " + filename +"?","Upload",JOptionPane.YES_NO_OPTION);
						if(yes == JOptionPane.YES_OPTION)
						{
						try
					{
					    sys_path = "/home/jgr208";
						upload("/proxies/etc","proxies.xml");
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
			download.setBounds(new Rectangle(300, 65, 130, 25));
			download.setText("download proxy");
			download.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
					try {
						sys_path = "/home/jgr208";
						download("/proxies/etc","proxies.xml");
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
			quit.setBounds(new Rectangle(600, 65, 108, 25));
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

//start proxy button
private JButton getJButton4() 
{
		if (start_prox == null) 
		{
			start_prox = new JButton();
			start_prox.setBounds(new Rectangle(80, 110, 108, 25));
			start_prox.setText("Start Proxy");
			start_prox.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
					try
					{
					start_camera();
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
						failed = new JOptionPane();
						failed.setSize(new Dimension(119, 69));
						JOptionPane.showMessageDialog(failed, "Failed to start camera");
						//System.out.println(e1);
					}
				}
			});
		}
		return start_prox;
	}

//stop proxy button
private JButton getJButton5() 
{
		if (stop_prox == null) 
		{
			stop_prox = new JButton();
			stop_prox.setBounds(new Rectangle(210, 110, 108, 25));
			stop_prox.setText("Stop Proxy");
			stop_prox.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
					try
					{
					stop_camera();
					}
					catch (Exception e1)
					{
						failed = new JOptionPane();
						failed.setSize(new Dimension(119, 69));
						JOptionPane.showMessageDialog(failed, "Failed to stop camera");
					}
				}
			});
		}
		return stop_prox;
	}

//start httpd button
private JButton getJButton6() 
{
		if (start_portal == null) 
		{
			start_portal = new JButton();
			start_portal.setBounds(new Rectangle(80, 150, 108, 25));
			start_portal.setText("Start Portal");
			start_portal.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
					try
					{
					start_portal();
					}
					catch (Exception e1)
					{
						failed = new JOptionPane();
						failed.setSize(new Dimension(119, 69));
						JOptionPane.showMessageDialog(failed, "Failed to start portal");
					}
				}
			});
		}
		return start_portal;
	}

//stop httpd button
private JButton getJButton7() 
{
		if (stop_portal == null) 
		{
			stop_portal = new JButton();
			stop_portal.setBounds(new Rectangle(210, 150, 108, 25));
			stop_portal.setText("Stop Portal");
			stop_portal.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
					try
					{
					stop_portal();
					}
					catch (Exception e1)
					{
						failed = new JOptionPane();
						failed.setSize(new Dimension(119, 69));
						JOptionPane.showMessageDialog(failed, "Failed to stop portal");
					}
				}
			});
		}
		return stop_portal;
	}

private JTextField getJTextField2() 
{
	if (path == null) 
	{
		path = new JTextField("/opt/flexTPS");
		path.setBounds(new Rectangle(7, 225, 220, 19));
		try
		{
		current_path();
		}
		catch (Exception e1)
		{}
		path.addKeyListener(new java.awt.event.KeyAdapter() 
		{
			public void keyReleased(java.awt.event.KeyEvent e) 
			{
				sys_path = path.getText(); 
			}
		});
	}
	return path;
	}

private JButton getJButton8() 
{
		if (change_path == null) 
		{
			change_path = new JButton();
			change_path.setBounds(new Rectangle(250, 220, 108, 25));
			change_path.setText("Change Path");
			change_path.addMouseListener(new java.awt.event.MouseAdapter() 
			{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
					try
					{
					//sys_path = path.getText();
					change_path();
					}
					catch (Exception e1)
					{
						failed = new JOptionPane();
						failed.setSize(new Dimension(119, 69));
						JOptionPane.showMessageDialog(failed, "Failed to change system path");
					}
				}
			});
		}
		return change_path;
	}

private JButton getJButton10() 
{
	if (downloadp == null) 
	{
		downloadp = new JButton();
		downloadp.setBounds(new Rectangle(450, 65, 130, 25));
		downloadp.setText("download portal");
		downloadp.addMouseListener(new java.awt.event.MouseAdapter() 
		{
			public void mouseClicked(java.awt.event.MouseEvent e) 
			{
				try {
					sys_path = "/home/jgr208";
					download("/portal/etc/conf","portal.xml");
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
	return downloadp;
}

private JButton getJButton11() 
{
	if (uploadp == null) 
	{
		uploadp = new JButton();
		uploadp.setBounds(new Rectangle(150, 65, 130, 25));
		uploadp.setText("upload portal");
		uploadp.addMouseListener(new java.awt.event.MouseAdapter() 
		{
			public void mouseClicked(java.awt.event.MouseEvent e) 
			{
				filename = "portal.xml";
						confirm = new JOptionPane();
						confirm.setSize(new Dimension(119, 69));
						yes = JOptionPane.showConfirmDialog(confirm,"Upload " + filename +"?","Upload",JOptionPane.YES_NO_OPTION);
					if(yes == JOptionPane.YES_OPTION)
					{
					try
				{
				    sys_path = "/home/jgr208";
					upload("/portal/etc/conf","portal.xml");
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
	return uploadp;
	}
}