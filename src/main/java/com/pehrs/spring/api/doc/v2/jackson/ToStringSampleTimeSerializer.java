package com.pehrs.spring.api.doc.v2.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@SuppressWarnings("rawtypes")
public class ToStringSampleTimeSerializer extends StdSerializer {

	private final String sampleValue;

	@SuppressWarnings("unchecked")
	public ToStringSampleTimeSerializer(Class t, String sampleValue) {
		super(t);
		this.sampleValue = sampleValue;
	}

	@Override
	public void serialize(Object value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonGenerationException {
		jgen.writeString(sampleValue);		
	}

}
