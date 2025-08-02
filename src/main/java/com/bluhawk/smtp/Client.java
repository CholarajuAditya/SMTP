package com.bluhawk.smtp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
	private int sendMail(Email mail, ObjectInputStream in, ObjectOutputStream out) throws IOException, Exception{
		Message req = null, res = null;
		
		//HELO/EHLO
		req = new Message("HELO");
		res = request(out, in, (Object)req);
		handleResponse(res);

		//MAIL FROM, RCPT TO, DATA
		mail.send(); //to add timestamp and unique message id
		res = request(out, in, (Object)mail);
		handleResponse(res);
		
		//BYE
		req = new Message("BYE");
		res = request(out, in, (Object)req);
		handleResponse(res);

		return 250;
	}

	private Message request(ObjectOutputStream out, ObjectInputStream  in,Object obj) throws IOException, Exception{
		
		if(obj instanceof Message){
			out.writeObject((Message)obj);
		}else if(obj instanceof Email){
			out.writeObject((Email)obj);	
		}
		out.flush();
		
		//response Message object
		return (Message)in.readObject();
	}

	private void handleResponse(Message res) throws Exception{
		if(!res.message.equalsIgnoreCase("250")){
			throw new Exception(res.message);
		}
	}

	public static void main(String[] args) {
		Socket socket = null;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		
		try {
			//email details
			String from = "abc@gmail.com";
			String to = "xyz@gmail.com";
			String subject = "test mail";
			String body = "Hello! on the other side... This is a test email from the client";
			Email mail = new Email(from, to, subject, body);
		
			DNSRecord record = DNSResolver.getRecord(to.split("@")[1]); //gets the domain from the to email address
			String ip = record.a; //gets the ip address of the domain server
			socket = new Socket(ip, 2525);
			//out must be initialized before in - otherwise deadlock
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());

			// String ;
			Client client = new Client();
			if(client.sendMail(mail, in, out) == 250){
				System.out.println("Email sent successfully!!");
			}
		} catch(IOException e) {
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
