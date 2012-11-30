package com.pehrs.spring.api.doc.v2;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import bsh.EvalError;
import bsh.Interpreter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pehrs.spring.api.doc.v2.jackson.JacksonTypeModule;

import edu.emory.mathcs.backport.java.util.Arrays;

public class ObjectValueGenerator {
	
	private final static Logger log = Logger.getLogger(ObjectValueGenerator.class);

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public static void main(String[] args) throws ClassNotFoundException,
			JsonGenerationException, JsonMappingException, IOException {

		LogUtils.initLogging();
		
		System.setProperty("jackson.values", "jackson-values-sample.properties");
		System.setProperty("preconfig.values", "preconfig-values-sample.properties");

		Class<?> beanClass = Class.forName("com.pehrs.json.model.SampleClassWithDetails");
		System.out.println("JSON:\n" + class2JSONSample(beanClass));

	}
	
	public static String class2JSONSample(Class<?> beanClass) {
		Object value = generateValue(beanClass);

		System.out.println("value=" + value);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		mapper.registerModule(new JacksonTypeModule());

		ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
		
		try {
			return writer.writeValueAsString(value);
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Object generateValue(String className) throws ClassNotFoundException {
		
		Class beanClass = Class.forName(className);
		
		return generateValue(beanClass, null);
	}
	public static Object generateValue(Class<?> beanClass) {
		return generateValue(beanClass, null);
	}
		
	private static Object generateValue(Class<?> beanClass, Type propertyType) {

		try {
			if(beanClass==null) {
				return null;
			}
			Object val = getPreconfigValue(beanClass);
			if(val!=null) {
				return val;
			}
			
			
			// Collection Types
			// java.util.List<>
			if (beanClass.isAssignableFrom(List.class)) {
				return generateList(beanClass, propertyType);
			}

			// java.util.Set<>

			// java.util.Map
			if (beanClass.isAssignableFrom(Map.class)) {
				return generateMap(beanClass, propertyType);
			}


			// Check for basic types
			if (beanClass.isPrimitive()) {
				return generatePrimitiveValue(beanClass);
			}

			if (isPrimitive(beanClass)) {
				return generatePrimitiveValue(beanClass);
			}

			if (beanClass == String.class) {
				return "STRING";
			}

			if (beanClass == DateTime.class) {
				return new DateTime();
			}
			if (beanClass == Date.class) {
				return new Date();
			}
			if (beanClass == java.sql.Date.class) {
				return new java.sql.Date(System.currentTimeMillis());
			}
			if (beanClass == Timestamp.class) {
				return new Timestamp(System.currentTimeMillis());
			}

			if (beanClass.isEnum()) {

				Object[] consts = beanClass.getEnumConstants();
				return consts[0];

			}

			Object value = beanClass.newInstance();

			BeanDescriptor beanDescriptor = new BeanDescriptor(beanClass);

			System.out.println("beanDescriptor=" + beanDescriptor);

			PropertyDescriptor[] properties = PropertyUtils
					.getPropertyDescriptors(beanClass);
			for (PropertyDescriptor property : properties) {
				String name = property.getName();
				if (!name.equals("class")) {
					System.out.println("property[" + name + "]=" + property);
					
					if(!hasJsonIgnore(property)) {					
					// Method writeMethod = property.getWriteMethod();
					Class<?> propertyClass = property.getPropertyType();
					Type propType = property.getReadMethod().getGenericReturnType();
					
					
					TypeVariable<?>[] beanClassTypeParams = beanClass.getTypeParameters();
					if(beanClassTypeParams!=null && beanClassTypeParams.length>0) {
						System.out.println("----------------> beanClassTypeParams="+Arrays.toString(beanClassTypeParams));
						System.out.println("----------------> propType="+propType);
						for(TypeVariable<?> tvar:beanClassTypeParams) {
							System.out.println("----------------> "+(propType == tvar));
						}
					}

					// BeanInfo info = Introspector.getBeanInfo(beanClass);
					Class<?> ptype = property.getWriteMethod()
							.getParameterTypes()[0];

					Object propValue = generateValue(propertyClass, propType);
					// writeMethod.invoke(beanClass,
					// generateValue(propertyClass));
					PropertyUtils.setProperty(value, name, propValue);
					}
				}
			}

			return value;
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private static boolean hasJsonIgnore(PropertyDescriptor property) {
		
		for(Annotation annotation:property.getReadMethod().getAnnotations()) {
			if(annotation instanceof JsonIgnore) {
				return true;
			}
		}
		for(Annotation annotation:property.getWriteMethod().getAnnotations()) {
			if(annotation instanceof JsonIgnore) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Object generateMap(Class<?> beanClass, Type propertyType) {
		
		System.out.println("GENERATE MAP===============> "+beanClass.getName()+" propertyType="+propertyType);

		Class leftElementType = null;
		Class rightElementType = null;
		if(propertyType != null && propertyType instanceof ParameterizedType) {
			ParameterizedType aType = (ParameterizedType) propertyType;
		    Type[] fieldArgTypes = aType.getActualTypeArguments();
		    leftElementType = (Class)fieldArgTypes[0]; 
		    rightElementType = (Class)fieldArgTypes[1]; 
		}

		Map map = new HashMap();

		Object leftVal = generateValue(leftElementType);
		Object rightVal = generateValue(rightElementType);
		
		map.put(leftVal, rightVal);

		return map;
	}

	@SuppressWarnings({ "rawtypes" })
	private static List<?> generateList(Class<?> beanClass, Type propertyType) {
		
		System.out.println("GENERATE LIST===============> "+beanClass.getName()+" propertyType="+propertyType);

		Class elementType = null;
		if(propertyType != null && propertyType instanceof ParameterizedType) {
			ParameterizedType aType = (ParameterizedType) propertyType;
		    Type[] fieldArgTypes = aType.getActualTypeArguments();
		    for(Type fieldArgType : fieldArgTypes){
		    	elementType = (Class) fieldArgType;
		        System.out.println("elementType = " + elementType);
		    }
		}

		List list = new ArrayList();

		Object val = generateValue(elementType);
		
		list.add(val);

		return list;
	}

	private static boolean isPrimitive(Class<?> beanClass) {
		if (beanClass == Boolean.class)
			return true;
		if (beanClass == Character.class)
			return true;
		if (beanClass == Byte.class)
			return true;
		if (beanClass == Short.class)
			return true;
		if (beanClass == Integer.class)
			return true;
		if (beanClass == Long.class)
			return true;
		if (beanClass == Double.class)
			return true;
		if (beanClass == Float.class)
			return true;
		if (beanClass == BigInteger.class)
			return true;
		if (beanClass == BigDecimal.class)
			return true;
		return false;
	}

	private static Object generatePrimitiveValue(Class<?> propertyClass) {

		// boolean,
		if (propertyClass == Boolean.class || propertyClass == Boolean.TYPE) {
			return true;
		}

		// char
		if (propertyClass == Character.class || propertyClass == Character.TYPE) {
			return 'A';
		}

		// byte, short, int, long,
		if (propertyClass == Integer.TYPE || propertyClass == Integer.class) {
			return 42;
		}
		if (propertyClass == Byte.class || propertyClass == Byte.TYPE) {
			return (byte) 42;
		}

		if (propertyClass == Long.class || propertyClass == Long.TYPE) {
			return 42l;
		}

		if (propertyClass == Short.class || propertyClass == Short.TYPE) {
			return (short) 42;
		}

		if (propertyClass == BigInteger.class) {
			return new BigInteger("42");
		}

		// float, and double
		if (propertyClass == Double.class || propertyClass == Double.TYPE) {
			return 42.42d;
		}

		if (propertyClass == Float.class || propertyClass == Float.TYPE) {
			return 42.42f;
		}
		if (propertyClass == BigDecimal.class) {
			return new BigDecimal("42.42");
		}

		return null;
	}

	private static Map<String, String> preconfigValueScripts = null;
	
	private static Object getPreconfigValue(Class<?> beanClass) {

		String className = beanClass.getName();
		
		if(preconfigValueScripts==null) {
			preconfigValueScripts=new HashMap<String, String>();
			
			String preconfigValuesFile = System.getProperty("preconfig.values");
			if(preconfigValuesFile==null) {
				throw new RuntimeException("You must set the -Dpreconfig.values property");
			}
			Properties props = new Properties();
			FileInputStream in = null;
			try {
				in = new FileInputStream(preconfigValuesFile);
				props.load(in);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					if(in!=null) {
						in.close();
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			for(Object key:props.keySet()) {
				preconfigValueScripts.put(""+key, props.getProperty(""+key));
			}
		}
		
		String script = preconfigValueScripts.get(className);
		if(script==null) {
			return null;
		}

		Interpreter interpreter = new Interpreter();  // Construct an interpreter
		interpreter.setClassLoader(Thread.currentThread().getContextClassLoader());

		Object val = null;
		try {
			val = interpreter.eval(script);
		} catch (EvalError e) {
			throw new RuntimeException(e);
		}
		
		return val;
	}


}
