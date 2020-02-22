package chatProgam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A client side class for a chat program to interact with multiple clients through the server 
 * @author Lucas Onwuchekwa
 */
public class client 
{
//	public static final String ACCESS_CODE = "chekwa23";
	/**
	 * A constant port number
	 */
	public static final int PORT = 2348;
	
	public static void main(String args[]) throws IOException
	{
		Socket sock = null;
		
		try
		{
			//Using localHost = 127.0.0.1
			sock = new Socket("127.0.0.1",PORT);
		}
		catch(UnknownHostException e)
		{
			System.err.println("Server: Sorry can't listen to port");
		} 
		catch (IOException e) 
		{
			System.err.println("Server: Sorry can't listen to port");
		}
		
		Scanner in = new Scanner(new InputStreamReader(sock.getInputStream()));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()), true);
		
		
		// sendMessage thread 
		Thread sendMessage = new Thread(new Runnable() 
		{ 
		@Override
		public void run() 
		{ 
			while (true) 
			{
				Scanner tryScan = new Scanner(System.in);
				String msg = tryScan.nextLine(); 
							
				// write on the output stream 
				out.println(msg);
				out.flush();
			} 
		} 
		}); 
				
		// readMessage thread 
		Thread readMessage = new Thread(new Runnable() 
		{ 
			@Override
			public void run() { 

			while (true) 
			{ 
				// read the message sent to this client 
				try
				{
					String msg = in.nextLine();
					System.out.println(msg); 
				}
				catch(NoSuchElementException e)
				{
					//do non
				}
			} 
		} 
		}); 

		sendMessage.start(); 
		readMessage.start(); 
	}
}
