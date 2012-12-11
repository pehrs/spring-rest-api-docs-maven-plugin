package com.pehrs.spring.api.doc;

public class JSONTextDecorator implements JSONDecorator {

	@Override
	public void classComment(StringBuilder out, String prefix, String name) {
		out.append(prefix + "/* " + name + " */").append("\n");
	}

	@Override
	public void classHead(StringBuilder out, String prefix) {
		out.append(prefix + "{").append('\n');		
	}

	@Override
	public void classTail(StringBuilder out, String prefix) {
		out.append("\n").append(prefix).append("}");
	}

	@Override
	public void fundamentalPropertyType(StringBuilder out, String prefix, String propertyName, String sampleData,
			String propertyComment) {
		out.append(prefix + "  \"" + propertyName + "\": "
				+ sampleData + " /* "
				+ propertyComment+" */");
		
	}

	@Override
	public void collectionHead(StringBuilder out, String prefix, String propertyName, String propertyComment) {
		out.append(prefix + "  \"" + propertyName + "\": [ /* "
				+ propertyComment + " */").append('\n');
	}

	@Override
	public void collectionTail(StringBuilder out, String prefix) {
		out.append("\n").append("\n").append(prefix).append("]");
	}

	@Override
	public void text(StringBuilder out, String prefix, String generatedSampleDataArray) {
		out.append(prefix + " " + generatedSampleDataArray);
	}

	@Override
	public void propertyHead(StringBuilder out, String prefix, String propertyName) {
		out.append(prefix + "  \"" + propertyName + "\": ");
	}

	@Override
	public void propertyTail(StringBuilder out, String prefix) {
		// Nothing for now...
	}


}
