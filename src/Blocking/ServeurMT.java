package Blocking;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurMT extends Thread{
	private int nombreClients ;

	public static void main(String[] args) {
		
		new ServeurMT().start();
	 
	}
	
	@Override
	public void run() {
		 try {
			ServerSocket serverSocket = new ServerSocket(1234);
			
			while (true) {
				Socket socket = serverSocket.accept();
				++nombreClients;

				new Conversation(socket, nombreClients).start();

			} 
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
	public class Conversation extends Thread{
		private Socket socketClient;
		private int numero;
		
		public Conversation(Socket socketClient, int numero) {
			this.socketClient = socketClient;
			this.numero = numero;
		}  
		
		@Override
		public void run() {
			try {
			InputStream inputStream = socketClient.getInputStream();
			InputStreamReader isr=new InputStreamReader(inputStream);
			BufferedReader br = new BufferedReader(isr);
			OutputStream os=socketClient.getOutputStream();
			
			PrintWriter pw = new PrintWriter(os, true);
			String ipClient = socketClient.getRemoteSocketAddress().toString();			
			pw.println("Bien Venue vous etes le client numéro : "+numero);
			System.out.println("Connexion du client numéro "+numero+  ", IP = "+ipClient);
			 
			while(true) {
				String req = br.readLine();
				String reponse= "Length = "+req.length();
				pw.println(reponse);
			}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		}
		
		
	}

}
