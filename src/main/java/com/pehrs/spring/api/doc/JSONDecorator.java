package com.pehrs.spring.api.doc;

public interface JSONDecorator {

	public abstract void classComment(StringBuilder out, String prefix, String name);

	public abstract void classHead(StringBuilder out, String prefix);

	public abstract void classTail(StringBuilder out, String prefix);

	public abstract void fundamentalPropertyType(StringBuilder out, String prefix, String propertyName,
			String sampleData, String propertyComment);

	public abstract void collectionHead(StringBuilder out, String prefix, String propertyName, String propertyComment);

	public abstract void collectionTail(StringBuilder out, String prefix);

	public abstract void text(StringBuilder out, String prefix, String generatedSampleDataArray);

	public abstract void propertyHead(StringBuilder out, String prefix, String propertyName);

	public abstract void propertyTail(StringBuilder out, String prefix);

}