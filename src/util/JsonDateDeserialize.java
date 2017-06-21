/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
/**
 * Used to serialize Java.util.Date, which is not a common JSON
 * type, so we have to create a custom deserialize method.
**/

public class JsonDateDeserialize extends JsonDeserializer<Date>{
	
private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

		@Override
		public Date deserialize(JsonParser jp, DeserializationContext ctx) throws IOException, JsonProcessingException {
		    
		    String dt = jp.getText();
		    if (dt == null || dt.trim().length() == 0) {
		        return null;
		    }
		    Date date = null;
		    try {
		        date = dateFormat.parse(dt);
		    } catch (ParseException e) {
		        throw new IOException(e);
		    }
		
		    return date;
		}
}