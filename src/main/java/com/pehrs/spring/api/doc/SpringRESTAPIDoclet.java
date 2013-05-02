package com.pehrs.spring.api.doc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.pehrs.spring.api.doc.json.JsonUtil;
import com.pehrs.spring.api.doc.model.ControllerDesc;
import com.pehrs.spring.api.doc.model.MethodDesc;
import com.pehrs.spring.api.doc.model.ParameterDesc;
import com.pehrs.spring.api.doc.model.PathInfo;
import com.pehrs.spring.api.doc.model.PkgDesc;
import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationDesc.ElementValuePair;
import com.sun.javadoc.AnnotationValue;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.RootDoc;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class SpringRESTAPIDoclet {

	private static final String SYSARG_EXCLUDE_PATTERN = "exclude.pattern";
	private static final String SYSARG_URL_PREFIX = "url.prefix";
	private static final String SYSARG_POM_VERSION = "pom.version";
	private static final String SYSARG_POM_NAME = "pom.name";
	private static final String SYSARG_POM_ARTIFACT_ID = "pom.artifact.id";
	private static final String SYSARG_POM_GROUP_ID = "pom.group.id";
	private static final String SYSARG_TARGET = "target";
	private static final String SYSARG_FILE_EXT = "file.ext";
	private static final String SYSARG_FILE_EXT_DEFAULT = ".html";
	private static final String SYSARG_CLASS_SUFFIX = "class.suffix";
	private static final String SYSARG_CLASS_SUFFIX_DEFAULT = "";
	private static final String SYSARG_CLASS_PREFIX = "class.prefix";
	private static final String SYSARG_CLASS_PREFIX_DEFAULT = "";
	
	private static final String DATAMODEL_POM_VERSION = "pom_version";
	private static final String DATAMODEL_POM_ARTIFACT_ID = "pom_artifact_id";
	private static final String DATAMODEL_POM_GROUP_ID = "pom_group_id";
	private static final String DATAMODEL_CONTROLLERS = "controllers";
	private static final String DATAMODEL_MODEL = "model";
	private static final String DATAMODEL_PACKAGES = "packages";
	private static final String DATAMODEL_PACKAGE_MAP = "packageMap";
	private static final String DATAMODEL_CONTROLLER = "controller";


	private static final String TEMPLATE_RESOURCE_DIR = "/com/pehrs/spring/api/doc/templates";

	private static final String SYSARG_TEMPLATE_DIR = "template.dir";

	private static final String SPRING_CONTROLLER_ANNOTATION_CLASSNAME = "org.springframework.stereotype.Controller";
	private static final String SPRING_PATH_VARIABLE_ANNOTATION_CLASSNAME = "org.springframework.web.bind.annotation.PathVariable";
	private static final String SPRING_REQUEST_ANNOTATION_CLASSNAME = "org.springframework.web.bind.annotation.RequestParam";
	private static final String SPRING_REQUEST_MAPPING_ANNOTATION_CLASSNAME = "org.springframework.web.bind.annotation.RequestMapping";
	private static final String SPRING_REQUEST_BODY_ANNOTATION_CLASSNAME = "org.springframework.web.bind.annotation.RequestBody";

	private static final String REQUEST_METHOD_GET = "org.springframework.web.bind.annotation.RequestMethod.GET";
	private static final String REQUEST_METHOD_TRACE = "org.springframework.web.bind.annotation.RequestMethod.TRACE";
	private static final String REQUEST_METHOD_OPTIONS = "org.springframework.web.bind.annotation.RequestMethod.OPTIONS";
	private static final String REQUEST_METHOD_HEAD = "org.springframework.web.bind.annotation.RequestMethod.HEAD";
	private static final String REQUEST_METHOD_PUT = "org.springframework.web.bind.annotation.RequestMethod.PUT";
	private static final String REQUEST_METHOD_DELETE = "org.springframework.web.bind.annotation.RequestMethod.DELETE";
	private static final String REQUEST_METHOD_POST = "org.springframework.web.bind.annotation.RequestMethod.POST";

	private static final String VALUE_ELEMENT_NAME = "value";
	private static final String DEFAULT_VALUE_ELEMENT_NAME = "defaultValue";
	private static final String REQUIRED_ELEMENT_NAME = "required";

	private final static String CONTROLLER_TEMPLATE_FILE = "controller.ftl";
	
	private final static String API_TEMPLATE_FILE = "api.ftl";

	private static final char SLASH = '/';
	private static final char DOT = '.';

	static Logger log = LoggerFactory.getLogger(SpringRESTAPIDoclet.class);


	static String urlPrefix = System.getProperty(SYSARG_URL_PREFIX, "");

	static String excludePatternRegex = System.getProperty(SYSARG_EXCLUDE_PATTERN);
	static Pattern excludePattern = null;

	static Map<String, ControllerDesc> model = new HashMap<String, ControllerDesc>();
	static Map<String, PkgDesc> packages = new HashMap<String, PkgDesc>();

	public static boolean start(RootDoc root) throws JsonGenerationException,
			JsonMappingException, IOException {

		LogUtils.initLogging();

		ClassDoc[] classes = root.classes();
		for (ClassDoc classDoc : classes) {

			if (shouldWeProcess(classDoc)) {
				if (getControllerAnnotation(classDoc) != null) {
					processClassDoc(classDoc);
				}
			}

		}

		log.debug("============================");
		log.debug("" + packages);
		log.debug("============================");

		// Generate result
		try {
			File targetDir = new File(System.getProperty(SYSARG_TARGET));
			targetDir.mkdirs();

			Map<String, Object> datamodel = new HashMap<String, Object>();
			datamodel.put(DATAMODEL_PACKAGE_MAP, packages);
			datamodel.put(DATAMODEL_MODEL, model);
			datamodel.put(DATAMODEL_POM_GROUP_ID, System.getProperty(SYSARG_POM_GROUP_ID, ""));
			datamodel.put(DATAMODEL_POM_ARTIFACT_ID,
					System.getProperty(SYSARG_POM_ARTIFACT_ID, ""));
			datamodel.put("pom_name", System.getProperty(SYSARG_POM_NAME, ""));
			datamodel.put(DATAMODEL_POM_VERSION, System.getProperty(SYSARG_POM_VERSION, ""));
			fmGenerateAPIHtml(datamodel, targetDir);

		} finally {

		}

		return true;
	}

	private static void processClassDoc(ClassDoc classDoc)
			throws JsonGenerationException, JsonMappingException, IOException {

		// AnnotationDesc reqMapDoc = getReqeustMappingAnnotation(classDoc);
		log.info("\n  from class " + classDoc.qualifiedName());

		ControllerDesc controller = createControllerDesc(classDoc);
		getOrCreatePackageDoc(classDoc, controller);
		// xml.append(ODTGenerator.odfBody(classDoc.qualifiedName()));

		String comment = classDoc.getRawCommentText();
		log.debug("  comment=" + comment);
		controller.setComment(comment);

		String urlRoot = "";

		AnnotationDesc controllerReqMappingDoc = getReqeustMappingAnnotation(classDoc);
		log.debug("  requestmapping:" + controllerReqMappingDoc);
		if (controllerReqMappingDoc != null) {
			urlRoot = getStrippedElementValue(controllerReqMappingDoc, VALUE_ELEMENT_NAME);
		}

		log.debug("  url.root=" + urlRoot);
		controller.setUrlRoot(urlPrefix + urlRoot);

		for (MethodDoc methodDoc : classDoc.methods()) {
			processMethodDoc(classDoc, controller, urlRoot, methodDoc);
		}
	}

	private static void processMethodDoc(ClassDoc classDoc,
			ControllerDesc controller, String urlRoot, MethodDoc methodDoc)
			throws JsonGenerationException, JsonMappingException, IOException {
		AnnotationDesc methodReqMapDoc = getReqeustMappingAnnotation(methodDoc);
		if (methodReqMapDoc != null) {
			processRequestMappingAnnotation(classDoc, controller, urlRoot,
					methodDoc, methodReqMapDoc);
		}
	}

	private static void processRequestMappingAnnotation(ClassDoc classDoc,
			ControllerDesc controller, String urlRoot, MethodDoc methodDoc,
			AnnotationDesc methodReqMapDoc) throws JsonGenerationException,
			JsonMappingException, IOException {
		AnnotationValue reqMethodValue = getElementAnnotationValue(
				methodReqMapDoc, "method");
		if (reqMethodValue != null) {
			Object[] reqMethods = (Object[]) reqMethodValue.value();
			for (Object springReqMethod : reqMethods) {
				processRequestMethod(classDoc, controller, urlRoot, methodDoc,
						methodReqMapDoc, springReqMethod);
			}
		}
	}

	private static void processRequestMethod(ClassDoc classDoc,
			ControllerDesc controller, String urlRoot, MethodDoc methodDoc,
			AnnotationDesc methodReqMapDoc, Object springReqMethod)
			throws JsonGenerationException, JsonMappingException, IOException {

		String reqMethod = getHttpMethodName("" + springReqMethod);
		String methodUrl = getStrippedElementValue(methodReqMapDoc, VALUE_ELEMENT_NAME);
		log.debug("methodUrl=" + methodUrl);
		if (methodUrl == null) {
			methodUrl = "";
		}

		log.info("    " + reqMethod + " " + urlRoot + methodUrl + " => "
				+ methodDoc.name() + "()");
		MethodDesc method = new MethodDesc();
		controller.addMethod(method);
		method.setName(methodDoc.name());
		method.setRequestMappingUrl(methodUrl);
		method.setRequestMappingMethod(reqMethod);
		method.setAnnotations(methodDoc.annotations());

		// Get Parameter Types
		Method classMethod = getMethod(classDoc, methodDoc);
		Parameter[] parameterDocs = methodDoc.parameters();

		@SuppressWarnings("rawtypes")
		Class methodReturnType = classMethod.getReturnType();


		String methodReturnTypeJson = JsonUtil
				.createJsonSample(methodReturnType);
		log.debug("      JSON Response Sample:\n" + methodReturnTypeJson);
		method.setResponseJSONSample(methodReturnTypeJson);
		method.setCommentText(methodDoc.getRawCommentText());

		for (Parameter javaDocParameter : parameterDocs) {
			if (javaDocParameter.typeName().equals("HttpServletRequest")
					|| javaDocParameter.typeName()
							.equals("HttpServletResponse")) {
				// Ignore the HTTP Request/response arguments for the method
				continue;
			}

			addParameter(methodDoc, method, classMethod, javaDocParameter,
					parameterDocs);

		}
	}

	private static void addParameter(Doc methodDoc, MethodDesc method,
			Method classMethod, Parameter javaDocParameter,
			Parameter[] parameterDocs) throws JsonGenerationException,
			JsonMappingException, IOException {
		@SuppressWarnings("rawtypes")
		Class parameterClass = getParameterClass(classMethod, javaDocParameter,
				parameterDocs);
		log.debug("    parameter.class=" + parameterClass.getName());

		ParameterDesc parameter = new ParameterDesc();
		method.addParameter(parameter);
		parameter.setName(javaDocParameter.name());
		parameter.setTypeName(javaDocParameter.typeName());

		AnnotationDesc pathVariable = getPathVariableAnnotation(javaDocParameter);
		if (pathVariable != null) {
			parameter.setPathVariable(true);
			parameter.setPathVariableValue(getElementValue(pathVariable,
					VALUE_ELEMENT_NAME));
		}

		processRequestParamAnnotation(javaDocParameter, parameter);

		processRequestBodyAnnotation(method, javaDocParameter, parameterClass,
				parameter);
		
	}

	@SuppressWarnings("rawtypes")
	private static void processRequestBodyAnnotation(MethodDesc method,
			Parameter javaDocParameter, Class parameterClass,
			ParameterDesc parameter) throws JsonGenerationException,
			JsonMappingException, IOException {
		AnnotationDesc requestBodyAnnotation = getRequestBodyAnnotation(javaDocParameter);
		boolean parameterIsBodyReq = requestBodyAnnotation != null;
		log.debug("    parameter: " + javaDocParameter.typeName() + " "
				+ javaDocParameter.name() + " "
				+ (parameterIsBodyReq ? " BODY REQUEST" : ""));
		if (parameterIsBodyReq) {
			
			String json = JsonUtil.createJsonSample(parameterClass);
			log.debug("      JSON Request Sample:\n" + json);

			method.setRequestJSONSample(json);
			parameter.setRequestBody(true);
			parameter.setRequestBodyJSONSample(json);
		}
	}

	private static void processRequestParamAnnotation(
			Parameter javaDocParameter, ParameterDesc parameter) {
		AnnotationDesc reqParam = getRequestParamAnnotation(javaDocParameter);
		if (reqParam != null) {
		
			boolean required = false;
			String requiredStr = getElementValue(reqParam, REQUIRED_ELEMENT_NAME);
			String reqP_defaultValue = getElementValue(reqParam, DEFAULT_VALUE_ELEMENT_NAME);
			String reqP_value = getElementValue(reqParam, VALUE_ELEMENT_NAME);
			if (reqP_value == null || reqP_value.length() == 0) {
				reqP_value = javaDocParameter.name();
			}
			if (requiredStr == null) {
				required = false;
			} else {
				required = Boolean.parseBoolean(requiredStr);
			}
			parameter.setRequestParam(true);
			parameter.setRequestParamRequired(required);
			parameter.setRequestParamDefaultValue(reqP_defaultValue);
			parameter.setRequestParamValue(reqP_value);
			log.debug("    request parameter: " + javaDocParameter.typeName()
					+ " " + javaDocParameter.name() + " "
					+ (reqParam == null ? "" : " required=" + required));
		}
	}

	private static int getParameterIndex(Parameter[] parameterDocs,
			Parameter javaDocParameter) {
		int parameterIndex = 0;
		for (; parameterIndex < parameterDocs.length; parameterIndex++) {
			if (parameterDocs[parameterIndex] == javaDocParameter) {
				break;
			}
		}
		return parameterIndex;
	}

	private static void getOrCreatePackageDoc(ClassDoc classDoc,
			ControllerDesc controller) {
		PackageDoc pkgDoc = classDoc.containingPackage();
		String pkgName = pkgDoc.name();
		PkgDesc pkg = packages.get(pkgName);
		if (pkg == null) {
			pkg = new PkgDesc();
			pkg.setName(pkgName);
			packages.put(pkgName, pkg);
		}
		pkg.addController(controller);
	}

	private static ControllerDesc createControllerDesc(ClassDoc classDoc) {
		ControllerDesc controller = new ControllerDesc();
		controller.setName(classDoc.name());
		controller.setPkgName(classDoc.containingPackage().name());
		model.put(classDoc.qualifiedName(), controller);
		return controller;
	}

	private static boolean shouldWeProcess(ClassDoc classDoc) {

		if (excludePatternRegex == null || excludePatternRegex.length() == 0) {
			return true;
		}

		if (excludePattern == null) {
			excludePattern = Pattern.compile(excludePatternRegex);
		}

		String fqcn = classDoc.containingPackage().name() + "."
				+ classDoc.name();
		log.debug("FQCN: " + fqcn);
		Matcher matcher = excludePattern.matcher(fqcn);

		return !matcher.matches();
	}

	private static String getHttpMethodName(String reqMethod) {
		if (reqMethod
				.equals(REQUEST_METHOD_GET)) {
			return "GET";
		}
		if (reqMethod
				.equals(REQUEST_METHOD_POST)) {
			return "POST";
		}
		if (reqMethod
				.equals(REQUEST_METHOD_DELETE)) {
			return "DELETE";
		}
		if (reqMethod
				.equals(REQUEST_METHOD_PUT)) {
			return "PUT";
		}
		if (reqMethod
				.equals(REQUEST_METHOD_HEAD)) {
			return "HEAD";
		}
		if (reqMethod
				.equals(REQUEST_METHOD_OPTIONS)) {
			return "OPTIONS";
		}
		if (reqMethod
				.equals(REQUEST_METHOD_TRACE)) {
			return "TRACE";
		}
		return "GET";
	}

	@SuppressWarnings("rawtypes")
	private static Class getParameterClass(Method method, Parameter parameter,
			Parameter[] parameterDocs) {

		int i = getParameterIndex(parameterDocs, parameter);
		return method.getParameterTypes()[i];
	}

	@SuppressWarnings("rawtypes")
	private static Method getMethod(ClassDoc classDoc, MethodDoc methodDoc) {
		log.debug("getMethod(" + classDoc.name() + ", " + methodDoc.name()
				+ ")");
		try {
			Class klass = Class.forName(classDoc.qualifiedName());

			for (Method method : klass.getMethods()) {
				log.debug("getMethod() method.name=" + method.getName()
						+ " methodDoc.name=" + methodDoc.name());
				if (methodDoc.name().equals(method.getName())) {
					if (method.getParameterTypes().length == methodDoc
							.parameters().length) {
						return method;
					}
				}
			}
			log.warn("Class " + classDoc.name() + " method for '"
					+ methodDoc.name() + "' not found");
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String getElementValue(AnnotationDesc desc, String key) {
		AnnotationValue val = getElementAnnotationValue(desc, key);
		if (val != null)
			return "" + val;
		return null;
	}

	private static AnnotationValue getElementAnnotationValue(
			AnnotationDesc desc, String key) {
		if (desc == null) {
			return null;
		}
		for (ElementValuePair val : desc.elementValues()) {
			String name = val.element().name();
			if (key.equals(name)) {
				return val.value();
			}
		}
		return null;
	}

	private static String getStrippedElementValue(AnnotationDesc desc,
			String key) {
		log.debug("getStrippedElementValue() desc=" + desc + ", key=" + key);

		String tmp = getElementValue(desc, key);
		log.debug("  tmp='" + tmp + "'");
		if (tmp == null) {
			return null;
		}
		return tmp.substring(1, tmp.length() - 1);
	}

	private static AnnotationDesc getPathVariableAnnotation(Parameter doc) {
		for (AnnotationDesc annotation : doc.annotations()) {
			if (SPRING_PATH_VARIABLE_ANNOTATION_CLASSNAME
					.equals(annotation.annotationType().qualifiedName())) {
				return annotation;
			}
		}
		return null;
	}

	private static AnnotationDesc getRequestParamAnnotation(Parameter doc) {
		for (AnnotationDesc annotation : doc.annotations()) {
			if (SPRING_REQUEST_ANNOTATION_CLASSNAME
					.equals(annotation.annotationType().qualifiedName())) {
				return annotation;
			}
		}
		return null;
	}

	private static AnnotationDesc getRequestBodyAnnotation(Parameter doc) {
		for (AnnotationDesc annotation : doc.annotations()) {
			if (SPRING_REQUEST_BODY_ANNOTATION_CLASSNAME
					.equals(annotation.annotationType().qualifiedName())) {
				return annotation;
			}
		}
		return null;
	}

	private static AnnotationDesc getReqeustMappingAnnotation(
			ProgramElementDoc doc) {
		for (AnnotationDesc annotation : doc.annotations()) {
			if (SPRING_REQUEST_MAPPING_ANNOTATION_CLASSNAME
					.equals(annotation.annotationType().qualifiedName())) {
				return annotation;
			}
		}
		return null;
	}

	private static AnnotationDesc getControllerAnnotation(ProgramElementDoc doc) {
		for (AnnotationDesc annotation : doc.annotations()) {
			if (SPRING_CONTROLLER_ANNOTATION_CLASSNAME.equals(annotation
					.annotationType().qualifiedName())) {
				return annotation;
			}
		}
		return null;
	}

	static Configuration cfg = new Configuration();
	static {
		String templateDir = System.getProperty(SYSARG_TEMPLATE_DIR);
		setTemplateDir(templateDir);
	}

	public static void setTemplateDir(String dir) {
		try {
			String templateDir = dir;
			if (templateDir == null) {
				TemplateLoader loader = new ClassTemplateLoader(
						SpringRESTAPIDoclet.class,
						TEMPLATE_RESOURCE_DIR);
				cfg.setTemplateLoader(loader);
			} else {
				cfg.setTemplateLoader(new FileTemplateLoader(new File(
						templateDir)));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	static void fmGenerateAPIHtml4Controller(Map<String, Object> datamodel,
			File targetDir) {

		String classPrefix = System.getProperty(SYSARG_CLASS_PREFIX, SYSARG_CLASS_PREFIX_DEFAULT);
		String classSuffix = System.getProperty(SYSARG_CLASS_SUFFIX, SYSARG_CLASS_SUFFIX_DEFAULT);
		String fileExt = System.getProperty(SYSARG_FILE_EXT, SYSARG_FILE_EXT_DEFAULT);

		FileWriter out = null;
		try {
			ControllerDesc controller = (ControllerDesc) datamodel
					.get(DATAMODEL_CONTROLLER);

			String pkgPath = controller.getPkgName().replace(DOT, SLASH);

			File pkgDir = new File(targetDir.getAbsolutePath() + "/" + pkgPath);
			pkgDir.mkdirs();

			String path = targetDir.getAbsolutePath() + "/" + pkgPath + "/"
					+ classPrefix + controller.getName() + classSuffix
					+ fileExt;
			log.debug("Generating " + path);
			out = new FileWriter(path);

			Template tpl = cfg.getTemplate(CONTROLLER_TEMPLATE_FILE);

			tpl.process(datamodel, out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		} finally {

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	private static void fmGenerateAPIHtml(Map<String, Object> datamodel,
			File targetDir) {

		String fileExt = System.getProperty(SYSARG_FILE_EXT, SYSARG_FILE_EXT_DEFAULT);

		FileWriter out = null;
		try {

			Map<String, PkgDesc> packages = (Map<String, PkgDesc>) datamodel
					.get(DATAMODEL_PACKAGE_MAP);
			TreeSet<PkgDesc> sortedPkgs = new TreeSet<PkgDesc>(
					packages.values());
			datamodel.put(DATAMODEL_PACKAGES, sortedPkgs.iterator());

			Map<String, ControllerDesc> model = (Map<String, ControllerDesc>) datamodel
					.get(DATAMODEL_MODEL);
			TreeSet<ControllerDesc> sortedControllers = new TreeSet<ControllerDesc>(
					model.values());
			datamodel.put(DATAMODEL_CONTROLLERS, sortedControllers.iterator());

			TreeSet<PathInfo> paths = new TreeSet<PathInfo>();
			for (ControllerDesc controller : model.values()) {
				log.info("\nGenerating HTML for Controller "
						+ controller.getName());
				String urlRoot = controller.getUrlRoot();
				for (MethodDesc method : controller.getMethods()) {
					String requestPath = urlRoot
							+ method.getRequestMappingUrl();

					// Create the method id
					String methodId = "api_" + controller.getJSId() + "_"
							+ method.getRequestMappingMethod() + "_"
							+ method.getName();

					PathInfo info = new PathInfo(controller, method,
							requestPath, method.getRequestMappingMethod(),
							controller.getPkgNameAsPath() + "/"
									+ controller.getName() + fileExt, methodId);
					paths.add(info);

					controller.getPathInfos().add(info);

					log.debug("PATH: " + info);
				}
			}

			datamodel.put("paths", paths.iterator());
			log.debug("    paths=" + paths);

			String path = targetDir.getAbsolutePath() + "/"
					+ datamodel.get(DATAMODEL_POM_GROUP_ID) + "."
					+ datamodel.get(DATAMODEL_POM_ARTIFACT_ID) + "-"
					+ datamodel.get(DATAMODEL_POM_VERSION) + fileExt;
			log.debug("Generating " + path);
			out = new FileWriter(path);

			Template tpl = cfg.getTemplate(API_TEMPLATE_FILE);
			tpl.process(datamodel, out);

		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		} finally {

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}

	}

}
