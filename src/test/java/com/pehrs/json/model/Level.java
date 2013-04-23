package com.pehrs.json.model;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * This represents a Institution grade It will be configured for each
 * Institution The database value is always and decimal value which is then
 * translated to the grade name
 * 
 * @author matti
 */
public class Level implements Serializable, Comparable<Level> {

	private static final long serialVersionUID = -4479479780977846192L;

	final public String superId;
	final public int value;
	final public String name;

	private Level(String superId, int value, String name) {
		super();
		this.superId = superId;
		this.value = value;
		this.name = name;
	}

	public static Level toGrade(String value) {
		String[] parts = value.split("/");
		if (parts == null || (parts.length != 3 && parts.length != 2)) {
			throw new IllegalArgumentException("" + value
					+ " is not a Grade value");
		}

		Level ret = null;
		if (parts.length == 2) {
			ret = toGrade(parts[0], parts[1]);
		} else {
			ret = toGrade(parts[0], Integer.parseInt(parts[1]));
		}

		if (ret == null) {
			throw new IllegalArgumentException("" + value
					+ " is not a Grade value");
		}
		return ret;
	}

	public static Level toGrade(String superId, String name) {
		return string2LevelMap.get(superId + "/" + name);
	}

	public static Level toGrade(String superId, int value) {
		return value2LevelMap.get(superId + "/" + value);
	}

	public static Level getClosestGrade(int value) {
		Level result = null;
		for (Level grade : value2LevelMap.values()) {
			if (value >= grade.value) {
				if (result == null) {
					result = grade;
				} else {
					if (grade.value >= result.value) {
						result = grade;
					}
				}
			}
		}
		return result;
	}

	private final static Map<String, Set<Level>> superId2LevelMap = new HashMap<String, Set<Level>>();
	private final static Map<String, Level> value2LevelMap = new HashMap<String, Level>();
	private final static Map<String, Level> string2LevelMap = new HashMap<String, Level>();

	static {

		Reader in = null;
		try {
			Properties props = new Properties();
			in = new InputStreamReader(
					Level.class.getResourceAsStream("/level.properties"));
			props.load(in);

			for (Object keyObj : props.keySet()) {
				String keyVal = (String) keyObj;
				String valStr = props.getProperty(keyVal);
				String[] parts = keyVal.split("/");
				String instId = parts[0];
				int value = Integer.parseInt(parts[1]);
				addGrade(new Level(instId, value, valStr));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private synchronized static void addGrade(Level level) {
		Set<Level> allLevels = superId2LevelMap.get(level.superId);
		if (allLevels == null) {
			allLevels = new TreeSet<Level>();
			superId2LevelMap.put(level.superId, allLevels);
		}
		allLevels.add(level);
		value2LevelMap.put(level.superId + "/" + level.value, level);
		string2LevelMap.put(level.superId + "/" + level.name, level);
	}

	public static Set<Level> getInstitutionGrades(String institutionId) {
		return superId2LevelMap.get(institutionId);
	}

	@Override
	public int compareTo(Level other) {
		return toString().compareTo(other.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((superId == null) ? 0 : superId.hashCode());
		result = prime * result + value;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Level other = (Level) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (superId == null) {
			if (other.superId != null) {
				return false;
			}
		} else if (!superId.equals(other.superId)) {
			return false;
		}
		if (value != other.value) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Level [superId=" + superId + ", value=" + value + ", name="
				+ name + "]";
	}

}
