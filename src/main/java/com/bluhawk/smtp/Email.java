package com.bluhawk.smtp;

import java.io.Serializable;
import java.time.ZonedDateTime;
public class Email implements Serializable {
	private String from;
	private String to;
	private String subject;
	private ZonedDateTime date;
	private String messageId;
	private String body;
	private String signature;
	
	public Email(String from, String to, String subject, String body){
		if(from == null || to == null || !from.contains("@") || !to.contains("@")) {
			throw new IllegalArgumentException("provide a valid from/to email address");
		}
		
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.date = null; //date & timestamp will be set when the client sends it
		this.body = body;
		this.messageId = null;
		this.signature = null;
	}
	
	public void setFrom(String from){
		this.from = from;
	}
	
	public String getFrom() {
		return from; 
	}

	public void setTo(String to){
		this.to = to;
	}
	
	public String getTo() {
		return to; 
	}
	
	public void setSubject(String subject){
		this.subject = subject;
	}

	public String getSubject() {
		return subject; 
	}
	
	public ZonedDateTime getDate() {
		return date; 
	}

	public void setBody(String body){
		this.body = body;
	}
	
	public String getBody() {
		return body;
	}
	
	public String getMessageId() {
		return messageId;
	}

	public void send(int privateKey) {
		messageId = "<" + System.currentTimeMillis() + "@" + from.split("@")[1] + ">" ;
		this.date = ZonedDateTime.now();

		//signing the email
		String message = subject + body;
        StringBuilder sb = new StringBuilder();
        for (char ch : message.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                sb.append((char)((ch - base + privateKey) % 26 + base));
            } else {
                sb.append(ch);
            }
        }
        this.signature = sb.toString();
	}

	public String getSignature(){
		return signature;
	}

	@Override
	public String toString(){
		return "From: " + this.from + "\nTo: " + this.to + "\nSubject: " + this.subject + "\nDate: " + this.date + "\nmessageId: " + this.messageId + "\nBody: " + this.body;
	}	
}
