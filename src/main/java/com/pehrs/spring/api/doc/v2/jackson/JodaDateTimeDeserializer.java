package com.pehrs.spring.api.doc.v2.jackson;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class JodaDateTimeDeserializer extends
		StdDeserializer<DateTime> {

	public JodaDateTimeDeserializer() {
		super(DateTime.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public DateTime deserialize(JsonParser parser,
			DeserializationContext ctx) throws IOException,
			JsonProcessingException {
		
		String val = parser.getText();				
		if(val==null) {
			return null;
		}

		return ISODateTimeFormat.dateTimeNoMillis().parseDateTime(val);
	}

}
