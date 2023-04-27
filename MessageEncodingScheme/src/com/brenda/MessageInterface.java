package com.brenda;

public interface MessageInterface {
	
	 public byte[] encode() throws IllegalArgumentException ;
	 public Message decode(byte[] data) throws IllegalArgumentException ;
		 
		

}
