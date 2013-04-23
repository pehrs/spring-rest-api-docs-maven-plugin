package com.pehrs.spring.api.doc.jackson;

import java.io.IOException;

import org.joda.time.DateTime;
import org.junit.Test;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pehrs.spring.api.doc.jackson.JacksonTypeModule;

public class JacksonTypeModuleTest {

	public static class SampleClass {
		public final String reqId;
		public final DateTime timestamp;
		public final String data;

		public SampleClass(String reqId, DateTime timestamp, String data) {
			super();
			this.reqId = reqId;
			this.timestamp = timestamp;
			this.data = data;
		}

		@Override
		public String toString() {
			return "SampleClass [reqId=" + reqId + ", timestamp=" + timestamp
					+ ", data=" + data + "]";
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test() throws ClassNotFoundException, JsonGenerationException, JsonMappingException, IOException {
	
		System.setProperty("jackson.values", "jackson-values-sample.properties");

		PodamFactory podamFacory = new PodamFactoryImpl();
		Class sampleClass = Class.forName(SampleClass.class.getName());
		Object sample = podamFacory.manufacturePojo(sampleClass);

		JacksonTypeModule module = new JacksonTypeModule();
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(module);	
		mapper.setSerializationInclusion(Include.NON_NULL);
		ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
		System.out.println("JSON:\n" + writer.writeValueAsString(sample));
	}

}
