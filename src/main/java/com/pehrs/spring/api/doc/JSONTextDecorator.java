package com.pehrs.spring.api.doc;

public class JSONTextDecorator implements JSONDecorator {

	/* (non-Javadoc)
	 * @see com.dalockr.api.generator.JSONDecorator#classComment(java.lang.StringBuilder, java.lang.String, java.lang.String)
	 */
	@Override
	public void classComment(StringBuilder out, String prefix, String name) {
		out.append(prefix + "/* " + name + " */").append("\n");
	}

	/* (non-Javadoc)
	 * @see com.dalockr.api.generator.JSONDecorator#classHead(java.lang.StringBuilder, java.lang.String)
	 */
	@Override
	public void classHead(StringBuilder out, String prefix) {
		out.append(prefix + "{").append('\n');		
	}

	/* (non-Javadoc)
	 * @see com.dalockr.api.generator.JSONDecorator#classTail(java.lang.StringBuilder, java.lang.String)
	 */
	@Override
	public void classTail(StringBuilder out, String prefix) {
		out.append(prefix + "}").append('\n');;		
	}

	/* (non-Javadoc)
	 * @see com.dalockr.api.generator.JSONDecorator#fundamentalPropertyType(java.lang.StringBuilder, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void fundamentalPropertyType(StringBuilder out, String prefix, String propertyName, String sampleData,
			String propertyComment) {

		out.append(prefix + "  \"" + propertyName + "\": "
				+ sampleData + "," + " /* "
				+ propertyComment+" */").append('\n');
		
	}

	/* (non-Javadoc)
	 * @see com.dalockr.api.generator.JSONDecorator#collectionHead(java.lang.StringBuilder, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void collectionHead(StringBuilder out, String prefix, String propertyName, String propertyComment) {
		out.append(prefix + "  \"" + propertyName + "\": [ /*"
				+ propertyComment + " */").append('\n');
	}

	/* (non-Javadoc)
	 * @see com.dalockr.api.generator.JSONDecorator#collectionTail(java.lang.StringBuilder, java.lang.String)
	 */
	@Override
	public void collectionTail(StringBuilder out, String prefix) {
		out.append(prefix + "  ],").append('\n');		
	}

	/* (non-Javadoc)
	 * @see com.dalockr.api.generator.JSONDecorator#text(java.lang.StringBuilder, java.lang.String, java.lang.String)
	 */
	@Override
	public void text(StringBuilder out, String prefix, String generatedSampleDataArray) {
		out.append(prefix + "    " + generatedSampleDataArray).append('\n');		
	}

	/* (non-Javadoc)
	 * @see com.dalockr.api.generator.JSONDecorator#propertyHead(java.lang.StringBuilder, java.lang.String, java.lang.String)
	 */
	@Override
	public void propertyHead(StringBuilder out, String prefix, String propertyName) {
		out.append(prefix + "  \"" + propertyName + "\": ").append('\n');		
	}

	/* (non-Javadoc)
	 * @see com.dalockr.api.generator.JSONDecorator#propertyTail(java.lang.StringBuilder, java.lang.String)
	 */
	@Override
	public void propertyTail(StringBuilder out, String prefix) {
		out.append(prefix + "  ,").append('\n');		
	}


}
