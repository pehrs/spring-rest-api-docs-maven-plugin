package com.pehrs.spring.api.doc.mvn;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.versioning.OverConstrainedVersionException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.pehrs.spring.api.doc.JSONSampleGenerator;

/**
 * Generate Java Code from MySQL database
 * 
 * You need to run: mvn clean dependency:copy-dependencies before using this
 * maven plugin
 * 
 * @requiresDependencyResolution runtime
 * @goal docs
 */
public class MVNMojo extends AbstractMojo {

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * The package root to scan for Spring Controllers
	 * 
	 * @parameter
	 * @required
	 */
	private String pkgRoot;
	
	/**
	 * URL prefix for the REST services
	 * 
	 * @parameter
	 */
	private String urlPrefix = "";

	/**
	 * The targets to generate
	 * 
	 * Format: {freemarker template}={FQN of target generated file}
	 * 
	 * <pre>
	 * &lt;typeSamples>
	 *   &lt;param>com.pehrs.MyFileRef="pehrs:/sample/path"&lt;/param>
	 *   &lt;param>com.pehrs.MyNumber=42.3&lt;/param>
	 * &lt;/typeSamples>
	 * </pre>
	 * 
	 * @parameter
	 */
	private List<String> typeSamples;

	/**
	 * The directory for the freemarker templates
	 * 
	 * @parameter
	 */
	private String templateDir = null;

	/**
	 * The target for the generted documentation
	 * 
	 * @parameter
	 */
	private String targetDir = null;

	/** @parameter default-value="${localRepository}" */
	private ArtifactRepository localRepository;

	/**
	 * @parameter expression="${plugin.artifacts}"
	 * @readonly
	 */
	@SuppressWarnings("rawtypes")
	private List pluginDependencies;

	/**
	 * @parameter expression="${plugin.pluginArtifact}"
	 * @readonly
	 */
	private Artifact pluginArtifact;

	/**
	 * The logging level for the code generation
	 * 
	 * @parameter
	 */
	private String loggingLevel = "Info";

	@SuppressWarnings("unchecked")
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("EXECUTE");
		getLog().info("groupId=" + project.getGroupId());
		getLog().info("artifactId=" + project.getArtifactId());
		getLog().info("version=" + project.getVersion());
		getLog().info("srcDir=" + project.getBuild().getSourceDirectory());
		getLog().info("compile.source.roots=" + project.getCompileSourceRoots());
		getLog().info("pkgRoot=" + pkgRoot);

		String localRepoPath = getLocalRepoPath();
		String pluginClasspath = getPluginClasspath(localRepoPath);

		SortedSet<String> pkgs = new TreeSet<String>();
		for (String srcPath : (List<String>)project.getCompileSourceRoots()) {
			File pkgDir = new File(srcPath
					+ "/" + pkgRoot.replace('.', '/'));
			pkgs.addAll(getPackagesFromDirs(pkgDir, pkgRoot, pkgDir));
			pkgs.add(pkgRoot);
		}
		for (String pkg : pkgs) {
			getLog().info("Package: " + pkg);
		}

		// Test to see if we have the ininbo classes in the classpath

		// if (templateDir != null) {
		// getLog().debug("Setting template dir: " + templateDir);
		// System.setProperty("template.dir", templateDir);
		// }
		// System.setProperty("target", targetDir);
		//
		// System.setProperty("pom.group.id", project.getGroupId());
		// System.setProperty("pom.artifact.id", project.getArtifactId());
		// System.setProperty("pom.name", project.getName());
		// System.setProperty("pom.version", project.getVersion());
		// System.setProperty("logging.level", "Debug");
		// System.setProperty("class.prefix", "");
		// System.setProperty("class.suffix", "");
		// System.setProperty("file.ext", ".html");

