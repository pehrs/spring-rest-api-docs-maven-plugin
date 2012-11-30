package com.pehrs.spring.api.doc.v2;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pehrs.spring.api.doc.v2.jackson.JacksonTypeModule;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

public class SpringControllerAPIDoclet {
	
	public static boolean start(RootDoc root) throws JsonGenerationException, JsonMappingException, IOException, ClassNotFoundException {
		
		LogUtils.initLogging();

		
		ClassDoc[] classes = root.classes();
		for (ClassDoc classDoc : classes) {
			// System.out.println("CLASS: "+classDoc);
			
			String className = classDoc.containingPackage().name()+"."+classDoc.name();
			Object value = ObjectValueGenerator.generateValue(className);
			// System.out.println("value="+value);
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			mapper.registerModule(new JacksonTypeModule());

			ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
			// System.out.println(df.toString());
			System.out.println("JSON ["+classDoc.name()+"]:\n" + writer.writeValueAsString(value));

		}
		
		
		return true;		
	}

	
	
}
