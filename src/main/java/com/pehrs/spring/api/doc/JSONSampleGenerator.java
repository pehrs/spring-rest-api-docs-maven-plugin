package com.pehrs.spring.api.doc;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JSONSampleGenerator {
	// static SimpleDateFormat ISOFMT = new
	// SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SZ");

	static Properties typeSamples = new Properties();

	static {
		String typeSampleFileName = System.getProperty("type.samples");
		if (typeSampleFileName != null) {
			FileReader typeSampleFile = null;
			try {
				typeSampleFile = new FileReader(typeSampleFileName);
				typeSamples.load(typeSampleFile);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					if(typeSampleFile!=null) {
						typeSampleFile.close();
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	public static void setTypeSample(String className, String sample) {
		typeSamples.put(className, sample);
	}

	private final JSONDecorator decorator;

	/**
	 * Default constructor creates an text generator
	 */
	public JSONSampleGenerator() {
		this.decorator = new JSONTextDecorator();
	}

	public JSONSampleGenerator(JSONDecorator decorator) {
		this.decorator = decorator;
	}

	public String generateJSONSample(
			@SuppressWarnings("rawtypes") Class beanClass, Type beanType) {
		return generateJSONSample("", beanClass, beanType);
	}

	public String generateJSONSample(String prefix,
			@SuppressWarnings("rawtypes") Class beanClass, Type beanType) {
		StringBuilder out = new StringBuilder();
		generateJSONSample(out, prefix, beanClass, beanType);
		return out.toString();
	}

	@SuppressWarnings({ "rawtypes" })
	private void generateJSONSample(StringBuilder out, String prefix,
			Class beanClass, Type beanType) {

		if (beanClass == null) {
			return;
		}

		String typeSample = typeSamples.getProperty(beanClass.getName());
		if (typeSample != null) {
			decorator.text(out, prefix,
					typeSample + " /* " + beanClass.getName() + "*/");
			return;
		}

		try {

			// System.out.println(prefix + "CLASS: " + beanClass.getName());
			Type[] typeArgs = null;
			// System.out.println("====================beanType="+beanType.getClass());
			if (beanType instanceof ParameterizedType) {
				try {
					ParameterizedType pt = (ParameterizedType) beanType;
					typeArgs = pt.getActualTypeArguments();
					// System.out.println(prefix + "  CLASS 	ARGS=<"
					// + Arrays.toString(typeArgs) + ">");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (Collection.class.isAssignableFrom(beanClass)
					&& typeArgs != null) {
				// System.out.println("COLLECTON TYPE: " + beanClass);
				decorator.text(out, prefix, "[");
				generateJSONSample(out, prefix + "       ",
						(Class) typeArgs[0], typeArgs[0]);
				decorator.text(out, prefix, "]");
				return;
			} else {

				decorator.classComment(out, prefix, beanClass.getName());
				BeanInfo info = Introspector.getBeanInfo(beanClass);
				// System.out.println(prefix + "{");
				decorator.classHead(out, prefix);
				for (PropertyDescriptor property : info
						.getPropertyDescriptors()) {

					if (!isWantedProperty(property.getName())) {
						continue;
					}

					Method readMethod = property.getReadMethod();
					if (readMethod != null) {
						// JsonIgnore ignore =
						// property.getReadMethod().getAnnotation(JsonIgnore.class);
						for (Annotation annotation : property.getReadMethod()
								.getAnnotations()) {
							if (annotation
									.annotationType()
									.getName()
									.equals("org.codehaus.jackson.annotate.JsonIgnore")) {
								// Jackson 1 ignore
								continue;
							}
							if (annotation
									.annotationType()
									.getName()
									.equals("com.fasterxml.jackson.annotation.JsonIgnore")) {
								// Jackson 2 ignore
								continue;
							}
						}
					}

					Class<?> propertyClass = property.getPropertyType();
					String componentType = null;
					Type propertyType = null;
					if (readMethod != null) {
						propertyType = readMethod.getGenericReturnType();
					} else {
						propertyType = property.getWriteMethod()
								.getParameterTypes()[0];
					}

					StringBuilder embeddedTypeDecl = null;

					if (propertyType instanceof ParameterizedType) {
						ParameterizedType pType = (ParameterizedType) propertyType;
						for (java.lang.reflect.Type typeArgument : pType
								.getActualTypeArguments()) {
							componentType = getName(typeArgument);
							// propertyTypeName += "<" + componentType + ">";
						}

					} else if (propertyType instanceof TypeVariable) {
						// TypeVariable typeVar = (TypeVariable) propertyType;

						if (typeArgs[0] instanceof Class) {
							Class typeClass = (Class) typeArgs[0];
							propertyType = typeClass;
							propertyClass = typeClass;
						} else if (typeArgs[0] instanceof ParameterizedType) {
							ParameterizedType pt = (ParameterizedType) typeArgs[0];
							embeddedTypeDecl = new StringBuilder();
							generateJSONSample(embeddedTypeDecl, prefix
									+ "    ", (Class) pt.getRawType(),
									typeArgs[0]);
						}
					}

					if (isFundamentalType(propertyClass)
							|| propertyClass == Date.class
							|| propertyClass == java.sql.Date.class
							|| propertyClass == Timestamp.class) {

						if (!isJsonIgnore(property)) {

							decorator.fundamentalPropertyType(
									out,
									prefix,
									property.getName(),
									generateSampleData(propertyClass),
									getPropertyComment(propertyClass,
											componentType));
						}
					} else {
						if (isCollection(propertyClass)) {
							decorator.collectionHead(
									out,
									prefix,
									property.getName(),
									getPropertyComment(propertyClass,
											componentType));
							try {
								Class componentClass = Class
										.forName(componentType);
								if (isFundamentalType(componentClass)
										|| componentClass == Date.class
										|| componentClass == java.sql.Date.class
										|| componentClass == Timestamp.class) {
									decorator
											.text(out,
													prefix,
													generateSampleDataArray(componentClass));
								} else {
									generateJSONSample(out, prefix + "    ",
											componentClass,
											componentClass
													.getGenericSuperclass());
								}
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
							decorator.collectionTail(out, prefix);
						} else {
							decorator.propertyHead(out, prefix,
									property.getName());
							if (embeddedTypeDecl != null) {
								decorator.text(out, prefix,
										embeddedTypeDecl.toString());
							} else {
								generateJSONSample(out, prefix + "    ",
										propertyClass, propertyType);
							}
							decorator.propertyTail(out, prefix);
						}
					}
				}
			}
			decorator.classTail(out, prefix);

		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}

	private boolean isJsonIgnore(PropertyDescriptor property) {

		Method readMethod = property.getReadMethod();
		if (readMethod != null) {
			for (Annotation ann : readMethod.getAnnotations()) {
				if (JsonIgnore.class.isAssignableFrom(ann.annotationType())) {
					return true;
				}
			}
		}

		return false;
	}

//	private void print(PropertyDescriptor property) {
//		System.out.println("PropertyDescriptor--------");
//		System.out.println("  DisplayName=" + property.getDisplayName());
//		System.out.println("  Name=" + property.getName());
//		System.out.println("  ShortDescription="
//				+ property.getShortDescription());
//		System.out.println("  PropertyType=" + property.getPropertyType());
//		System.out.println("  ReadMethod=" + property.getReadMethod());
//		System.out.println("  WriteMethod=" + property.getWriteMethod());
//		System.out.println("--------");
//		System.out.flush();
//	}

	@SuppressWarnings("rawtypes")
	private String getPropertyComment(Class propertyClass,
			String componentTypeName) {

		String propertyTypeName = propertyClass.getName();
		if (componentTypeName != null) {
			propertyTypeName += "<" + componentTypeName + ">";
		}

		StringBuilder out = new StringBuilder();
		// out.append(" /* ");
		out.append(propertyTypeName);

		if (componentTypeName != null) {
			try {
				out.append(" ");
				Class componentType = Class.forName(componentTypeName);
				if (componentType.isEnum()) {
					Object[] enumConstants = componentType.getEnumConstants();
					for (int i = 0; i < enumConstants.length; i++) {
						Object ec = enumConstants[i];
						if (i > 0) {
							out.append(", ");
						}
						out.append("" + ec);
					}
				}
				out.append(" ");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (propertyClass.isEnum()) {
			out.append(" ");
			Object[] enumConstants = propertyClass.getEnumConstants();
			for (int i = 0; i < enumConstants.length; i++) {
				Object ec = enumConstants[i];
				if (i > 0) {
					out.append(", ");
				}
				out.append("" + ec);
			}
			out.append(" ");
		}

		// out.append(" */");
		return out.toString();
	}

	@SuppressWarnings("rawtypes")
	private String generateSampleDataArray(Class componentClass) {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < 3; i++) {
			if (i > 0) {
				out.append(", ");
			}
			out.append("" + generateSampleData(componentClass));
		}
		return out.toString();
	}

	private String generateSampleData(Class<?> propertyClass) {

		String typeSample = typeSamples.getProperty(propertyClass.getName());
		if (typeSample != null) {
			return typeSample;
		}

		if (propertyClass == String.class) {
			return "\"STRING\"";
		}

		if (propertyClass == Boolean.class || propertyClass == Boolean.TYPE) {
			return "true";
		}

		if (propertyClass == Integer.TYPE || propertyClass == Integer.class
				|| propertyClass == Long.class || propertyClass == Long.TYPE
				|| propertyClass == Short.class || propertyClass == Short.TYPE
				|| propertyClass == BigInteger.class) {
			return "42";
		}

		if (propertyClass == Double.class || propertyClass == Double.TYPE
				|| propertyClass == Float.class || propertyClass == Float.TYPE
				|| propertyClass == BigDecimal.class) {
			return "42.42";
		}

		if (propertyClass == Date.class || propertyClass == java.sql.Date.class
				|| propertyClass == Timestamp.class) {
			return "\"2012-08-21T10:45:19.000+0000\"";
		}

		if (propertyClass.isEnum()) {

			Object[] consts = propertyClass.getEnumConstants();
			return "\"" + consts[0]+"\"";

		}

		if (propertyClass == Date.class) {
			// return ISOFMT.format(new Date());
			return "" + (new Date()).getTime();
		}

		return "";
	}

	@SuppressWarnings("rawtypes")
	private boolean isCollection(Class propertyClass) {
		return Collection.class.isAssignableFrom(propertyClass);
	}

	private boolean isWantedProperty(String propertyName) {
		if ("class".equals(propertyName)) {
			return false;
		}
		if ("version".equals(propertyName)) {
			return false;
		}
		if ("classLoader".equals(propertyName)) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	private boolean isFundamentalType(Class propertyClass) {

		// propertyClass.isArray();
		// propertyClass.getComponentType();

		if (propertyClass.isEnum()) {
			return true;
		}

		if (propertyClass.isPrimitive())
			return true;

		if (propertyClass == String.class)
			return true;

		if (propertyClass == Integer.class)
			return true;

		if (propertyClass == Long.class)
			return true;

		if (propertyClass == Double.class)
			return true;

		if (propertyClass == Float.class)
			return true;

		if (propertyClass == Short.class)
			return true;

		if (propertyClass == Byte.class)
			return true;

		if (propertyClass == Character.class)
			return true;

		if (propertyClass == Date.class)
			return true;

		return false;
	}

	@SuppressWarnings("rawtypes")
	private String getName(java.lang.reflect.Type typeArgument) {
		if (typeArgument instanceof Class) {
			return ((Class) typeArgument).getName();
		}

		if (typeArgument instanceof TypeVariable) {
			return ((TypeVariable) typeArgument).getName();
		}

		if (typeArgument instanceof WildcardType) {
			return ((WildcardType) typeArgument).toString();
		}

		if (typeArgument instanceof GenericArrayType) {
			return ((GenericArrayType) typeArgument).toString();
		}

		if (typeArgument instanceof ParameterizedType) {
			return ((ParameterizedType) typeArgument).toString();
		}

		return null;
	}

}
