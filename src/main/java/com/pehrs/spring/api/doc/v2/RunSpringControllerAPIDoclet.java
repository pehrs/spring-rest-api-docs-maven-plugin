package com.pehrs.spring.api.doc.v2;

import java.io.File;
import java.io.FileFilter;

public class RunSpringControllerAPIDoclet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String loggingLevel = "DEBUG";
		String targetDir =  "target/sample";
		String groupId = "com.pehrs";
		String artifactId = "sample-api";
		String projectName = "project-name";
		String projectVersion = "1.0.0-SNAPSHOT";
		String urlPrefix = "/api";

 		System.setProperty("logging.level",loggingLevel);
 		System.setProperty("target", targetDir);
 		System.setProperty("pom.group.id", groupId);
 		System.setProperty("pom.artifact.id", artifactId);
 		System.setProperty("pom.name", projectName);
 		System.setProperty("pom.version", projectVersion);
 		System.setProperty("url.prefix", urlPrefix);

 		System.setProperty("preconfig.values", "preconfig-values-sample.properties");
 		System.setProperty("jackson.values", "jackson-values-sample.properties");

		String sourcepath = "src/main/java:src/test/java";
	    String classpath = "target/classes:target/test-classes";
		String docletpath = "target/classes:target/test-classes";
		
		File depDir = new File("target/dependency");
		if(!depDir.exists()) {
			System.err.println("Please run 'mvn dependency:copy-dependencies' before running this!");
			return;
		}
		
		File[] jarFiles = depDir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
				return file.getName().endsWith(".jar");
			}
		});
		
		for(File jarFile:jarFiles) {
			classpath += ":"+jarFile.getAbsolutePath();
			docletpath += ":"+jarFile.getAbsolutePath();
		}
		
		String[] javadocargs = { "-sourcepath", sourcepath,
	                     		//"-J-Xms140m",
	                     		//"-J-Xmx1280m",
	                     		//"-J-XX:PermSize=256M",
	                     		//"-J-XX:MaxPermSize=512M",
	                     		//"-J-Dlogging.level=" + loggingLevel,
	                     		//"-J-Dtarget=" + targetDir,
	                     		//"-J-Dpom.group.id=" + groupId,
	                     		//"-J-Dpom.artifact.id=" + artifactId,
	                     		//"-J-Dpom.name=" + projectName,
	                     		//"-J-Dpom.version=" + projectVersion,
	                     		//"-J-Durl.prefix="+urlPrefix,
	                    		"-doclet",
	                    		"com.pehrs.spring.api.doc.v2.SpringControllerAPIDoclet",
	                    		"-classpath", classpath,
	                    		"-docletpath", docletpath,
	                    		"com.pehrs.json.model"
	                             }; 
	    com.sun.tools.javadoc.Main.execute(javadocargs);
	}
}
