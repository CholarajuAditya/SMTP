package com.bluhawk.smtp;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private void SMTP(ObjectInputStream in, ObjectOutputStream out) throws IOException, Exception{
		Object recievedObject = null;
		Message res = null;
		
		//HELO/EHLO
		recievedObject = in.readObject();
		if(recievedObject instanceof Message){
			Message req = (Message) recievedObject;
			if(!req.message.equalsIgnoreCase("HELO"))
				throw new Error("HELO not intiated by client");
			sendResponse(in, out, res, "250");
		}else{
			sendResponse(in, out, res, "HELO error");
		}

		//MAIL FROM, RCPT TO, DATA
		recievedObject = in.readObject();
		if(recievedObject instanceof Email){
			//have to save it
			Email mail = (Email) recievedObject;
			//verify email function - DNS records
			System.out.println(mail);
			sendResponse(in, out, res, "250");
		}else{
			sendResponse(in, out, res, "email not valid");
		}

		//BYE
		recievedObject = in.readObject();
		if(recievedObject instanceof Message){
			Message req = (Message) recievedObject;
			if(!req.message.equalsIgnoreCase("BYE"))
				throw new Error("Bye not intiated by client");
			sendResponse(in, out, res, "250");
		}else{
			sendResponse(in, out, res, "BYE error");
		}
	}
	
	private void sendResponse(ObjectInputStream in, ObjectOutputStream out, Message res, String message) throws IOException, Exception{
		res = new Message(message);
		out.writeObject(res);
		out.flush();
	}

	private void verifyEmail(Email mail){
		
	}

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
						Server server = new Server();
						server.SMTP(in, out);
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
