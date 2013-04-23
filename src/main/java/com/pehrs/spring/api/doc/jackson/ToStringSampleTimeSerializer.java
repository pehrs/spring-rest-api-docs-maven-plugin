package com.pehrs.spring.api.doc.jackson;

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
		if (super.handledType() == int.class) {
			jgen.writeNumber(Integer.parseInt(sampleValue));
		} else if (super.handledType() == short.class) {
			jgen.writeNumber(Short.parseShort(sampleValue));
		} else if (super.handledType() == long.class) {
			jgen.writeNumber(Long.parseLong(sampleValue));
		} else if (super.handledType() == byte.class) {
			jgen.writeNumber(Byte.parseByte(sampleValue));
		} else if (super.handledType() == float.class) {
			jgen.writeNumber(Float.parseFloat(sampleValue));
		} else if (super.handledType() == double.class) {
			jgen.writeNumber(Double.parseDouble(sampleValue));
		} else if (super.handledType() == boolean.class) {
			jgen.writeBoolean(Boolean.parseBoolean(sampleValue));
		} else {
			jgen.writeString(sampleValue);
		}
	}

}
