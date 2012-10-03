package com.pehrs.spring.api.doc;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JacksonJSONGenerator {

	@SuppressWarnings("rawtypes")
	public String generateJSONSample(Class parameterClass,
			java.lang.reflect.Type genericSuperclass) {
		
		try {
			Object instance = parameterClass.newInstance();
			
			ObjectMapper mapper = new ObjectMapper();			
			ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
			
			return writer.writeValueAsString(instance);			
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			return "/* Could not generate JSON Sample for "+parameterClass.getName()+"*/";
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return "/* Could not generate JSON Sample for "+parameterClass.getName()+"*/";
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			return "/* Could not generate JSON Sample for "+parameterClass.getName()+"*/";
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return "/* Could not generate JSON Sample for "+parameterClass.getName()+"*/";
		} catch (IOException e) {
			e.printStackTrace();
			return "/* Could not generate JSON Sample for "+parameterClass.getName()+"*/";
		}
		
		
	}

}
