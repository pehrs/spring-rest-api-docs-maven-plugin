package com.pehrs.spring.api.doc;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;

public abstract class LogUtils {

	public static final String SYSARG_LOGGING_LEVEL = "logging.level";

	public static void initLogging() {
		org.apache.log4j.Logger root = org.apache.log4j.Logger.getRootLogger();
		Level level = Level.toLevel(System.getProperty(SYSARG_LOGGING_LEVEL, ""
				+ Level.DEBUG));
		root.setLevel(level);
		root.removeAllAppenders();
		root.addAppender(new ConsoleAppender(new PatternLayout(
				PatternLayout.DEFAULT_CONVERSION_PATTERN)));
	}

}
