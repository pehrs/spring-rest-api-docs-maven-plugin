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
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.pehrs.spring.api.doc.LogUtils;
import com.pehrs.spring.api.doc.SpringRESTAPIDoclet;
import com.pehrs.spring.api.doc.jackson.JacksonTypeModule;

/**
 * Generate HTML documentation from Spring MVC REST Controller.
 * This Mojo kicks of javadoc and runs the APIDoclet 
 *  
 * @requiresDependencyResolution runtime
 * @goal docs
 */
public class MVNMojo extends AbstractMojo {

	private static final String PROVIDED_SCOPE = "provided";

	private static final String SYSARG_OS_NAME = "os.name";

	private static final String SYSARG_JAVA_HOME = "java.home";

	private static final String DEFAULT_LOGGING_LEVEL = "Info";

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
	 * Regex pattern for classes to exclude
	 * 
	 * @parameter
	 */
	private String excludePattern = "";	
	

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
	private String loggingLevel = DEFAULT_LOGGING_LEVEL;

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

		SortedSet<String> pkgs = getPackages();


		File typeSamplesFile;
		try {
			typeSamplesFile = getTypeSamplesFile();
		} catch (IOException e) {
			throw new MojoFailureException("Could not create type samples: "+e.getMessage());
		}

		getLog().debug("Setting up compile classpath...");
		
		String theClasspath = buildClasspath();
		runJavaDoc((List<String>)project.getCompileSourceRoots(), 
				pkgs, theClasspath,
				pluginClasspath, typeSamplesFile);

		// Remove the temporary samples file
		if(typeSamplesFile!=null) {
			typeSamplesFile.delete();
		}
	}

	private String buildClasspath() {
		StringBuilder classpath = new StringBuilder();
		// Setup the target/classes dir
		classpath.append(this.project.getBuild().getOutputDirectory());

		buildClasspath4ProvidedArtifacts(classpath);

		buildClasspath4Runtime(classpath);

		return classpath.toString();
	}

	private void buildClasspath4Runtime(StringBuilder classpath) {
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
	}

	private void buildClasspath4ProvidedArtifacts(StringBuilder classpath) {
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
	}

	private File getTypeSamplesFile() throws IOException {
		File typeSamplesFile = null;
		if (typeSamples != null) {
			typeSamplesFile = File.createTempFile(this.getClass().getName() + "-type-samples-", ".cfg");
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
					// JSONSampleGenerator.setTypeSample(className, sample);
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
		return typeSamplesFile;
	}

	@SuppressWarnings("unchecked")
	private SortedSet<String> getPackages() {
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
		return pkgs;
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
		// -J-Dpom.group.id=com.pehrs
		// -J-Dpom.artifact.id=pehrs-core
		// -J-Dpom.name=Sample Backend
		// -J-Dpom.version=1.0.0-SNAPSHOT
		// -doclet com.pehrs.spring.api.doc.APIDoclet
		// -classpath
		// /media/DEVELOPMENT/.../classes
		// -docletpath
		// /media/DEVELOPMENT/.../classes
		// -sourcepath /path/to/src/main/java
		// com.pehrs.sample.ws.rest 
		// com.pehrs.sample.ws.rest.controller

//		for (Object pluginObj : project.getPluginArtifacts()) {
//			DefaultArtifact plugin = (DefaultArtifact) pluginObj;
//			try {
//				getLog().debug(
//						"plugin=" + plugin.getGroupId() + ":"
//								+ plugin.getArtifactId());
//				getLog().debug(
//						" plugin.selectedVersion="
//								+ plugin.getSelectedVersion());
//				if (plugin.getGroupId().equals("com.pehrs")
//						&& plugin.getArtifactId().equals(
//								"spring-rest-api-docs-maven-plugin")) {
//
//					ArtifactFilter filter = plugin.getDependencyFilter();
//					getLog().debug("  filter=" + filter);
//
//				}
//			} catch (OverConstrainedVersionException e) {
//				e.printStackTrace();
//			}
//		}

		// 
		// Build command line to call javadoc
		// 
		String[] cmdarray = buildJavadocCommandLine(srcPath, pkgs, classpath,
				docletpath, typeSamplesFile);

		// Execute the javadoc command
		Runtime rt = Runtime.getRuntime();
		try {
			String[] env = { "CLASSPATH=" + classpath, };
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

	private String[] buildJavadocCommandLine(List<String> srcPath, Set<String> pkgs,
			String classpath, String docletpath, File typeSamplesFile) {
		
		// Get the path for the javadoc program
		File javadocFile = getJavaDocPath();

		List<String> cmd = new ArrayList<String>();
		cmd.add(javadocFile.getAbsolutePath());
		if (templateDir != null) {
			cmd.add("-J-D"+SpringRESTAPIDoclet.SYSARG_TEMPLATE_DIR+"=" + templateDir);
		}
		if (typeSamplesFile != null) {
			cmd.add("-J-D"+JacksonTypeModule.SYSARG_JACKSON_VALUES+"=" + typeSamplesFile.getAbsolutePath());
		}

		// FIXME: take these as optional in parameters
		cmd.add("-J-Xms140m");
		cmd.add("-J-Xmx1280m");
		cmd.add("-J-XX:PermSize=256M");
		cmd.add("-J-XX:MaxPermSize=512M");

		cmd.add("-J-D"+LogUtils.SYSARG_LOGGING_LEVEL+"=" + loggingLevel);
		cmd.add("-J-D"+SpringRESTAPIDoclet.SYSARG_TARGET+"=" + targetDir);
		cmd.add("-J-D"+SpringRESTAPIDoclet.SYSARG_POM_GROUP_ID+"=" + project.getGroupId());
		cmd.add("-J-D"+SpringRESTAPIDoclet.SYSARG_POM_ARTIFACT_ID+"=" + project.getArtifactId());
		cmd.add("-J-D"+SpringRESTAPIDoclet.SYSARG_POM_NAME+"=" + project.getName());
		cmd.add("-J-D"+SpringRESTAPIDoclet.SYSARG_POM_VERSION+"=" + project.getVersion());
		cmd.add("-J-D"+SpringRESTAPIDoclet.SYSARG_URL_PREFIX+"="+urlPrefix);
		cmd.add("-J-D"+SpringRESTAPIDoclet.SYSARG_EXCLUDE_PATTERN+"="+excludePattern);
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
		cmd.add(SpringRESTAPIDoclet.class.getName());
		cmd.add("-classpath");
		cmd.add(classpath);
		cmd.add("-docletpath");
		cmd.add(docletpath);
		cmd.addAll(pkgs);

		getLog().debug("CMD: " + cmd);

		String[] cmdarray = cmd.toArray(new String[0]);
		return cmdarray;
	}

	private File getJavaDocPath() {
		File javaHome = new File(System.getProperty(SYSARG_JAVA_HOME));
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
		return javadocFile;
	}

	private static boolean isWindows() {
		String os = System.getProperty(SYSARG_OS_NAME).toLowerCase();
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
		@SuppressWarnings("unchecked")
		Set<Artifact> dependencyArtifacts = (Set<Artifact>)project.getDependencyArtifacts();
		List<Artifact> provided = new ArrayList<Artifact>();

		copyScopedDependenciesToTarget(dependencyArtifacts, provided,
				PROVIDED_SCOPE);
		return provided;
	}

	private void copyScopedDependenciesToTarget(Set<Artifact> dependencyArtifacts,
			List<Artifact> targetArtifacts, String scope) {
		for (Artifact artifact : dependencyArtifacts) {
			if (artifact.getScope().equals(scope)) {
				targetArtifacts.add(artifact);
			}
		}
	}
}
