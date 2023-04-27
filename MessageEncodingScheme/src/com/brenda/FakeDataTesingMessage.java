package com.brenda;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FakeDataTesingMessage {
	
	 public static  Map<String, String> headers;
	
	static {
	     headers= new HashMap<String ,String>();
		 headers.put("Content-Type:","application/json");
		 headers.put("Authorization:","Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
		 headers.put("X-Request-ID:","1234567890abcdef");
		 
	     }
	
	
	public static void main(String args[]) {
		
		byte[] payload = new byte[256 * 1024];
		Arrays.fill(payload, (byte) 0);
		
		Message message =new Message(headers,payload);
		
		byte[] encodeMessage=message.encode();
		
	     Message messageTest= message.decode(encodeMessage); 
		 
		System.out.println(messageTest.headers);  
		  
		System.out.println(messageTest.payload);  
		 
		
	}

}
