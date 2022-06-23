package com.meli.Utils;

import java.util.HashMap;
import java.util.Map;

public class CodeAndDecode {
	
    // Encodes a URL to a shortened URL.
    //TinyURL to URL
    static Map<String,String> shortMap = new HashMap<>();
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int count = 6;
	
    public static String encode(String longUrl) {
        //String uniqID = "https://me.li/"+generateID();
    	
    	String uniqID = "http://localhost/"+generateID();
        if(shortMap.containsKey(uniqID)) {
            String url = shortMap.get(uniqID);
			//If different long URL exists mapping the generated short URL, call generate short url method again
            if(!url.equals(longUrl))
                return encode(longUrl);
            else
                return uniqID;
        }
        else {
            shortMap.put(uniqID,longUrl);
            return uniqID;
        }
        
    }

    private static String generateID() {
        StringBuilder builder = new StringBuilder();
        int temp = count;
        while (temp-- != 0) {
            //Generate random alphanumeric string
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
     // Decodes a shortened URL to its original URL.
    public static String decode(String shortUrl) {
        return shortMap.get("http://localhost/"+shortUrl);
    }

}
