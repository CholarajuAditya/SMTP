import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
	public static void main(String[] args) {
		Socket socket = null;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		
		try {
			socket = new Socket("localhost", 2525);
			//out must be initialized before in - otherwise deadlock
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());

			while(true) {
				String from = "abc@gmail.com";
				String to = "xyz@gmail.com";
				String subject = "test mail";
				String body = "Hello! on the other side... This is a test email from the client";
				Email email = new Email(from, to, subject, body);
				
				email.send();
				out.writeObject(email);
				out.flush();

				Response res = (Response)in.readObject();
				if(res.message.equalsIgnoreCase("MAIL RECIEVED")){
					System.out.println("Server: " + res.message);
					break;
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		} 
		finally {
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
