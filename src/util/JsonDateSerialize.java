/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Date;
import java.text.SimpleDateFormat;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 *
 * @author ASUS555
 */
public class JsonDateSerialize extends JsonSerializer<Date>{

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException,JsonProcessingException{
        
        String formattedDate = dateFormat.format(date);
        gen.writeString(formattedDate);
    }

    
}
