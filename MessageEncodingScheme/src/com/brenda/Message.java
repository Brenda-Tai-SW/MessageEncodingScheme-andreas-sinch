package com.brenda;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Message implements MessageInterface {
    public   Map<String, String> headers;
    public   byte[] payload;

    public Message(Map<String, String> headers, byte[] payload) {
        this.headers = headers;
        this.payload = payload;
    }
    
  

    // This method encodes a message into a byte array
    public byte[] encode() throws IllegalArgumentException {
        // Validate headers
        if (headers == null || headers.size() > 63) {
            throw new IllegalArgumentException("Invalid headers");
        }
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (entry.getKey().getBytes().length > 1023 || entry.getValue().getBytes().length > 1023) {
                throw new IllegalArgumentException("Header name or value too long");
            }
        }

        // Validate payload
        if (payload == null || payload.length > 256 * 1024) {
            throw new IllegalArgumentException("Invalid payload");
        }

        // Encode headers and payload
        ByteBuffer buffer = ByteBuffer.allocate(4 + headers.size() * (1023 + 1023) + payload.length);
        buffer.putInt(headers.size());
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            buffer.put(entry.getKey().getBytes());
            buffer.put(new byte[1023 - entry.getKey().getBytes().length]);
            buffer.put(entry.getValue().getBytes());
            buffer.put(new byte[1023 - entry.getValue().getBytes().length]);
        }
        buffer.put(payload);
        return buffer.array();
    }

    // This method decodes a message from a byte array
    public  Message decode(byte[] data) throws IllegalArgumentException {
        ByteBuffer buffer = ByteBuffer.wrap(data);

        // Decode headers
        int headerCount = buffer.getInt();
        Map<String, String> headers = new HashMap<>();
        for (int i = 0; i < headerCount; i++) {
            byte[] nameBytes = new byte[1023];
            buffer.get(nameBytes);
            String name = new String(nameBytes).trim();
            byte[] valueBytes = new byte[1023];
            buffer.get(valueBytes);
            String value = new String(valueBytes).trim();
            headers.put(name, value);
        }

        // Decode payload
        byte[] payload = new byte[buffer.remaining()];
        buffer.get(payload);

        return new Message(headers, payload);
    }
}