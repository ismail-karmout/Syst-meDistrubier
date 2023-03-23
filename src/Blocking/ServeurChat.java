package Blocking;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServeurChat extends Thread{
	private int nombreClients ;
    private List<Conversation> clients =new ArrayList<Conversation>();
	public static void main(String[] args) {
		
		new ServeurChat().start();
	 
	}
	
	@Override
	public void run() {
		 try {
			ServerSocket serverSocket = new ServerSocket(1234);
			
			while (true) {
				Socket socket = serverSocket.accept();
				++nombreClients;

				Conversation conversation = new Conversation(socket, nombreClients);
				clients.add(conversation);
				conversation.start();

			} 
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
	public class Conversation extends Thread{
		protected Socket socketClient;
		protected int numero;
		
		public Conversation(Socket socketClient, int numero) {
			this.socketClient = socketClient;
			this.numero = numero;
		}  
		public void broadcastmessage(String message , Socket socket , int numClient){
			try {
				for(Conversation client:clients ) {
					if(client.socketClient!=socket) {
						if(client.numero==numClient || numClient==-1) {
							 PrintWriter printWriter = new PrintWriter(client.socketClient.getOutputStream(),true);
					         printWriter.println(message);
						}
					   
					   }
					
					}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
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
				if(req.contains("=>")) {
					String[] requestParams = req.split("=>");
					if(requestParams.length==2);
					String message = requestParams[1];
					int numeroClient = Integer.parseInt(requestParams[0]);
					broadcastmessage(message , socketClient ,numeroClient);
				}
				else {
					broadcastmessage(req ,socketClient ,-1);
				}
				
				
				
			}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		}
		
		
	}

}
