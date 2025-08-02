package com.bluhawk.smtp;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;

public class DNSResolver {
    public static DNSRecord getRecord(String domain) throws Exception{
		InputStream in = Client.class.getClassLoader().getResourceAsStream("DNS.json");
		if(in == null)
			throw new Exception("DNS.json not found in resources");
		
		Gson gson = new Gson();
		DNSRecord[] records = gson.fromJson(new InputStreamReader(in), DNSRecord[].class);

		for(DNSRecord record : records){
			if(record.domain.equalsIgnoreCase(domain)){
				return record;
			}
		}

		throw new Exception("No DNS record found for domain: " + domain);
	} 
}
