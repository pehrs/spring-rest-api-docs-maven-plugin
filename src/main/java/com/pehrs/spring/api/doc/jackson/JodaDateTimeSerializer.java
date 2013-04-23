package com.pehrs.spring.api.doc.jackson;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class JodaDateTimeSerializer extends StdSerializer<DateTime> {

	public JodaDateTimeSerializer() {
		super(DateTime.class, false);
	}

	@Override
	public void serialize(DateTime val, JsonGenerator generator,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		if(val==null) {
			generator.writeNull();			
		} else {						
			generator.writeString(ISODateTimeFormat.dateTimeNoMillis().print(val));
		}
	}

}
