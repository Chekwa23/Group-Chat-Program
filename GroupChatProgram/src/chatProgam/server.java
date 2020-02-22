package chatProgam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A server side class for a chat program to interact with multiple clients and distribute messages.
 * @author Lucas Onwuchekwa
 */
public class server 
{
	/**
	 * Array list of clients handlers to keep track of all the clients that connected to the server.
	 */
	public static ArrayList<clientHandler> clients = new ArrayList<>();
	/**
	 * The access code for the server.
	 */
	public static final String ACCESS_CODE = "chekwa23";
	/**
	 * Constant port number for connecting to the socket.
	 */
	public static final int PORT = 2348;
	
	public static void main(String args[]) throws IOException
	{
		ServerSocket servSock = null;
		int i = 1;
		try 
		{
			System.out.println("Server: Server started");
			servSock = new ServerSocket(PORT);
		} 
		catch (IOException e) 
		{
			System.err.println("Server: Sorry can't listen to port");
		}
		
		while(true)
		{
			System.out.println("Server: Server is waiting for client request");
			Socket socks = null;
			try 
			{
				socks = servSock.accept();
				String name = "Client "+ i;
				clientHandler client = new clientHandler(socks, name);
				clients.add(client);
				Thread t = new Thread(client);
				t.start();
				i++;
			} 
			catch (IOException e) 
			{
				System.err.println("Server: Accept failed");
			}
		}
	}
	
	public static class clientHandler implements Runnable
	{
		Socket socket;
		String id;
		String clientName = "";
		Scanner in;
		PrintWriter out;
		
		public clientHandler(Socket socket, String name)
		{
			this.socket = socket;
			id = name;
			try 
			{
				out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
				in = new Scanner(new InputStreamReader(socket.getInputStream()));
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() 
		{
			out.println("Server: Enter your name");
			clientName = in.nextLine();
			while(true)
			{
				out.println("Server: Input Access Code");
				out.flush();
				String temp = in.nextLine();
				if(temp.equals(ACCESS_CODE))
				{
					out.println("Server: Hello "+ clientName +"!! You are connected");
					out.println("Server: Start Chatting");
					break;
				}
				out.println("Server: Incorrect Access Code... Try again");
			}
			
			while(true)
			{
				try
				{
					String newMessage = in.nextLine();
					System.out.println(clientName+": "+newMessage);
					for(clientHandler ch : clients)
					{
						if(!ch.id.equals(id))
						{
							ch.out.println(clientName+": "+newMessage);
							ch.out.flush();
						}
					}
				}
				catch(NoSuchElementException e)
				{
					//do non
				}
			}
		}

	}
}
