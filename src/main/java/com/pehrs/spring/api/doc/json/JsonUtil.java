package com.pehrs.spring.api.doc.json;

import java.io.IOException;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pehrs.spring.api.doc.jackson.JacksonTypeModule;

public class JsonUtil {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String createJsonSample(Class sampleClass) throws JsonGenerationException, JsonMappingException, IOException {
		PodamFactory podamFacory = new PodamFactoryImpl();
		Object sample = podamFacory.manufacturePojo(sampleClass);

		JacksonTypeModule module = new JacksonTypeModule();
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(module);	
		mapper.setSerializationInclusion(Include.NON_NULL);
		ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
		return writer.writeValueAsString(sample);
	}
	
}
