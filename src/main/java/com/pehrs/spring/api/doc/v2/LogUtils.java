package com.pehrs.spring.api.doc.v2;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;

public abstract class LogUtils {

	public static void initLogging() {
		org.apache.log4j.Logger root = org.apache.log4j.Logger.getRootLogger();
		Level level = Level.toLevel(System.getProperty("logging.level", ""
				+ Level.DEBUG));
		root.setLevel(level);
		root.removeAllAppenders();
		root.addAppender(new ConsoleAppender(new PatternLayout(
				PatternLayout.DEFAULT_CONVERSION_PATTERN)));
	}

}