		File typeSamplesFile = null;
		if (typeSamples != null) {
			typeSamplesFile = new File(System.getProperty("java.io.tmpdir")
					+ "/" + this.getClass().getName() + "-type-samples-"
					+ System.nanoTime());
			PrintWriter out = null;
			try {
				out = new PrintWriter(new FileWriter(typeSamplesFile));
				for (String typeSample : typeSamples) {
					String[] parts = typeSample.split("=");
					String className = parts[0];
					String sample = parts[1];
					getLog().info(
							"CUSTOM TYPE SAMPLE: " + className + " => "
									+ sample);
					JSONSampleGenerator.setTypeSample(className, sample);
					out.println("" + className + "=" + sample);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				if (out != null) {
					out.close();
				}
			}
		}

		getLog().debug("Setting up compile classpath...");
		StringBuilder classpath = new StringBuilder();

		// Setup the target/classes dir
		classpath.append(this.project.getBuild().getOutputDirectory());

		for (Artifact provided : getProvidedArtifacts(project)) {
			getLog().debug("provided.artifact=" + provided);
			File file = provided.getFile();
			if (file != null) {
				if (classpath.length() > 0) {
					classpath.append(":");
				}
				classpath.append(file.getAbsolutePath());
			} else {
				// Try to find the jar in the local repo anyways...

				// javax.servlet:servlet-api:jar:2.5:provided
				// getLocalRepoPath();
				// javax/servlet/servlet-api/2.5/servlet-api-2.5.jar

				File path = new File(getLocalRepoPath() + "/"
						+ provided.getGroupId().replace('.', '/') + "/"
						+ provided.getArtifactId() + "/"
						+ provided.getVersion() + "/"
						+ provided.getArtifactId() + "-"
						+ provided.getVersion() + "." + provided.getType());
				getLog().debug("path=" + path);
				if (classpath.length() > 0) {
					classpath.append(":");
				}
				classpath.append(path.getAbsolutePath());
			}

		}

		try {
			@SuppressWarnings("rawtypes")
			List classPathElements = project.getRuntimeClasspathElements();
			// List classPathElements = project.getCompileClasspathElements();

			URL[] runtimeUrls = new URL[classPathElements.size()];
			for (int i = 0; i < classPathElements.size(); i++) {
				String element = (String) classPathElements.get(i);
				File file = new File(element);
				runtimeUrls[i] = file.toURI().toURL();
				getLog().debug("CLASSPATH URL=" + runtimeUrls[i]);

				if (classpath.length() > 0) {
					classpath.append(":");
				}
				classpath.append(file.getAbsolutePath());
			}
			// URLClassLoader newLoader = new URLClassLoader(runtimeUrls,
			// Thread.currentThread().getContextClassLoader());
			// Thread.currentThread().setContextClassLoader(newLoader);

		} catch (MalformedURLException e) {
			getLog().error(e);
		} catch (DependencyResolutionRequiredException e) {
			getLog().error(e);
		}

		String theClasspath = classpath.toString();
		runJavaDoc((List<String>)project.getCompileSourceRoots(), 
				pkgs, theClasspath,
				pluginClasspath, typeSamplesFile);
	}

