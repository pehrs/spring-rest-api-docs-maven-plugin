package com.pehrs.spring.api.doc.json;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.pehrs.spring.api.doc.jackson.JacksonTypeModuleTest.SampleClass;

public class JsonUtilTest {

	@Test
	public void test() throws JsonGenerationException, JsonMappingException, IOException {
		System.setProperty("jackson.values", "jackson-values-sample.properties");
		System.out.println(""+JsonUtil.createJsonSample(SampleClass.class));
	}

}
