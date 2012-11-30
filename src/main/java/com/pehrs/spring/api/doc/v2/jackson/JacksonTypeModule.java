package com.pehrs.spring.api.doc.v2.jackson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

public class JacksonTypeModule extends SimpleModule {
	
	private static final Logger log = Logger.getLogger(JacksonTypeModule.class);
	
	private static Version version = new Version(1, 0, 0, "SNAPSHOT", "com.pehrs.json", "pehrs-module");
	
	public JacksonTypeModule() {
		super("PehrsJsonModule", version);
	}

	
	private static Map<Class<?>, String> preconfigValues = null;


	@SuppressWarnings("unchecked")
	@Override
	public void setupModule(SetupContext context) {
	   	 SimpleSerializers serializers = new SimpleSerializers();
         SimpleDeserializers deserializers = new SimpleDeserializers();
         
 		if(preconfigValues==null) {
			preconfigValues=new HashMap<Class<?>, String>();
			
			String preconfigValuesFile = System.getProperty("jackson.values");
			if(preconfigValuesFile==null) {
				throw new RuntimeException("You must set the -Djackson.values property");
			}
			Properties props = new Properties();
			FileInputStream in = null;
			try {
				in = new FileInputStream(preconfigValuesFile);
				props.load(in);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					if(in!=null) {
						in.close();
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			for(Object key:props.keySet()) {
				Class<?> theClass;
				try {
					theClass = Class.forName(""+key);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
				String val = props.getProperty(""+key);
				log.debug("["+theClass.getName()+"]="+val);
				preconfigValues.put(theClass, val);
			}
		}


         serializers.addSerializer(DateTime.class, new JodaDateTimeSerializer());
         deserializers.addDeserializer(DateTime.class, new JodaDateTimeDeserializer());
         
         // serializers.addSerializer(Percentage.class, new ToStringSampleTimeSerializer(Percentage.class, "45.5"));
         
         for(Class<?> theClass:preconfigValues.keySet()) {
        	 serializers.addSerializer(theClass,  
        			 new ToStringSampleTimeSerializer(theClass, preconfigValues.get(theClass)));
         }

         context.addSerializers(serializers);
         context.addDeserializers(deserializers);

	}

}