	private String getLocalRepoPath() {
		String localRepPath = null;
		try {
			URI localRepUri = new URI("" + localRepository.getUrl());
			localRepPath = localRepUri.getSchemeSpecificPart().replace("///",
					"/");
			getLog().debug("localRepository.url=" + localRepPath);
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		if (localRepPath == null) {
			throw new RuntimeException("Cannot find local repository "
					+ localRepository.getUrl());
		}
		return localRepPath;
	}

	private SortedSet<String> getPackagesFromDirs(File pkgDir, String pkgName,
			File subDir) {
		SortedSet<String> result = new TreeSet<String>();
		File[] files = subDir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}

		});
		for (File file : files) {
			String pkg = file.getAbsolutePath()
					.substring(pkgDir.getAbsolutePath().length())
					.replace('/', '.');
			result.add(pkgName + pkg);
			SortedSet<String> subDirs = getPackagesFromDirs(pkgDir, pkgName,
					file);
			result.addAll(subDirs);
		}
		return result;
	}

	private void runJavaDoc(List<String> srcPath, Set<String> pkgs, String classpath,
			String docletpath, File typeSamplesFile) {
		// javadoc
		// -J-Dtype.samples=/media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../etc/typeSamples.conf
		// -J-Dtemplate.dir=/media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../templates/
		// -J-Dtarget=sample-api
		// -J-Dpom.group.id=com.ininbo
		// -J-Dpom.artifact.id=ininbo-core -J-Dpom.name=InInBo Backend
		// -J-Dpom.version=1.0.0-SNAPSHOT
		// -doclet com.pehrs.spring.api.doc.APIDoclet
		// -classpath
		// /media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../target/classes:/media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../target/lib/slf4j-log4j12-1.6.6.jar:/media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../target/lib/jackson-core-2.0.5.jar:/media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../target/lib/log4j-1.2.17.jar:/media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../target/lib/tools.jar:/media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../target/lib/freemarker-2.3.19.jar:/media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../target/lib/slf4j-api-1.6.6.jar:/media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../target/lib/jackson-databind-2.0.5.jar:/media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../target/lib/jackson-annotations-2.0.5.jar:/media/DEVELOPMENT/ininbo/src/ws/iibser/target/classes
		// -docletpath
		// /media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../target/classes:/media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../target/lib/slf4j-log4j12-1.6.6.jar:/media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../target/lib/jackson-core-2.0.5.jar:/media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../target/lib/log4j-1.2.17.jar:/media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../target/lib/tools.jar:/media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../target/lib/freemarker-2.3.19.jar:/media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../target/lib/slf4j-api-1.6.6.jar:/media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../target/lib/jackson-databind-2.0.5.jar:/media/DEVELOPMENT/pehrs.com/src/spring-rest-api-docs-maven-plugin/bin/../target/lib/jackson-annotations-2.0.5.jar:/media/DEVELOPMENT/ininbo/src/ws/iibser/target/classes
		// -sourcepath /media/DEVELOPMENT/ininbo/src/ws/iibser/src/main/java
		// com.ininbo.ws.rest com.ininbo.ws.rest.util
		// com.ininbo.ws.rest.controller
		// com.ininbo.ws.rest.controller.file
		// com.ininbo.ws.rest.controller.core
		// com.ininbo.ws.rest.controller.content

		for (Object pluginObj : project.getPluginArtifacts()) {
			DefaultArtifact plugin = (DefaultArtifact) pluginObj;
			try {
				getLog().debug(
						"plugin=" + plugin.getGroupId() + ":"
								+ plugin.getArtifactId());
				getLog().debug(
						" plugin.selectedVersion="
								+ plugin.getSelectedVersion());
				if (plugin.getGroupId().equals("com.pehrs")
						&& plugin.getArtifactId().equals(
								"spring-rest-api-docs-maven-plugin")) {

					ArtifactFilter filter = plugin.getDependencyFilter();
					getLog().debug("  filter=" + filter);

				}
			} catch (OverConstrainedVersionException e) {
				e.printStackTrace();
			}
		}

		List<String> cmd = new ArrayList<String>();
		File javaHome = new File(System.getProperty("java.home"));

		// Check if jre and not jdk
		File javadocFile = new File(javaHome.getAbsolutePath() + "/bin/javadoc"
				+ (isWindows() ? ".exe" : ""));
		if (!javadocFile.exists()) {
			// Check if JRE within JDK
			javaHome = javaHome.getParentFile();
			javadocFile = new File(javaHome.getAbsolutePath() + "/bin/javadoc"
					+ (isWindows() ? ".exe" : ""));
			if (!javadocFile.exists()) {
				throw new RuntimeException("You need to run this within a JDK");
			}
		}

		String[] env = { "CLASSPATH=" + classpath, };

		cmd.add(javadocFile.getAbsolutePath());
		if (templateDir != null) {
			cmd.add("-J-Dtemplate.dir=" + templateDir);
		}
		if (typeSamplesFile != null) {
			cmd.add("-J-Dtype.samples=" + typeSamplesFile.getAbsolutePath());
		}

		// FIXME: take these as optional in parameters
		cmd.add("-J-Xms140m");
		cmd.add("-J-Xmx1280m");
		cmd.add("-J-XX:PermSize=256M");
		cmd.add("-J-XX:MaxPermSize=512M");

		cmd.add("-J-Dlogging.level=" + loggingLevel);
		cmd.add("-J-Dtarget=" + targetDir);
		cmd.add("-J-Dpom.group.id=" + project.getGroupId());
		cmd.add("-J-Dpom.artifact.id=" + project.getArtifactId());
		cmd.add("-J-Dpom.name=" + project.getName());
		cmd.add("-J-Dpom.version=" + project.getVersion());
		cmd.add("-J-Durl.prefix="+urlPrefix);
		cmd.add("-sourcepath");
		
		StringBuilder srcp = new StringBuilder();
		for(String sp:srcPath) {
			if(srcp.length()>0) {
				srcp.append(":");
			}
			srcp.append(sp);
		}		
		cmd.add(srcp.toString());
		cmd.add("-doclet");
		cmd.add("com.pehrs.spring.api.doc.APIDoclet");
		cmd.add("-classpath");
		cmd.add(classpath);
		cmd.add("-docletpath");
		cmd.add(docletpath);
		cmd.addAll(pkgs);

		getLog().debug("CMD: " + cmd);

		String[] cmdarray = cmd.toArray(new String[0]);

		// com.sun.tools.javadoc.Main.execute(javadocargs);
		Runtime rt = Runtime.getRuntime();
		try {
			Process process = rt.exec(cmdarray, env);

			ThreadedStreamReader stdin = new ThreadedStreamReader(
					process.getInputStream(), new PrintWriter(System.err));
			ThreadedStreamReader stderr = new ThreadedStreamReader(
					process.getErrorStream(), new PrintWriter(System.err));
			stderr.start();
			stdin.start();

			int res = process.waitFor();
			if (res == 0) {
				getLog().info("javadoc done");
			} else {
				getLog().warn("javadoc returned " + res);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		// windows
		return (os.indexOf("win") >= 0);

	}

	private String getPluginClasspath(String repositoryRootDir)
			throws MojoExecutionException {

		getLog().debug("plugin.artifact=" + pluginArtifact);

		StringBuilder out = new StringBuilder();

		String path = localRepository.pathOf(pluginArtifact);
		getLog().debug("   path=" + path);
		out.append(repositoryRootDir + "/" + path);

		for (Object dep : this.pluginDependencies) {
			DefaultArtifact artifact = (DefaultArtifact) dep;
			getLog().debug(
					"plugin.dependency=" + artifact.getGroupId() + ":"
							+ artifact.getArtifactId() + ":"
							+ artifact.getVersion());

			// RepositoryMetadata metadata = new ArtifactRepositoryMetadata(
			// artifact);

			path = localRepository.pathOf(artifact);
			getLog().debug("   path=" + path);

			out.append(":").append(repositoryRootDir + "/" + path);

		}

		return out.toString();
	}

	private Collection<Artifact> getProvidedArtifacts(MavenProject project) {
		Set dependencyArtifacts = project.getDependencyArtifacts();
		List<Artifact> provided = new ArrayList<Artifact>();

		copyScopedDependenciesToTarget(dependencyArtifacts, provided,
				"provided");
		return provided;
	}

	private void copyScopedDependenciesToTarget(Set dependencyArtifacts,
			List<Artifact> targetArtifacts, String scope) {
		for (Object dependencyArtifact : dependencyArtifacts) {
			Artifact artifact = (Artifact) dependencyArtifact;

			if (artifact.getScope().equals(scope)) {
				targetArtifacts.add(artifact);
			}
		}
	}
}
