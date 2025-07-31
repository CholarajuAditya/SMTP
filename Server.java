import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	public static void main(String args[]) {
		Socket socket = null;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		ServerSocket serverSocket = null;

	
		try {		
			serverSocket = new ServerSocket(2525);
			
			while(true) {
				socket = serverSocket.accept();
				//out must be initialized before in - otherwise deadlock
				out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();
				in = new ObjectInputStream(socket.getInputStream());
				try {
					while(true) {
						Email mail = (Email) in.readObject();
						
						Response res = new Response();
						System.out.println(mail);
						res.message = "mail Recieved";
						
						out.writeObject(res);
						out.flush();	
					}
				} catch(EOFException e){
					System.out.println("client disconnected...");
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			try {				
				if(socket != null) 
					socket.close();
				if(in != null)
					in.close();
				if(out != null)
					out.close();
			} catch(IOException e) {
				e.printStackTrace();
			} catch(Exception e){
			e.printStackTrace();
			} 
		}
	}
}
