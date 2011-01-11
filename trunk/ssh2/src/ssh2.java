import com.jcraft.jsch.*;

//import java.awt.*;
//import javax.swing.*;

import java.util.Scanner;

import javax.swing.JOptionPane;

public class ssh2 {

	public static void main(String[] args){
		
		System.out.println("This program will allow you to log into ssh through java");
		try
		{
		Scanner keyboard = new Scanner(System.in);
		
		String host = null;
		
		JSch jsch = new JSch();
		
		if(args.length>0)
		{
			host = args[0];
		}
		else
		{
		System.out.println("Enter username@server");
		
		host = keyboard.next();
		
		System.out.println(host);
		}
	String user = host.substring(0, host.indexOf('@'));
	host = host.substring(host.indexOf('@')+1);
	
	Session session=jsch.getSession(user, host, 22);
		
    UserInfo ui = new MyUserInfo();
    session.setUserInfo(ui);

    session.setConfig("cipher.s2c", "aes128-cbc,3des-cbc,blowfish-cbc");
    session.setConfig("cipher.c2s", "aes128-cbc,3des-cbc,blowfish-cbc");
    session.setConfig("CheckCiphers", "aes128-cbc");

    //this is what connectes to get the finger print
    session.connect();
    
    Channel channel=session.openChannel("shell");

    channel.setInputStream(System.in);
    channel.setOutputStream(System.out);

    channel.connect();
	}
	catch(Exception e)
	{
	System.out.println(e);
	}
}
    public static class MyUserInfo implements UserInfo, UIKeyboardInteractive
    {
    	String passwd = null;
    	Scanner keyboard = new Scanner(System.in);
    	
    	public String getPassword(){ return passwd; }
    	
    	
		@Override
		public String[] promptKeyboardInteractive(String arg0, String arg1,
				String arg2, String[] arg3, boolean[] arg4) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getPassphrase() {
			// TODO Auto-generated method stub
			return null;
		}
		
		
		@Override
		public boolean promptPassphrase(String arg0) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean promptPassword(String passwd) {
			// TODO Auto-generated method stub
			System.out.println("Please enter your password");
			passwd = keyboard.next();
			return true;
		}

		@Override
		public boolean promptYesNo(String str) {
			// TODO Auto-generated method stub
			//Object[] options={ "yes", "no" };
    		//int foo = JOptionPane.showOptionDialog(null, 
	          //     str,
	               //"Warning", 
	            //   JOptionPane.DEFAULT_OPTION, 
	              // JOptionPane.WARNING_MESSAGE,
	              // null, options, options[0]);
    		return true;
		}

		@Override
		public void showMessage(String arg0) {
			// TODO Auto-generated method stub
			
		}
}
}