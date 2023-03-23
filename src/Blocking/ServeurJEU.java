package Blocking;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServeurJEU extends Thread{
	private int nombreClients ;
	private int nombreSecret;
	private boolean fin;
	private String gagnant ;

	public static void main(String[] args) {
		
		new ServeurJEU().start();
	 
	}
	
	@Override
	public void run() {
		 try {
			ServerSocket serverSocket = new ServerSocket(1234);
			nombreSecret = new Random().nextInt(1000);
			System.out.println("le nombre choisi est : "+nombreSecret);
			
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
			pw.println("Devinez le nombre secret .......!!!");
			 
			while(true) {
				String req = br.readLine();
				int nombre = Integer.parseInt(req);
				System.out.println("Client "+ipClient+" Tentative avec le nombre : "+nombre);
				if(fin==false) {
					if(nombre>nombreSecret) {
						pw.println("votre nombre es supérieur au nombre secret");
					}
					else if(nombre<nombreSecret) {
						pw.println("Votre nombre est inférieur au nombre secret ");
					}
					else {
						pw.println("BRAVOO..!!! Vous avez gagné");
						gagnant = ipClient ;
						System.out.println("BRAVOO...!! au gagnant , IP Client : "+ipClient);
						fin=true;
					}
				}else {
					pw.println("JEU terminer, li gagnant est :"+gagnant);
				}
				 
			}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		}
		
		
	}

}
