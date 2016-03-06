package com.central_controller;

import com.central_controller.image_meta;
import com.central_controller.MetaSerializer;
import java.util.ArrayList;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.Version;
import java.io.IOException;

public class meta_data_handler {
    private ObjectMapper mapper;
    private SimpleModule mod;

    public meta_data_handler() {
	
	mapper = new ObjectMapper();
	mod = new SimpleModule("MyModule", new Version(1, 0, 0, null));
	mod.addSerializer(new MetaSerializer());
	mapper.registerModule(mod);
    }

    //Not using API but sending to client
    public byte[] write_meta(){
	return null;
    }

    //For API
    public String api_call_write(){
	return "";
    }

    public void get_meta(){
	//GET DATA FROM SQL SERVER FOR META DATA

    }

    public String buildAllJSONresponse(ArrayList<image_meta> metas){
	String total_response = "";

	int last_count = metas.size();
	int this_count = 0;	
	try
	    {

		for(image_meta meta : metas)
		    {
			this_count++;
			if(this_count == last_count)
			    total_response += getJsonString(meta);
			else
			    total_response += getJsonString(meta) + ", ";
		    }
	    } catch(IOException e)
	    {
	    }
	return total_response;
    }

    public String getJsonString(image_meta toString) throws IOException {
	return mapper.writeValueAsString(toString);
    }
}
