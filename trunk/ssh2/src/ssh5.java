
import com.jcraft.jsch.*;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.InputStream;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
public class ssh5 {
	public static void main(String[] args)
	{
			ssh5 s = new ssh5();
			try
			{
			s.login();
			}
			catch(Exception e)
			 {
				System.out.println(e);
			 }
		}
			public void login() throws Exception
			{
				Scanner keyboard = new Scanner(System.in);
				String host = null;
				System.out.println("Enter user for nees server");				String nees =  "@neespop.nees.lehigh.edu";
				host =  keyboard.next() +  nees;
				//System.out.println("Please enter your password");
				//String password = keyboard.next();
				String user = host.substring(0, host.indexOf('@'));
			    host = host.substring(host.indexOf('@')+1);
				JSch jsch = new JSch();												
				Session session = jsch.getSession(user, host, 22);				
				//session.setPassword(password);					
				//auto accept of host key
				
				//session.setConfig("StrictHostKeyChecking", "yes");
				UserInfo ui=new MyUserInfo();
			    session.setUserInfo(ui);
				session.connect();				
				//gets the hostkey and prints out to verifyt i have the host key
				//HostKey hk=session.getHostKey();
				//System.out.println("HostKey: "+
				//		   hk.getHost()+" "+
				//		   hk.getType()+" "+
				//		   hk.getFingerPrint(jsch));				
				
				/*****
				 * just the new stuff that was added
				 *******/
				//this opens up the channel to the shh server to take commands from java
				Channel channel = session.openChannel("exec");
				System.out.println("enter 1 to start or 2 to stop");
				//int that corrsponds to what commands the user wants to run
				int command = keyboard.nextInt();
				//if 1 starts 
				if(command == 1)
				  //((ChannelExec)channel).setCommand("ssh -T " + user + "@" + host + " sudo /sbin/service flextps_proxies restart");
				  ((ChannelExec)channel).setCommand("sudo /sbin/service flextps_httpd restart;echo neess");
					//((ChannelExec)channel).setCommand("ssh -T " + user + "@" + host + " ls -al");
				//if 2 stops
				else if(command == 2)
				((ChannelExec)channel).setCommand("ssh -T " + host + " sudo /sbin/service flextps_proxies stop");
				/*****
				 * end of new stuff that was added
				 *******/
				((ChannelExec)channel).setErrStream(System.err);			
				channel.setInputStream(null);
				InputStream in=channel.getInputStream();

				channel.connect(); 
				
				 byte[] tmp=new byte[1024];
			      while(true){
			        while(in.available()>0){
			          int i=in.read(tmp, 0, 1024);
			          if(i<0)break;
			          System.out.print(new String(tmp, 0, i));
			        }
			        if(channel.isClosed()){
			          System.out.println("exit-status: "+channel.getExitStatus());
			          break;
			        }
			        try{Thread.sleep(1000);}catch(Exception ee){}
			      }
			      channel.disconnect();
			      session.disconnect();
			}
			
			public static class MyUserInfo implements UserInfo, UIKeyboardInteractive{
			    public String getPassword(){ return passwd; }
			    public boolean promptYesNo(String str){
			      Object[] options={ "yes", "no" };
			      int foo=JOptionPane.showOptionDialog(null, 
			             str,
			             "Warning", 
			             JOptionPane.DEFAULT_OPTION, 
			             JOptionPane.WARNING_MESSAGE,
			             null, options, options[0]);
			       return foo==0;
			    }
			  
			    String passwd;
			    JTextField passwordField=(JTextField)new JPasswordField(20);

			    public String getPassphrase(){ return null; }
			    public boolean promptPassphrase(String message){ return true; }
			    public boolean promptPassword(String message){
			      Object[] ob={passwordField}; 
			      int result=
			        JOptionPane.showConfirmDialog(null, ob, message,
			                                      JOptionPane.OK_CANCEL_OPTION);
			      if(result==JOptionPane.OK_OPTION){
			        passwd=passwordField.getText();
			        return true;
			      }
			      else{ 
			        return false; 
			      }
			    }
			    public void showMessage(String message){
			      JOptionPane.showMessageDialog(null, message);
			    }
			    final GridBagConstraints gbc = 
			      new GridBagConstraints(0,0,1,1,1,1,
			                             GridBagConstraints.NORTHWEST,
			                             GridBagConstraints.NONE,
			                             new Insets(0,0,0,0),0,0);
			    private Container panel;
			    public String[] promptKeyboardInteractive(String destination,
			                                              String name,
			                                              String instruction,
			                                              String[] prompt,
			                                              boolean[] echo){
			      panel = new JPanel();
			      panel.setLayout(new GridBagLayout());

			      gbc.weightx = 1.0;
			      gbc.gridwidth = GridBagConstraints.REMAINDER;
			      gbc.gridx = 0;
			      panel.add(new JLabel(instruction), gbc);
			      gbc.gridy++;

			      gbc.gridwidth = GridBagConstraints.RELATIVE;

			      JTextField[] texts=new JTextField[prompt.length];
			      for(int i=0; i<prompt.length; i++){
			        gbc.fill = GridBagConstraints.NONE;
			        gbc.gridx = 0;
			        gbc.weightx = 1;
			        panel.add(new JLabel(prompt[i]),gbc);

			        gbc.gridx = 1;
			        gbc.fill = GridBagConstraints.HORIZONTAL;
			        gbc.weighty = 1;
			        if(echo[i]){
			          texts[i]=new JTextField(20);
			        }
			        else{
			          texts[i]=new JPasswordField(20);
			        }
			        panel.add(texts[i], gbc);
			        gbc.gridy++;
			      }

			      if(JOptionPane.showConfirmDialog(null, panel, 
			                                       destination+": "+name,
			                                       JOptionPane.OK_CANCEL_OPTION,
			                                       JOptionPane.QUESTION_MESSAGE)
			         ==JOptionPane.OK_OPTION){
			        String[] response=new String[prompt.length];
			        for(int i=0; i<prompt.length; i++){
			          response[i]=texts[i].getText();
			        }
				return response;
			      }
			      else{
			        return null;  // cancel
			      }
			    }
			  }
}